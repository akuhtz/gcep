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
 * Two geometries base and test are said to overlap if some part of test spatially overlaps base
 * This relationship is undefined for P/L, P/A, L/A
 * If point is tested versus a line, this filter will return false
 * If point is tested versus a polygon, this filter will return false
 * If line is tested versus a polygon, this filter will return false.
 * This relationship is valid for P/P, A/A, L/L
 * This filter returns true if test overlaps base
 */

public class STOverlapsFilter
{
	//	org.apache.commons.logging.log used for logging
    private static final Log log = LogFactory.getLog(STOverlapsFilter.class);
    
    //	setting up for Singleton pattern
	private static final STOverlapsFilter INSTANCE = new STOverlapsFilter();
	
	private STOverlapsFilter()
	{
		
	}
	public static STOverlapsFilter getInstance()
	{
		return INSTANCE;
	}
	
	public boolean STOverlaps( Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			String logStmt = "STOverlaps is test = " + test.toString() + " test spacially overlaps base = " + base.toText();
			log.info(logStmt);			
		}
		boolean retVal = base.overlaps(test);
		
		return retVal;
		
	}
}
