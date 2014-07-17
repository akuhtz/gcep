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
 * Two geometries base and test are said to touch if some boundary of test touches a boundary of base
 * This relationship is valid for A/A, L/L, L/A, P/A and P/L
 * This relationship is undefined for P/P.  If both base and test are a point, this filter will return false.
 * This filter returns true if test touches base
 */

public class STTouchesFilter
{
	//	org.apache.commons.logging.log used for logging
    private static final Log log = LogFactory.getLog(STTouchesFilter.class);
    
    //	setting up for Singleton pattern
	private static final STTouchesFilter INSTANCE = new STTouchesFilter();
	
	private STTouchesFilter()
	{
		
	}
	public static STTouchesFilter getInstance()
	{
		return INSTANCE;
	}
	
	public boolean STTouches( Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			String logStmt = "STTouches is test = " + test.toString() + " some boundary of test touches a boundary of base = " + base.toText();
			log.info(logStmt);			
		}
		boolean retVal = base.touches(test);
		
		return retVal;
		
	}
}
