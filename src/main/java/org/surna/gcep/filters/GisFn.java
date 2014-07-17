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

public class GisFn
{
	//	org.apache.commons.logging.log used for logging
    private static final Log log = LogFactory.getLog(GisFn.class);

    public static boolean STContains (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STContains() entered.");			
		}
		return STContainsFilter.getInstance().STContains (base, test);
	}

    public static boolean STIntersects (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STIntersects() entered.");
		}	
	return STIntersectsFilter.getInstance().STIntersects (base, test);
	}

    public static boolean STDisjoint (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STDisjoint() entered.");
		}
	return STDisjointFilter.getInstance().STDisjoint (base, test);
	}
	public static boolean STWithin (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STWithin() entered.");
		}
	return STWithinFilter.getInstance().STWithin (base, test);
	}
	public static boolean STEquals (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STEquals() entered.");
		}
	return STEqualsFilter.getInstance().STEquals (base, test);
	}
	public static boolean STTouches (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STTouches() entered.");
		}
	return STTouchesFilter.getInstance().STTouches (base, test);
	}
	public static boolean STCrosses (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STCrosses() entered.");
		}
	return STCrossesFilter.getInstance().STCrosses (base, test);
	}
	public static boolean STOverlaps (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STOverlaps() entered.");
		}
	return STOverlapsFilter.getInstance().STOverlaps (base, test);
	}
	public static Geometry STIntersection (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STIntersection() entered.");
		}
	return STIntersectionFilter.getInstance().STIntersection (base, test);
	}
	public static Geometry STUnion (Geometry base, Geometry test)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STUnion() entered.");
		}
	return STUnionFilter.getInstance().STUnion (base, test);
	}
	public static Geometry STConvexHull (Geometry base)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STConvexHull() entered.");
		}
	return STConvexHullFilter.getInstance().STConvexHull (base);
	}
	public static boolean STRelate (Geometry base, Geometry test, String DE9IM)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STRelate() entered.");
		}
	return STRelateFilter.getInstance().STRelate (base, test, DE9IM);
	}
	public static Geometry STBuffer (Geometry base, double dist, int degapprox, int endcap)
	{
		if(log.isInfoEnabled())
		{
			log.info("FisFn.STConvexHull() entered.");
		}
		return STBufferFilter.getInstance().STBuffer (base, 100.0, 0, 0);
	}
}
