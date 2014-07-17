/**************************************************************************************
 * Copyright (C) 2009 - 2010 Surna, Inc. All rights reserved.                                *
 * http://www.surna.org                                                               *
 * http://www.surna.com                                                               *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/

package org.surna.gcep.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author rpbrandt
 *
 * The intersection of two Geometries A and B is the set of all points which lie in both A and B.
 * Two geometries base and test are said to intersect if some part of test is contained in base
 * If two geometries intersect, the result will be the lowest form
 * If a point intersects a polygon, you will get a point returned
 * If a line intersects a polygon, you will get the portion of the line that is inside the polygon.
 * This filter returns the intersection of two geometries or an empty geometry if there is no intersection
 */

public class STIntersectionFilter
{
	//	org.apache.commons.logging.log used for logging
    private static final Log log = LogFactory.getLog(STIntersectionFilter.class);
    
    //	setting up for Singleton pattern
	private static final STIntersectionFilter INSTANCE = new STIntersectionFilter();
	
	private STIntersectionFilter()
	{
		
	}
	public static STIntersectionFilter getInstance()
	{
		return INSTANCE;
	}
	
	public Geometry STIntersection( Geometry base, Geometry test)
	{
		
		Geometry retVal = base.intersection(test);
		if(log.isInfoEnabled())
		{
			String logStmt = "STIntersection is test = " + test.toString() + retVal.toString() + " part of test is within base = " + base.toText();
			log.info(logStmt);			
		}
		
		return retVal;
		
	}
}
