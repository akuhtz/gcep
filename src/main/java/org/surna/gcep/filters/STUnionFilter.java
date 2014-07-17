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
 * The union of two Geometries A and B is the set of all points which lie in A or B.
 * This filter returns the union of two geometries or an empty geometry if there is no intersection
 */

public class STUnionFilter
{
	//	org.apache.commons.logging.log used for logging
    private static final Log log = LogFactory.getLog(STUnionFilter.class);
    
    //	setting up for Singleton pattern
	private static final STUnionFilter INSTANCE = new STUnionFilter();
	
	private STUnionFilter()
	{
		
	}
	public static STUnionFilter getInstance()
	{
		return INSTANCE;
	}
	
	public Geometry STUnion( Geometry base, Geometry test)
	{
		
		Geometry retVal = base.union(test);
		if(log.isInfoEnabled())
		{
			String logStmt = "STUnion is test = " + test.toString() + " Union = " + retVal.toString() + " is union of test and base = " + base.toText();
			log.info(logStmt);			
		}
		
		return retVal;
		
	}
}
