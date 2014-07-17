/**************************************************************************************
 * Copyright (C) 2009 - 2010 Surna, Inc. All rights reserved.                                *
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
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 * @author rpbrandt
 * 
 * Given two geometries base and test.  Test is said to be within base if its dimensions are spacially within base
 * This relationship is valid for A/A, A/L, A/P, L/L, L/P, and P/P
 */

public class STWithinFilterTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;
	private Point pointIn;
	private Point pointOut;
	private Point pointOn;
	private Point pointExcludes;

	/**
	 * @throws java.lang.Exception
	 * The testArea defines an area from near the 180 to the other side of the -180 latitude line
	 * that excludes the smaller area that includes the 180 / -180 latitude.
	 */
	@BeforeClass
	public void setUpBeforeClass() {
		//	This is run once before any of the tests are run
		geometryFactory = new GeometryFactory();
		Coordinate coordinate1 = new Coordinate(-160.0, -23.0);
		Coordinate coordinate2 = new Coordinate(170.0, -23.0);
		Coordinate coordinate3 = new Coordinate(170.0, -55.0);
		Coordinate coordinate4 = new Coordinate(-160.0, -55.0);
		Coordinate coordinate5 = new Coordinate(-160.0, -23.0);
		Coordinate[] coordinates = {coordinate1, coordinate2, coordinate3, coordinate4, coordinate5};
		CoordinateArraySequence coordinateArraySequence = new CoordinateArraySequence(coordinates);
		LinearRing shell = new LinearRing(coordinateArraySequence, geometryFactory);
		LinearRing[] holes = null;
		testArea = new Polygon(shell, holes, geometryFactory);
		Coordinate coordinateIn = 	new Coordinate(-150.0,-32.0);
		pointIn = geometryFactory.createPoint(coordinateIn);
		Coordinate coordinateOut = 	new Coordinate(65.0,-5.0);
		pointOut = geometryFactory.createPoint(coordinateOut);
		Coordinate coordinateOn = 	new Coordinate(170.0,-50.0);
		pointOn = geometryFactory.createPoint(coordinateOn);
		Coordinate coordinateExcludes = 	new Coordinate(180.0,-39.0);
		pointExcludes = geometryFactory.createPoint(coordinateExcludes);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STWithinFilter#getInstance()}.
	 */
	@Test
	public void testGetInstance()
	{
		STWithinFilter test = STWithinFilter.getInstance();
		Assert.assertNotNull(test);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STWithinFilter#STWithin(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Point inside the testArea
	 */
	@Test
	public void testSTWithinIn()
	{
		Assert.assertTrue(STWithinFilter.getInstance().STWithin(pointIn, testArea));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STWithinFilter#STWithin(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Point outside the testArea
	 */
	@Test
	public void testSTWithinOut()
	{
		Assert.assertFalse(STWithinFilter.getInstance().STWithin(pointOut, testArea));		
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STWithinFilter#STWithin(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Point on the boundry of the testArea
	 */
	@Test
	public void testSTWithinOn()
	{
		//		assertTrue(STWithinFilter.getInstance().STWithin(pointOn, testArea));
		// within is false, coveredBy would be true
		Assert.assertFalse(STWithinFilter.getInstance().STWithin(pointOn, testArea));		
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STWithinFilter#STWithin(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Point excluded on the 180 / -180 latitude outside the testArea
	 */
	@Test
	public void testSTWithinExcludes()
	{
		Assert.assertFalse(STWithinFilter.getInstance().STWithin(pointExcludes, testArea));		
	}

}
