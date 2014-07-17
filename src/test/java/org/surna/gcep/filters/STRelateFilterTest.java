/**************************************************************************************
 * Copyright (C) 2009 - 2010 Surna, Inc. All rights reserved.                         *
 * http://www.surna.org                                                               *
 * http://www.surna.com                                                               *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/

package org.surna.gcep.filters;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;

/**
 * @author rpbrandt
 * 
 * * 
 * The relate operation uses the DE-9IM pattern matrix to describe the relationship between two geometries.
 * This is just a more generalized method of to accomplish the operations that are defined under the JTS
 * operations contains,crosses,disjoint,equals,intersects,overlaps,touches,union, and within.
 * The DE-9IM matrix has the form:
 *               interior    boundary    exterior
 *     interior 
 *     boundary
 *     exterior
 *     The cells can have the values T,T,*,1,2 
 *     The pattern matrix contains the acceptable values for each of the intersection matrix cells. The possible pattern values are:
 *     T - An intersection must exist; dim = 0, 1, or 2. 
 *     F - An intersection must not exist; dim = -1. 
 *     * - It does not matter if an intersection exists or not; dim = -1, 0, 1, or 2. 
 *     0 - An intersection must exist and its maximum dimension must be 0; dim = 0. 
 *     1 - An intersection must exist and its maximum dimension must be 1; dim = 1. 
 *     2 - An intersection must exist and its maximum dimension must be 2; dim = 2.
 *     For example, the pattern matrix of the ST_Within() function for geometry combinations has the following form:
 *                   b 
 *             Interior Boundary Exterior 
 *   Interior      T       *        F 
 * a Boundary      *       *        F 
 *   Exterior      *       *        * 
 *
 *  Simply put, the ST_Within() function returns TRUE when the interiors of both geometries intersect,
 *  and the interior and boundary of geometry a does not intersect the exterior of geometry b. All other conditions do not matter.
 *
 *     The matrix for the overlap operation looks like:
 *               interior     boundary   exterior
 *     interior     T            *         T
 *     boundary     *            *         *
 *     exterior     T            *         *
 *     
 *     This would be passed to the relate function as a row wise string 'T*T***T**'   
 */

public class STRelateFilterTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;             // polygon which is a linear ring
	private Point pointRelate;         // point is implemented as two coordinates
	private Geometry polyRelate;
	private String DE9IMOverlap = "T*T***T**";
	private String DE9IMWithin = "T*F**F***";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public void setUpBeforeClass() {
		//	This is run once before any of the tests are run

		geometryFactory = new GeometryFactory();

		/**
		 * The testArea defines an area from montrose and western in chicago
		 * north to western and wilson
		 * west to wilson and sacramento
		 * south to montrose and sacramento
		 * east back to montrose and westerrn
		 */ 
		Coordinate[]coordinates;
		coordinates = new Coordinate[5];
		coordinates[0] = new Coordinate(41.96128, -87.68867); // montrose and western
		coordinates[1] = new Coordinate(41.96472, -87.68875); // western and wilson
		coordinates[2] = new Coordinate(41.96472, -87.70360); // wilson and sacramento
		coordinates[3] = new Coordinate(41.96121, -87.70347); // montrose and sacramento
		coordinates[4] = new Coordinate(41.96128, -87.68867); // montrose and western
		LinearRing shell = geometryFactory.createLinearRing(coordinates);
		testArea = geometryFactory.createPolygon(shell, null);

		/* the next polygon intersects with the testarea
		 * it starts at cmapbell and sunnyside
		 * south on campbell to half block short of montrose
		 * east to half block short of montrose and leavitt
		 * north to leavitt and sunnyside
		 * west to campbell and sunnyside
		 */
		Coordinate[] c1;
		c1 = new Coordinate[5];
		c1[0] = new Coordinate(41.96313, -87.69120);  // campbell and sunnyside
		c1[1] = new Coordinate(41.96214, -87.69128); // campbell not quite montrose
		c1[2] = new Coordinate(41.96220, -87.68382);  // leavitt not quite montrose
		c1[3] = new Coordinate(41.96319, -87.68382);  // leavitt and sunnyside
		c1[4] = new Coordinate(41.96313, -87.69120); // campbell and sunnyside
		LinearRing shell1 = geometryFactory.createLinearRing(c1);
		polyRelate = geometryFactory.createPolygon(shell1, null);

		/*
		 *  Point is at sunnyside and the Chicago river, which is inside the test area polygon	
		 */
		Coordinate coordinate = new Coordinate(41.96242599151277,-87.6969051361084);
		pointRelate = geometryFactory.createPoint(coordinate);  
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STRelateFilter#getInstance()}.
	 */
	@Test
	public void testGetInstance()
	{
		STRelateFilter test = STRelateFilter.getInstance();
		Assert.assertNotNull(test);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STRelateFilter#STRelate(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/A
	 * Boundry condition: Point inside the testArea and overlap operation. Tests as undefined and returns false.
	 */
	@Test
	public void testSTtestareaWithPointRelateOverlap()
	{
		Assert.assertFalse(STRelateFilter.getInstance().STRelate(testArea, pointRelate, DE9IMOverlap));
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STRelateFilter#STRelate(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/A
	 * Boundry condition: Point inside the testArea and within operation. Tests as true.
	 */
	@Test
	public void testSTtestareaWithPointRelateWithin()
	{
		Assert.assertTrue(STRelateFilter.getInstance().STRelate(pointRelate,testArea, DE9IMWithin));
	}


	/**
	 * Test method for {@link org.surna.gcep.filters.STRelateFilter#STRelate(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/A
	 * Boundry condition: Polygon inside the testArea means it overlaps
	 */
	@Test
	public void testSTtestareaWithPolyRelate()
	{
		Assert.assertTrue(STRelateFilter.getInstance().STRelate(testArea, polyRelate, DE9IMOverlap));
	}

}
