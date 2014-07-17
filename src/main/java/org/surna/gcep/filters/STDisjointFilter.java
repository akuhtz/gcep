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
 * Two geometries base and test are said to be Disjoint if no points of test are contained in base
 * This filter returns true if test is disjoint base
 */

public class STDisjointFilter
{
	//	org.apache.commons.logging.log used for logging
    private static final Log log = LogFactory.getLog(STDisjointFilter.class);
    
    //	setting up for Singleton pattern
	private static final STDisjointFilter INSTANCE = new STDisjointFilter();
	
	private STDisjointFilter()
	{
		
	}
	public static STDisjointFilter getInstance()
	{
		return INSTANCE;
	}
	
	public boolean STDisjoint( Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			String logStmt = "STDisjoint is test = " + test.toString() + " no part of test is within base = " + base.toText();
			log.info(logStmt);			
		}
		boolean retVal = base.disjoint(test);
		
		return retVal;
		
	}
}

