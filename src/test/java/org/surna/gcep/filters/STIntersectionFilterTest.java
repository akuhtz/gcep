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
 *  tests intersect geometry.  intersect is defined as some point in test is in base.
 */
public class STIntersectionFilterTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;             // polygon which is a linear ring
	private Point pointIntersection;         // point is implemented as two coordinates
	private Point pointNoIntersect;
	private Geometry polyIntersection;
	private Geometry polyNoIntersect;
	private Geometry lineIntersection;        // jts implements a line as a linestring, which is like a polygon
	private Geometry lineNoIntersect;
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
		polyIntersection = geometryFactory.createPolygon(shell1, null);


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
		polyNoIntersect = geometryFactory.createPolygon(shell2, null);

		/*
		 *  Point is at sunnyside and the Chicago river, which is inside the test area polygon	
		 */
		Coordinate coordinate = new Coordinate(41.96242599151277,-87.6969051361084);
		pointIntersection = geometryFactory.createPoint(coordinate);  
		/*
		 *  Point is at Ravenswood Manor Park, which is north of Wilson and just outside the test area	
		 */
		coordinate  = new Coordinate(41.9654893873846,-87.70074605941772);
		pointNoIntersect = geometryFactory.createPoint(coordinate);
		/* This is a line that intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends north and west of welles park at former govenor Blagos house
		 * a line might be calculated on two points and extended to imply velocity
		 */
		Coordinate[] l1;
		l1 = new Coordinate[2];
		l1[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l1[1] = new Coordinate(41.962936567719815, -87.7021837234497); // Blagos house
		lineIntersection = new GeometryFactory().createLineString(l1);
		/* This is a line that does not intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends north and east of welles park at the montrose brown line stop
		 */
		Coordinate[] l2;
		l2 = new Coordinate[2];
		l2[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l2[1] = new Coordinate(41.96181967695292, -87.67506122589111); // montrose brown line
		lineNoIntersect = new GeometryFactory().createLineString(l2);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STIntersectionFilter#getInstance()}.
	 */
	@Test
	public void testGetInstance()
	{
		STIntersectionFilter test = STIntersectionFilter.getInstance();
		Assert.assertNotNull(test);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STIntersectionFilter#STIntersection(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Point inside the testArea.  If you make a geometry out of the intersection,
	 * it turns out to be a point with coordinates pointIntersection.
	 */
	@Test
	public void testSTIntersectionWithPointIntersection()
	{
		geomRes = STIntersectionFilter.getInstance().STIntersection(testArea, pointIntersection);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}


	/**
	 * Test method for {@link org.surna.gcep.filters.STIntersectionFilter#STIntersection(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Point outside the testArea means it does not create an intersection.  If you contstruct a 
	 * geometry of the intersection, you get an empty geometry collection.
	 */
	@Test
	public void testSTIntersectionNoIntersectWithPoint()
	{
		geomRes = STIntersectionFilter.getInstance().STIntersection(testArea, pointNoIntersect);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertTrue(geomRes.isEmpty());
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STIntersectionFilter#STIntersection(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Polygon inside the testArea means it will create another polygon which is the intersection of test and base
	 */
	@Test
	public void testSTIntersectionWithPolyIntersection()
	{
		geomRes = STIntersectionFilter.getInstance().STIntersection(testArea, polyIntersection);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STIntersectionFilter#STIntersection(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: Polygon outside the testArea means it does not intersect and should return an empty geometry
	 */
	@Test
	public void testSTIntersectionNoIntersectWithPoly()
	{
		geomRes = STIntersectionFilter.getInstance().STIntersection(testArea, polyNoIntersect);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertTrue(geomRes.isEmpty());

	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STIntersectionFilter#STIntersection(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: line starts outside the testArea and goes inside testArea means it intersects.  The intersection should be a line.
	 */
	@Test
	public void testSTIntersectionIntersectWithLine()
	{
		geomRes = STIntersectionFilter.getInstance().STIntersection(testArea, lineIntersection);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertFalse(geomRes.isEmpty());

	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STIntersectionFilter#STIntersection(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * Boundry condition: line starts outside the testArea and does not go inside testArea means it should produce an empty geometry
	 */
	@Test
	public void testSTIntersectionNoIntersectWithLine()
	{
		geomRes = STIntersectionFilter.getInstance().STIntersection(testArea, lineNoIntersect);
		Assert.assertTrue(geomRes.isValid());
		Assert.assertTrue(geomRes.isEmpty());
	}

}
