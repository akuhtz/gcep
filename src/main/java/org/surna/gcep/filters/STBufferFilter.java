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
import com.vividsolutions.jts.operation.buffer.BufferOp;

/**
 * @author rpbrandt
 * 
 *         The buffer of a Geometry at a distance d is the Polygon or
 *         MultiPolygon which contains all points within a distance d of the
 *         Geometry. The distance d is interpreted according to the Precision
 *         Model of the Geometry. Both positive and negative distances are
 *         supported.
 * 
 *         In mathematical terms, buffering is defined as taking the Minkowski
 *         sum or difference of the Geometry with a disc of radius equal to the
 *         absolute value of the buffer distance. Positive and negative
 *         buffering is also referred to as dilation or erosion. In CAD/CAM
 *         terms, buffering is referred to as computing an offset curve.
 * 
 *         Buffering allows three different types of end caps to be specified.
 *         These are: CAP_ROUND The usual round end caps - default CAP_BUTT End
 *         caps are truncated flat at the line ends CAP_SQUARE End caps are
 *         squared off at the buffer distance beyond the line ends
 * 
 *         Finally, since the exact buffer outline of a Geometry usually
 *         contains circular sections, the buffer must be approximated by the
 *         linear Geometry supported by JTS. The degree of approximation may be
 *         specifed as a buffer argument. The default is 8.
 */

public class STBufferFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(STBufferFilter.class);

    // setting up for Singleton pattern
    private static final STBufferFilter INSTANCE = new STBufferFilter();

    private STBufferFilter() {

    }

    public static STBufferFilter getInstance() {
        return INSTANCE;
    }

    public Geometry STBuffer(Geometry base, double dist, int degapprox, int endcap) {
        BufferOp bufOp = new BufferOp(base);
        if (endcap > 0) {
            bufOp.setEndCapStyle(endcap);
        }
        if (degapprox > 0) {
            bufOp.setQuadrantSegments(degapprox);
        }
        Geometry retVal = bufOp.getResultGeometry(dist);
        LOGGER.debug("STBuffer is base = {}, new geom = {}, deg approx = {}, endcap style = {}", base, retVal,
                degapprox, endcap);

        return retVal;

    }
}
