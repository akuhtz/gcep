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
 * Two geometries base and test are said to cross if some part of test spatially crosses base
 * The Crosses relation applies to P/L, P/A, L/L and L/A situations. 
 * point crossing point is undefined.  If both test and base are a point, this filter will return false
 * polygon crossing polygon is undefined.  If both test and base are polygons, this filter will return false.
 * This filter returns true if test crosses base
 */

public class STCrossesFilter
{
	//	org.apache.commons.logging.log used for logging
    private static final Log log = LogFactory.getLog(STCrossesFilter.class);
    
    //	setting up for Singleton pattern
	private static final STCrossesFilter INSTANCE = new STCrossesFilter();
	
	private STCrossesFilter()
	{
		
	}
	public static STCrossesFilter getInstance()
	{
		return INSTANCE;
	}
	
	public boolean STCrosses( Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			String logStmt = "STCrosses is test = " + test.toString() + " some part of test spatially crosses base = " + base.toText();
			log.info(logStmt);			
		}
		boolean retVal = base.crosses(test);
		
		return retVal;
		
	}
}
