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
 *         Given two geometries base and test. Test is said to be within base if
 *         its dimensions are spacially within base This relationship is valid
 *         for A/A, A/L, A/P, L/L, L/P, and P/P This filter returns true if test
 *         is within base
 */

public class STWithinFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(STWithinFilter.class);

    // setting up for Singleton pattern
    private static final STWithinFilter INSTANCE = new STWithinFilter();

    private STWithinFilter() {

    }

    public static STWithinFilter getInstance() {
        return INSTANCE;
    }

    public boolean STWithin(Geometry base, Geometry test) {
        boolean retVal = base.within(test);
        LOGGER.debug("STWithin is test = {}, base is  within test = {}, retVal = {}", base, test, retVal);

        return retVal;
    }
}
