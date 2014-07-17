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
 * Two geometries base and test are said to cross if some part of test spatially crosses base
 * The Crosses relation applies to P/L, P/A, L/L and L/A situations. 
 * point crossing point is undefined.  If both test and base are a point, this filter will return false
 * polygon crossing polygon is undefined.  If both test and base are polygons, this filter will return false.
 */


public class STCrossesFilterTest
{
	private GeometryFactory geometryFactory;
	private Geometry testArea;             // polygon which is a linear ring
	private Point pointCrosses;         // point is implemented as two coordinates
	private Point pointNoCrosses;
	private Geometry polyCrosses;
	private Geometry lineCrosses;        // jts implements a line as a linestring, which is like a polygon
	private Geometry lineNoCrosses;
	private Geometry lineCrossesLine;


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
		polyCrosses = geometryFactory.createPolygon(shell1, null);

		/*
		 *  Point is at sunnyside and the Chicago river, which is inside the test area polygon	
		 */
		Coordinate coordinate = new Coordinate(41.96242599151277,-87.6969051361084);
		pointCrosses = geometryFactory.createPoint(coordinate);  
		/*
		 *  Point is at Ravenswood Manor Park, which is north of Wilson and just outside the test area	
		 */
		coordinate  = new Coordinate(41.9654893873846,-87.70074605941772);
		pointNoCrosses = geometryFactory.createPoint(coordinate);
		/* This is a line that intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends north and west of welles park at former govenor Blagos house
		 * a line might be calculated on two points and extended to imply velocity
		 */
		Coordinate[] l1;
		l1 = new Coordinate[2];
		l1[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l1[1] = new Coordinate(41.962936567719815, -87.7021837234497); // Blagos house
		lineCrosses = new GeometryFactory().createLineString(l1);
		/* This is a line that intersects with the lineCrosses
		 * it starts on Montrose, which is south of blagos house
		 * it ends north and east of former govenor Blagos house  at ravenswood manor park
		 */
		Coordinate[] l1c;
		l1c = new Coordinate[2];
		l1c[0] = new Coordinate(41.96121335662364, -87.7025055885315);  // montrose and richmond
		l1c[1] = new Coordinate(41.96547343257932, -87.70033836364746); // ravenswood manor park
		lineCrossesLine = new GeometryFactory().createLineString(l1c);	
		/* This is a line that does not intersects with the testarea
		 * it starts in welles park, which is outside the test area
		 * it ends north and east of welles park at the montrose brown line stop
		 */
		Coordinate[] l2;
		l2 = new Coordinate[2];
		l2[0] = new Coordinate(41.96236216919921, -87.68566131591797);  // welles park
		l2[1] = new Coordinate(41.96181967695292, -87.67506122589111); // montrose brown line
		lineNoCrosses = new GeometryFactory().createLineString(l2);
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STCrossesFilter#getInstance()}.
	 */
	@Test
	public void testGetInstance()
	{
		STCrossesFilter test = STCrossesFilter.getInstance();
		Assert.assertNotNull(test);
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STCrossesFilter#STCrosses(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/P - undefined returns false
	 */
	@Test
	public void testSTPointCrossesPoint()
	{
		Assert.assertFalse(STCrossesFilter.getInstance().STCrosses(pointCrosses, pointCrosses));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STCrossesFilter#STCrosses(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/A
	 * Boundry condition: Point inside the testArea. Tests as false according to jts test suite.
	 */
	@Test
	public void testSTCrossesWithPointCrosses()
	{
		Assert.assertFalse(STCrossesFilter.getInstance().STCrosses(testArea, pointCrosses));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STCrossesFilter#STCrosses(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * P/A
	 * Boundry condition: Point outside the testArea means it does not cross.
	 */
	@Test
	public void testSTCrossesNoCrossesWithPoint()
	{
		Assert.assertFalse(STCrossesFilter.getInstance().STCrosses(testArea, pointNoCrosses));		
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STCrossesFilter#STCrosses(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * A/A is undefined and returns false
	 * Boundry condition: Polygon inside the testArea 
	 */
	@Test
	public void testSTCrossesWithPolyCrosses()
	{
		Assert.assertFalse(STCrossesFilter.getInstance().STCrosses(testArea, polyCrosses));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STCrossesFilter#STCrosses(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/A
	 * Boundry condition: line starts outside the testArea and goes inside testArea means it crosses
	 */
	@Test
	public void testSTCrossesCrossesWithLine()
	{
		Assert.assertTrue(STCrossesFilter.getInstance().STCrosses(testArea, lineCrosses));
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STCrossesFilter#STCrosses(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/A
	 * Boundry condition: line starts outside the testArea and does not go inside testArea means it does not cross
	 */
	@Test
	public void testSTCrossesNoCrossesWithLine()
	{
		Assert.assertFalse(STCrossesFilter.getInstance().STCrosses(testArea, lineNoCrosses));
	}

	/**
	 * Test method for {@link org.surna.gcep.filters.STCrossesFilter#STCrosses(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/L
	 * Boundry condition: line crosses line
	 */
	@Test
	public void testSTlineCrossesLine()
	{
		Assert.assertTrue(STCrossesFilter.getInstance().STCrosses(lineCrossesLine, lineCrosses));
	}
	/**
	 * Test method for {@link org.surna.gcep.filters.STCrossesFilter#STCrosses(com.vividsolutions.jts.geom.Geometry, com.vividsolutions.jts.geom.Geometry)}.
	 * L/L
	 * Boundry condition: line starts line does not cross line
	 */
	@Test
	public void testSTlineNoCrossesWithLine()
	{
		Assert.assertFalse(STCrossesFilter.getInstance().STCrosses(lineCrosses, lineNoCrosses));
	}

}
