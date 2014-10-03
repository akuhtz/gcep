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
 *         Two geometries base and test are said to be Disjoint if no points of
 *         test are contained in base This filter returns true if test is
 *         disjoint base
 */

public class STDisjointFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(STDisjointFilter.class);

    // setting up for Singleton pattern
    private static final STDisjointFilter INSTANCE = new STDisjointFilter();

    private STDisjointFilter() {

    }

    public static STDisjointFilter getInstance() {
        return INSTANCE;
    }

    public boolean STDisjoint(Geometry base, Geometry test) {
        boolean retVal = base.disjoint(test);
        LOGGER.debug("STDisjoint is test = {}, no part of test is within base = {}, retVal = {}", test, base, retVal);

        return retVal;

    }
}
