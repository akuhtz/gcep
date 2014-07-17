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
 * Two geometries base and test are said to touch if some boundary of test touches a boundary of base
 * This relationship is valid for A/A, L/L, L/A, P/A and P/L
 * This relationship is undefined for P/P.  If both base and test are a point, this filter will return false.
 * This filter returns true if test touches base
 */

public class STTouchesFilterTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;             // polygon which is a linear ring
	private Point pointTouches;         // point is implemented as two coordinates
	private Point pointNoTouches;
	private Geometry pointTouchesLine;
	private Geometry polyTouches;
	private Geometry polyNoTouches;
	private Geometry lineTouches;        // jts implements a line as a linestring, which is like a polygon
	private Geometry lineNoTouches;

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

		/* the next polygon touches the testarea along the western ave border
		 * it starts at western and montrose
		 * north on western to  wilson
		 * east to wilson and leavitt
		 * south to leavitt and montrose
		 * west to western and montrose
		 */
		Coordinate[] c1;
		c1 = new Coordinate[5];
		c1[0] = new Coordinate(41.96128, -87.68867);  // western and montrose
		c1[1] = new Coordinate(41.96472, -87.68875); // western and wilson
		c1[2] = new Coordinate(41.96489905692884, -87.68388032913208);  // leavitt and wilson
		c1[3] = new Coordinate(41.9613410034882, -87.68375158309936);  // leavitt and montrose
		c1[4] = new Coordinate(41.96128, -87.68867); // western and montrose
		LinearRing shell1 = geometryFactory.createLinearRing(c1);
		polyTouches = geometryFactory.createPolygon(shell1, null);


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
		polyNoTouches = geometryFactory.createPolygon(shell2, null);

		/*
		 *  Point is at wilson and sacramento which touches the test area polygon	
		 */
		Coordinate coordinate = new Coordinate(41.96472, -87.70360); // wilson and sacramento
		pointTouches = geometryFactory.createPoint(coordinate);  
		/*
		 *  Point is at Ravenswood Manor Park, which is north of Wilson and just outside the test area	
		 */
		coordinate  = new Coordinate(41.9654893873846,-87.70074605941772);
		pointNoTouches = geometryFactory.createPoint(coordinate);
		/*
		 *  Point is at the western and montrose	
		 */
		coordinate  = new Coordinate(41.96128, -87.68867);
		pointTouchesLine = geometryFactory.createPoint(coordinate);
		/* This is a line that intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends western and montrose
		 * a line might be calculated on two points and extended to imply velocity
		 */
		Coordinate[] l1;
		l1 = new Coordinate[2];
		l1[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l1[1] = new Coordinate(41.96128, -87.68867); // western and montrose
		lineTouches = new GeometryFactory().createLineString(l1);
		/* This is a line that does not intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends north and east of welles park at the montrose brown line stop
		 */
		Coordinate[] l2;
		l2 = new Coordinate[2];
		l2[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l2[1] = new Coordinate(41.96181967695292, -87.67506122589111); // montrose brown line
		lineNoTouches = new GeometryFactory().createLineString(l2);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STTouchesFilter#getInstance()}.
	 */
	@Test
	public void testGetInstance()
	{
		STTouchesFilter test = STTouchesFilter.getInstance();
		Assert.assertNotNull(test);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STTouchesFilter#STTouches(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/P is undefined and returns false.
	 */
	@Test
	public void testSTPointTouchesWithPointNoTouches()
	{
		Assert.assertFalse(STTouchesFilter.getInstance().STTouches(pointTouches, pointNoTouches));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STTouchesFilter#STTouches(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/L
	 * Boundry condition: Point touches line is defined and returns true.
	 */
	@Test
	public void testSTlineTouchesWithPointTouchesLine()
	{
		Assert.assertTrue(STTouchesFilter.getInstance().STTouches(lineTouches, pointTouchesLine));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STTouchesFilter#STTouches(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/A
	 * Boundry condition: Point touches testarea is defined and returns true.
	 */
	@Test
	public void testSTtestareaWithPointTouches()
	{
		Assert.assertTrue(STTouchesFilter.getInstance().STTouches(testArea, pointTouches));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STTouchesFilter#STTouches(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/L
	 * Boundry condition: lines touch at welles park returns true.
	 */
	@Test
	public void testSTlineTouchesWithlineNoTouches()
	{
		Assert.assertTrue(STTouchesFilter.getInstance().STTouches(lineTouches, lineNoTouches));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STTouchesFilter#STTouches(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/L
	 * Boundry condition: line touches testArea. 
	 */
	@Test
	public void testSTtestAreaWithPointTouches()
	{
		Assert.assertTrue(STTouchesFilter.getInstance().STTouches(testArea, lineTouches));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STTouchesFilter#STTouches(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/L
	 * Boundry condition: line does not touch testArea. 
	 */
	@Test
	public void testSTtestAreaWithPointNoTouches()
	{
		Assert.assertFalse(STTouchesFilter.getInstance().STTouches(testArea, lineNoTouches));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STTouchesFilter#STTouches(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/A
	 * Boundry condition: polygon touches testArea. 
	 */
	@Test
	public void testSTtestAreaWithPolyTouches()
	{
		Assert.assertTrue(STTouchesFilter.getInstance().STTouches(testArea, polyTouches));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STTouchesFilter#STTouches(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/A
	 * Boundry condition: polygon does not touche testArea. 
	 */
	@Test
	public void testSTtestAreaWithPolyNoTouches()
	{
		Assert.assertFalse(STTouchesFilter.getInstance().STTouches(testArea, polyNoTouches));
	}

}
