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
 *  tests ConvexHull geometry.
 *  
 * The convex hull of a Geometry is the smallest convex Polygon that contains all the points in
 * the Geometry. If the convex hull contains fewer than 3 points, a lower dimension Geometry
 * is returned, specified as follows:
 * Number of Points in convex hull Geometry Class of result
 *          0                       empty GeometryCollection
 *          1                       Point
 *          2                       LineString
 *          3                       or more Polygon
 */
public class STConvexHullFilterTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;             // polygon which is a square
	private Geometry testArea2;            // polygon with extra points
	private Point point;                   // point is implemented as two coordinates
	private Geometry line;                 // jts implements a line as a linestring, which is like a polygon
	private Geometry geomRes;


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

		/**
		 * The testArea2 defines an area from montrose and western in chicago
		 * north to western and wilson
		 * south west to sunnyside and campbell
		 * north west to short of wilson and fransisco
		 * north west to wilson and sacramento
		 * south to montrose and sacramento
		 * east back to montrose and westerrn
		 */ 
		Coordinate[]coordinates2;
		coordinates2 = new Coordinate[7];
		coordinates2[0] = new Coordinate(41.96128, -87.68867); // montrose and western
		coordinates2[1] = new Coordinate(41.96472, -87.68875); // western and wilson
		coordinates2[2] = new Coordinate(41.963121,-87.691156); // sunnyside and campbell
		coordinates2[3] = new Coordinate(41.964228, -87.70100); // near wilson and fransisco
		coordinates2[4] = new Coordinate(41.96472, -87.70360); // wilson and sacramento
		coordinates2[5] = new Coordinate(41.96121, -87.70347); // montrose and sacramento
		coordinates2[6] = new Coordinate(41.96128, -87.68867); // montrose and western
		LinearRing shell2 = geometryFactory.createLinearRing(coordinates2);
		testArea2 = geometryFactory.createPolygon(shell2, null);

		/*
		 *  Point is at sunnyside and the Chicago river.	
		 */
		Coordinate coordinate = new Coordinate(41.96242599151277,-87.6969051361084);
		point = geometryFactory.createPoint(coordinate);  

		/* This is a line that starts in welles park, which is outside the test area
		 * it ends north and west of welles park at former govenor Blagos house
		 * a line might be calculated on two points and extended to imply velocity
		 */
		Coordinate[] l1;
		l1 = new Coordinate[2];
		l1[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l1[1] = new Coordinate(41.962936567719815, -87.7021837234497); // Blagos house
		line = new GeometryFactory().createLineString(l1);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STUnionFilter#getInstance()}.
	 */
	@Test
	public void testGetInstance()
	{
		STConvexHullFilter test = STConvexHullFilter.getInstance();
		Assert.assertNotNull(test);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STConvexHullFilter#STConvexHull(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Point.   The result will be the point.
	 */
	@Test
	public void testSTConvexHullPoint()
	{
		geomRes = STConvexHullFilter.getInstance().STConvexHull(point);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STConvexHullFilter#STConvexHull(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Polygon. The test will create another polygon possibly with less points.
	 */
	@Test
	public void testSTConvexHullWithtestArea()
	{
		geomRes = STConvexHullFilter.getInstance().STConvexHull(testArea);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STConvexHullFilter#STConvexHull(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Polygon. With extra interior points. 
	 * The test will create another polygon with less points. It should be very 
	 * similar to testarea results.  Will have to be visually inspected.
	 */
	@Test
	public void testSTConvexHullWithtestArea2()
	{
		geomRes = STConvexHullFilter.getInstance().STConvexHull(testArea2);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STConvexHullFilter#STConvexHull(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: line.This test produces a line.
	 */
	@Test
	public void testSTConvexHullWithLine()
	{
		geomRes = STConvexHullFilter.getInstance().STConvexHull(line);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}
}
