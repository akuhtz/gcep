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
 * The convex hull of a Geometry is the smallest convex Polygon that contains all the points in
 * the Geometry. If the convex hull contains fewer than 3 points, a lower dimension Geometry
 * is returned, specified as follows:
 * Number of Points in convex hull Geometry Class of result
 *          0                       empty GeometryCollection
 *          1                       Point
 *          2                       LineString
 *          3                       or more Polygon
 */

public class STConvexHullFilter
{
	//	org.apache.commons.logging.log used for logging
    private static final Log log = LogFactory.getLog(STConvexHullFilter.class);
    
    //	setting up for Singleton pattern
	private static final STConvexHullFilter INSTANCE = new STConvexHullFilter();
	
	private STConvexHullFilter()
	{
		
	}
	public static STConvexHullFilter getInstance()
	{
		return INSTANCE;
	}
	
	public Geometry STConvexHull( Geometry base)
	{
		Geometry retVal = base.convexHull();
		if(log.isInfoEnabled())
		{
			String logStmt = "STConvexHullIntersection is base = " + base.toString() + retVal.toString();
			log.info(logStmt);			
		}
		
		return retVal;
		
	}
}
