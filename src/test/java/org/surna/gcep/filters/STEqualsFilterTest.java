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
 * Two geometries base and test are said to be equal if test is spatially equal to base
 * This means that P/P, L/L, A/A are defined and possibly equal.  Any other combination is undefined and returns false.
 */

public class STEqualsFilterTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;             // polygon which is a linear ring
	private Point pointEquals;         // point is implemented as two coordinates
	private Point pointNoEquals;
	private Geometry polyEquals;
	private Geometry polyNoEquals;
	private Geometry lineEquals;        // jts implements a line as a linestring, which is like a polygon
	private Geometry lineNoEquals;


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
		polyEquals = geometryFactory.createPolygon(shell1, null);


		/*  The next poly gon lies entirely outside of the test area.
		 *  It starts at berteau and western
		 *  then east to berteau and leavitt
		 *  then south to leavitt and irving park
		 *  then west to irving park and western
		 *  then north to berteau and western	
		 */
		Coordinate[] c2;
		c2 = new Coordinate[5];
		c2[0] = new Coordinate(41.95761, -87.68862);  // berteau and western
		c2[1] = new Coordinate(41.95761, -87.68369); // berteau and leavitt
		c2[2] = new Coordinate(41.95413, -87.68369); // leavitt and irving park
		c2[3] = new Coordinate(41.95410, -87.68845); // irving park and western
		c2[4] = new Coordinate(41.95761, -87.68862); // berteau and western
		LinearRing shell2 = geometryFactory.createLinearRing(c2);
		polyNoEquals = geometryFactory.createPolygon(shell2, null);

		/*
		 *  Point is at sunnyside and the Chicago river, which is inside the test area polygon	
		 */
		Coordinate coordinate = new Coordinate(41.96242599151277,-87.6969051361084);
		pointEquals = geometryFactory.createPoint(coordinate);  
		/*
		 *  Point is at Ravenswood Manor Park, which is north of Wilson and just outside the test area	
		 */
		coordinate  = new Coordinate(41.9654893873846,-87.70074605941772);
		pointNoEquals = geometryFactory.createPoint(coordinate);
		/* This is a line that intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends north and west of welles park at former govenor Blagos house
		 * a line might be calculated on two points and extended to imply velocity
		 */
		Coordinate[] l1;
		l1 = new Coordinate[2];
		l1[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l1[1] = new Coordinate(41.962936567719815, -87.7021837234497); // Blagos house
		lineEquals = new GeometryFactory().createLineString(l1);
		/* This is a line that does not intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends north and east of welles park at the montrose brown line stop
		 */
		Coordinate[] l2;
		l2 = new Coordinate[2];
		l2[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l2[1] = new Coordinate(41.96181967695292, -87.67506122589111); // montrose brown line
		lineNoEquals = new GeometryFactory().createLineString(l2);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#getInstance()}.
	 */
	@Test
	public void testGetInstance()
	{
		STEqualsFilter test = STEqualsFilter.getInstance();
		Assert.assertNotNull(test);
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#STEquals(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/L
	 * Boundry condition: Point with line is undefined and returns false.
	 */
	@Test
	public void testSTlineEqualsWithPointEquals()
	{
		Assert.assertFalse(STEqualsFilter.getInstance().STEquals(lineEquals, pointEquals));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#STEquals(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/A
	 * Boundry condition: Point inside the testArea. Tests as undefined and returns false.
	 */
	@Test
	public void testSTEqualsWithPointEquals()
	{
		Assert.assertFalse(STEqualsFilter.getInstance().STEquals(testArea, pointEquals));
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#STEquals(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/P
	 * Boundry condition: Point with point is defined and returns equal.
	 */
	@Test
	public void testSTPointEqualsWithPointEquals()
	{
		Assert.assertTrue(STEqualsFilter.getInstance().STEquals(pointEquals, pointEquals));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#STEquals(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/P
	 * Boundry condition: Point not equal point. Tests as defined and returns false.
	 */
	@Test
	public void testSTpointNoEqualsWithPointEquals()
	{
		Assert.assertFalse(STEqualsFilter.getInstance().STEquals(pointNoEquals, pointEquals));
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#STEquals(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/A is undefined and returns false
	 */
	@Test
	public void testSTEtestareaWithLine()
	{
		Assert.assertFalse(STEqualsFilter.getInstance().STEquals(testArea, lineNoEquals));
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#STEquals(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/L
	 * Boundry condition: line equals line
	 */
	@Test
	public void testSTlineEqualsLine()
	{
		Assert.assertTrue(STEqualsFilter.getInstance().STEquals(lineEquals, lineEquals));
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#STEquals(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/L
	 * Boundry condition: line does not equal line
	 */
	@Test
	public void testSTlineNoEqualsWithLine()
	{
		Assert.assertFalse(STEqualsFilter.getInstance().STEquals(lineEquals, lineNoEquals));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#STEquals(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/A
	 * Boundry condition: Polygon equals polygon
	 */
	@Test
	public void testSTPolyEqualsWithPolyEquals()
	{
		Assert.assertTrue(STEqualsFilter.getInstance().STEquals(polyEquals, polyEquals));
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STEqualsFilter#STEquals(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/A
	 * Boundry condition: Polygon does not equal polygon
	 */
	@Test
	public void testSTPolyEqualsWithPolyNoEquals()
	{
		Assert.assertFalse(STEqualsFilter.getInstance().STEquals(polyNoEquals, polyEquals));
	}

}
