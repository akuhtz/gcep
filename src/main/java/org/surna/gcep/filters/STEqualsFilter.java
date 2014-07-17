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
 * Two geometries base and test are said to be equal if test is spatially equal to base
 * This filter returns true if test is spacially equal to base
 */

public class STEqualsFilter
{
	//	org.apache.commons.logging.log used for logging
    private static final Log log = LogFactory.getLog(STEqualsFilter.class);
    
    //	setting up for Singleton pattern
	private static final STEqualsFilter INSTANCE = new STEqualsFilter();
	
	private STEqualsFilter()
	{
		
	}
	public static STEqualsFilter getInstance()
	{
		return INSTANCE;
	}
	
	public boolean STEquals( Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			String logStmt = "STEquals is test = " + test.toString() + " test is spacially equal to base = " + base.toText();
			log.info(logStmt);			
		}
		boolean retVal = base.equals(test);
		
		return retVal;
		
	}
}
