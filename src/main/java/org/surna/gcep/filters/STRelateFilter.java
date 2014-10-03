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
 *         The relate operation uses the DE-9IM pattern matrix to describe the
 *         relationship between two geometries. This is just a more generalized
 *         method to accomplish the operations that are defined under the JTS
 *         operations
 *         contains,crosses,disjoint,equals,intersects,overlaps,touches,union,
 *         and within. The DE-9IM matrix has the form: interior boundary
 *         exterior interior boundary exterior The cells can have the values
 *         T,T,*,1,2 The pattern matrix contains the acceptable values for each
 *         of the intersection matrix cells. The possible pattern values are: T
 *         - An intersection must exist; dim = 0, 1, or 2. F - An intersection
 *         must not exist; dim = -1. * - It does not matter if an intersection
 *         exists or not; dim = -1, 0, 1, or 2. 0 - An intersection must exist
 *         and its maximum dimension must be 0; dim = 0. 1 - An intersection
 *         must exist and its maximum dimension must be 1; dim = 1. 2 - An
 *         intersection must exist and its maximum dimension must be 2; dim = 2.
 *         For example, the pattern matrix of the ST_Within() function for
 *         geometry combinations has the following form: b Interior Boundary
 *         Exterior Interior T * F a Boundary * * F Exterior * * *
 * 
 *         Simply put, the ST_Within() function returns TRUE when the interiors
 *         of both geometries intersect, and the interior and boundary of
 *         geometry a does not intersect the exterior of geometry b. All other
 *         conditions do not matter.
 * 
 *         The matrix for the overlap operation looks like: interior boundary
 *         exterior interior T * T boundary * * * exterior T * *
 * 
 *         This would be passed to the relate function as a row wise string
 *         'T*T***T**'
 */

public class STRelateFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(STRelateFilter.class);

    // setting up for Singleton pattern
    private static final STRelateFilter INSTANCE = new STRelateFilter();

    private STRelateFilter() {
    }

    public static STRelateFilter getInstance() {
        return INSTANCE;
    }

    public boolean STRelate(Geometry base, Geometry test, String DE9IM) {
        boolean retVal = base.relate(test, DE9IM);
        LOGGER.debug("STRelate is test = {}, test spacially overlaps base = {}, using DE9IM = {}, retVal = {}", test,
                base, DE9IM, retVal);

        return retVal;
    }
}
