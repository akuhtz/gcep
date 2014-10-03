/**************************************************************************************
 * Copyright (C) 2009 - 2010 Surna, Inc. All rights reserved.                                *
 * http://www.surna.org                                                               *
 * http://www.surna.com                                                               *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/

package org.surna.gcep.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author rpbrandt
 * 
 *         Two geometries base and test are said to intersect if some part of
 *         test is contained in base This filter returns true if test intersects
 *         base
 */

public class STIntersectsFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(STIntersectsFilter.class);

    // setting up for Singleton pattern
    private static final STIntersectsFilter INSTANCE = new STIntersectsFilter();

    private STIntersectsFilter() {

    }

    public static STIntersectsFilter getInstance() {
        return INSTANCE;
    }

    public boolean STIntersects(Geometry base, Geometry test) {
        boolean retVal = base.intersects(test);
        LOGGER.debug("STIntersects is test = {}, some part of test is within base = {}, retVal = {}", test, base,
                retVal);

        return retVal;

    }
}
