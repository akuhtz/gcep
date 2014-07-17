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
import com.vividsolutions.jts.operation.buffer.BufferOp;

/**
 * @author rpbrandt
 *
 * The buffer of a Geometry at a distance d is the Polygon or MultiPolygon which contains all
 * points within a distance d of the Geometry. The distance d is interpreted according to the
 * Precision Model of the Geometry. Both positive and negative distances are supported.
 * 
 * In mathematical terms, buffering is defined as taking the Minkowski sum or difference of
 * the Geometry with a disc of radius equal to the absolute value of the buffer distance.
 * Positive and negative buffering is also referred to as dilation or erosion. In CAD/CAM
 * terms, buffering is referred to as computing an offset curve.
 * 
 * Buffering allows three different types of end caps to be specified.
 * These are:
 * CAP_ROUND The usual round end caps - default
 * CAP_BUTT End caps are truncated flat at the line ends
 * CAP_SQUARE End caps are squared off at the buffer distance beyond the line ends
 * 
 * Finally, since the exact buffer outline of a Geometry usually contains circular sections, the buffer
 * must be approximated by the linear Geometry supported by JTS. The degree of approximation may be specifed
 * as a buffer argument.  The default is 8.
 */

public class STBufferFilterTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;             // polygon which is a linear ring
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
		STBufferFilter test = STBufferFilter.getInstance();
		Assert.assertNotNull(test);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STBufferFilter#STBuffer(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Point.   The result will be the point.
	 */
	@Test
	public void testSTBufferPoint()
	{
		geomRes = STBufferFilter.getInstance().STBuffer(point,100.0,0,0);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STBufferFilter#STBuffer(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Polygon. The test will create another polygon possibly with less points.
	 */
	@Test
	public void testSTBufferWithtestArea()
	{
		geomRes = STBufferFilter.getInstance().STBuffer(testArea,50.0,16,0);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STBufferFilter#STBuffer(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: line.This test produces a line.
	 */
	@Test
	public void testSTBufferWithLine()
	{
		geomRes = STBufferFilter.getInstance().STBuffer(line,75.9,0,BufferOp.CAP_BUTT);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}
}
