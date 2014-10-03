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

public class STContainsFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(STContainsFilter.class);

    // setting up for Singleton pattern
    private static final STContainsFilter INSTANCE = new STContainsFilter();

    private STContainsFilter() {

    }

    public static STContainsFilter getInstance() {
        return INSTANCE;
    }

    public boolean STContains(Geometry base, Geometry test) {
        LOGGER.debug("STContains is test = {} contained within base = {}", test, base);

        boolean retVal = base.contains(test);

        return retVal;

    }
}
