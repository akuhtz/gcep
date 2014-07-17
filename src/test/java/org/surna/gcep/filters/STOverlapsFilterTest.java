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
 * Two geometries base and test are said to overlap if some part of test spatially overlaps base
 * This relationship is undefined for P/L, P/A, L/A
 * If point is tested versus a line, this filter will return false
 * If point is tested versus a polygon, this filter will return false
 * If line is tested versus a polygon, this filter will return false.
 * This relationship is valid for P/P, A/A, L/L
 * This filter returns true if test overlaps base
 */

public class STOverlapsFilterTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;             // polygon which is a linear ring
	private Point pointOverlaps;         // point is implemented as two coordinates
	private Point pointNoOverlaps;
	private Geometry polyOverlaps;
	private Geometry polyNoOverlaps;
	private Geometry lineOverlaps;        // jts implements a line as a linestring, which is like a polygon
	private Geometry lineNoOverlaps;
	private Geometry lineOverlapsLine;


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
		polyOverlaps = geometryFactory.createPolygon(shell1, null);


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
		polyNoOverlaps = geometryFactory.createPolygon(shell2, null);

		/*
		 *  Point is at sunnyside and the Chicago river, which is inside the test area polygon	
		 */
		Coordinate coordinate = new Coordinate(41.96242599151277,-87.6969051361084);
		pointOverlaps = geometryFactory.createPoint(coordinate);  
		/*
		 *  Point is at Ravenswood Manor Park, which is north of Wilson and just outside the test area	
		 */
		coordinate  = new Coordinate(41.9654893873846,-87.70074605941772);
		pointNoOverlaps = geometryFactory.createPoint(coordinate);
		/* This is a line that intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends north and west of welles park at former govenor Blagos house
		 * a line might be calculated on two points and extended to imply velocity
		 */
		Coordinate[] l1;
		l1 = new Coordinate[2];
		l1[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l1[1] = new Coordinate(41.962936567719815, -87.7021837234497); // Blagos house
		lineOverlaps = new GeometryFactory().createLineString(l1);
		/* This is a line that does not intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends north and east of welles park at the montrose brown line stop
		 */
		Coordinate[] l2;
		l2 = new Coordinate[2];
		l2[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l2[1] = new Coordinate(41.96181967695292, -87.67506122589111); // montrose brown line
		lineNoOverlaps = new GeometryFactory().createLineString(l2);

		/* This is a line that overlaps lineOverlaps
		 * it starts in welles park and ends a little east of blagos house
		 */
		Coordinate[] l2o;
		l2o = new Coordinate[2];
		l2o[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l2o[1] = new Coordinate(41.96181967695292, -87.67506122589111); // 
		lineOverlapsLine = new GeometryFactory().createLineString(l2o);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#getInstance()}.
	 */
	@Test
	public void testGetInstance()
	{
		STOverlapsFilter test = STOverlapsFilter.getInstance();
		Assert.assertNotNull(test);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#STOverlaps(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/L
	 * Boundry condition: Point with line is undefined and returns false.
	 */
	@Test
	public void testSTlineOverlapsWithPointOverlaps()
	{
		Assert.assertFalse(STOverlapsFilter.getInstance().STOverlaps(lineOverlaps, pointOverlaps));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#STOverlaps(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/A
	 * Boundry condition: Point inside the testArea. Tests as undefined and returns false.
	 */
	@Test
	public void testSTtestareaWithPointOverlaps()
	{
		Assert.assertFalse(STOverlapsFilter.getInstance().STOverlaps(testArea, pointOverlaps));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#STOverlaps(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/P
	 * Boundry condition: Point with same point is defined but tests false according to JTS test suite.
	 */
	@Test
	public void testSTpointOverlapsWithPointOverlaps()
	{
		Assert.assertFalse(STOverlapsFilter.getInstance().STOverlaps(pointOverlaps, pointOverlaps));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#STOverlaps(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/P
	 * Boundry condition: Point with other point is defined and returns false.
	 */
	@Test
	public void testSTpointNoOverlapsWithPointOverlaps()
	{
		Assert.assertFalse(STOverlapsFilter.getInstance().STOverlaps(pointNoOverlaps, pointOverlaps));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#STOverlaps(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/A
	 * Boundry condition: line with polygon is undefined and returns false.
	 */
	@Test
	public void testSTtestareawithlineOverlaps()
	{
		Assert.assertFalse(STOverlapsFilter.getInstance().STOverlaps(testArea, lineOverlaps));		
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#STOverlaps(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/L
	 * Boundry condition: line with line overlap is defined and tests false according to JTS test suite.
	 */
	@Test
	public void testSTlineOverlapsLinewithlineOverlaps()
	{
		Assert.assertFalse(STOverlapsFilter.getInstance().STOverlaps(lineOverlapsLine, lineOverlaps));		
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#STOverlaps(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/L
	 * Boundry condition: line with a line that does not overlap is defined and returns false.
	 */
	@Test
	public void testSTlineOverlapswithlineNoOverlaps()
	{
		Assert.assertFalse(STOverlapsFilter.getInstance().STOverlaps(lineOverlaps, lineNoOverlaps));		
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#STOverlaps(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/A
	 * Boundry condition: Polygon inside the testArea means it overlaps
	 */
	@Test
	public void testSTtestareaWithPolyOverlaps()
	{
		Assert.assertTrue(STOverlapsFilter.getInstance().STOverlaps(testArea, polyOverlaps));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STOverlapsFilter#STOverlaps(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/A
	 * Boundry condition: Polygon outside the testArea means it does not overlap
	 */
	@Test
	public void testSTtestareaWithPolyNoOverlap()
	{
		Assert.assertFalse(STOverlapsFilter.getInstance().STOverlaps(testArea, polyNoOverlaps));
	}

}
