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
 * @author dphanson
 *
 */
public class GisFnTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;
	private Point pointIn;
	private Geometry geomRes;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public void setUpBeforeClass() {
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
	}

	@Test
	public void testSTContains ()
	{
		Assert.assertTrue(GisFn.STContains (testArea, pointIn));
	}

	@Test
	public void testSTWithin ()
	{
		Assert.assertTrue(GisFn.STWithin (pointIn, testArea));
	}
	@Test
	/**
	 * test for geometry pointIN intersects testArea.  Additional tests found in testSTIntersects.  In this case,
	 * a point intersecting a polygon is the same as a point being contained by a polygon
	 * 
	 */
	public void testSTIntersects ()
	{
		Assert.assertTrue(GisFn.STIntersects (testArea, pointIn));
	}

	@Test
	/**
	 * test for geometry pointIN disjoint testArea.  Additional tests found in testSTDisjoint.  In this case,
	 * a point disjoint a polygon is the same as a point not being contained by a polygon
	 * 
	 */
	public void testSTDisjoint ()
	{
		Assert.assertFalse(GisFn.STDisjoint (testArea, pointIn));
	}
	@Test
	/**
	 * test for geometry pointIN disjoint testArea.  Additional tests found in testSTDisjoint.  In this case,
	 * a point disjoint a polygon is the same as a point not being contained by a polygon
	 * 
	 */
	public void testSTEquals ()
	{
		Assert.assertFalse(GisFn.STEquals (testArea, pointIn));
	}
	@Test
	/**
	 * test for geometry pointIN disjoint testArea.  Additional tests found in testSTDisjoint.  In this case,
	 * a point disjoint a polygon is the same as a point not being contained by a polygon
	 * 
	 */
	public void testSTTouches ()
	{
		Assert.assertFalse(GisFn.STTouches (testArea, pointIn));
	}
	@Test
	/**
	 * test for geometry pointIN disjoint testArea.  Additional tests found in testSTDisjoint.  In this case,
	 * a point disjoint a polygon is the same as a point not being contained by a polygon
	 * 
	 */
	public void testSTCrosses ()
	{
		Assert.assertFalse(GisFn.STCrosses (testArea, pointIn));
	}
	@Test
	/**
	 * test for geometry pointIN disjoint testArea.  Additional tests found in testSTDisjoint.  In this case,
	 * a point disjoint a polygon is the same as a point not being contained by a polygon
	 * 
	 */
	public void testSTOverlaps ()
	{
		Assert.assertFalse(GisFn.STOverlaps (testArea, pointIn));
	}
	@Test
	/**
	 * test for geometry pointIN intersection of testArea.  Additional tests found in testSTIntersectiont.
	 */
	public void testSTIntersection ()
	{
		geomRes = GisFn.STIntersection (testArea, pointIn);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}
	@Test
	/**
	 * test for geometry pointIN union of of testArea.  Additional tests found in testSTUnion.
	 */
	public void testSTUnion ()
	{
		geomRes = GisFn.STUnion (testArea, pointIn);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}
	@Test
	/**
	 * test for geometry convex hull of testArea.  Additional tests found in testSTConvexHull.
	 */
	public void testSTConvexHull ()
	{
		geomRes = GisFn.STConvexHull (testArea);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}
	@Test
	/**
	 * test for geometry relate with de9im set to overlap relationship.
	 */
	public void testSTRelate ()
	{
		Assert.assertFalse(GisFn.STRelate (testArea, pointIn, "T*T***T**"));
	}
	@Test
	/**
	 * test for geometry buffer of testArea.  Additional tests found in testSTBuffer.
	 */
	public void testSTBuffer ()
	{
		geomRes = GisFn.STBuffer (testArea, 100.0, 0, 0);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}

}
