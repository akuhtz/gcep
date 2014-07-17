/**************************************************************************************
 * Copyright (C) 2009 - 2010 Surna, Inc. All rights reserved.                                *
 * http://www.surna.org                                                               *
 * http://www.surna.com                                                               *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/

package org.surna.gcep.filters.regression;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;

/**
 * @author rpbrandt
 *
 */
public class STFenceEPLTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(STFenceEPLTest.class);
	static EPRuntime cepRT = null;
	static GeometryFactory geometryFactory = null;
	static Geometry theFence = null;
	static Coordinate coordinate = null;
	static Point[] points = null;   
	public static final int IN = 1;
	public static final int OUT = 0;
	public static int loc = OUT;

	public static class FenceEvent {  

		Geometry fence;  
		Geometry curLoc;  
		int inOut;

		public FenceEvent(Geometry fc, Geometry cl, int io) {  

			fence = fc;  
			curLoc = cl;  
			inOut = io;  
		}  

		public Geometry getFence() {
			return fence;
		}

		public Geometry getCurLoc() {
			return curLoc;
		}  

		public int getInOut() { 
			return inOut;
		}
	}

	public static class CEPListenerIn implements UpdateListener {  

		public void update(EventBean[] newData, EventBean[] oldData) {
			if(LOGGER.isInfoEnabled())
			{
				LOGGER.info("++ Enter fence event received: " + newData[0].getUnderlying()); 			
			}
			Assert.assertEquals(loc,OUT,"entered fence");
			loc = IN;
		}

	} 

	public static class CEPListenerOut implements UpdateListener {  

		public void update(EventBean[] newData, EventBean[] oldData) {
			if(LOGGER.isInfoEnabled())
			{
				LOGGER.info("++ Leaver fence event received: " + newData[0].getUnderlying()); 			
			}
			Assert.assertEquals(loc,IN, "left fence");
			loc = OUT;
		}  
	}  

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		geometryFactory = new GeometryFactory();

		/**
		 * The fence defines the boundaries of Welles park
		 * So, when the sensor leaves the park we get one and only one alert
		 * We get one and only one alert when the sensor re enters the park.
		 */ 
		Coordinate[]coordinates;
		coordinates = new Coordinate[5];
		coordinates[0] = new Coordinate(41.96312803274265, -87.68870294094085);  // NW corner
		coordinates[1] = new Coordinate(41.9631719100627, -87.6849639415741);    // NE corner
		coordinates[2] = new Coordinate(41.96138089308095, -87.68381059169769);  // SE corner
		coordinates[3] = new Coordinate(41.961309091796046, -87.68867075443268); // SW corner
		coordinates[4] = new Coordinate(41.96312803274265, -87.68870294094085);  // back to NW corner
		LinearRing shell = geometryFactory.createLinearRing(coordinates);
		theFence = geometryFactory.createPolygon(shell, null);

		/**
		 * We will send ESPER 8 observations.  The sensor will start out set for out of the fence
		 * 1 will be out - no alert
		 * 2 will be out - no alert
		 * 3 will be in  - alert sent to IN listener
		 * 4 will be in  - no alert
		 * 5 will be in  - no alert
		 * 6 will be out - alert sent to OUT listener
		 * 7 will be out - no alert
		 * 8 will be out - no alert
		 */

		points = new Point[8];
		coordinate = new Coordinate(41.96228239121736, -87.68223345279693);     // head toward east side
		points[0] = geometryFactory.createPoint(coordinate);
		coordinate = new Coordinate(41.962294357921024, -87.68383741378784);    // closer to east side
		points[1] = geometryFactory.createPoint(coordinate);  
		coordinate = new Coordinate(41.96228638011882, -87.68529653549194);     // enter east side
		points[2] = geometryFactory.createPoint(coordinate);  
		coordinate = new Coordinate(41.962290369020046, -87.68709361553192);    // go toward west side
		points[3] = geometryFactory.createPoint(coordinate);  
		coordinate = new Coordinate(41.96219862422892, -87.68834888935089);     // go towards west side
		points[4] = geometryFactory.createPoint(coordinate);  
		coordinate = new Coordinate(41.961305, -87.68961);                      // exit west side
		points[5] = geometryFactory.createPoint(coordinate);  
		coordinate = new Coordinate(41.961317, -87.690146);                     // head further west
		points[6] = geometryFactory.createPoint(coordinate);
		coordinate = new Coordinate(41.961305, -87.691348);                     // further west
		points[7] = geometryFactory.createPoint(coordinate);  

		Configuration cepConfig = new Configuration(); 
		cepConfig.addImport("org.surna.gcep.filters.*");	// package import
		cepConfig.addEventType("CheckFence", FenceEvent.class.getName());  
		EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);  
		cepRT = cep.getEPRuntime();  
		EPAdministrator cepAdm = cep.getEPAdministrator();  
		//             EPStatement cepStatementIn = cepAdm.createEPL("select * from " +  
		//                     "CheckFence((inOut = 0) and (GisFn.STWithin(curLoc, fence) = true))");  
		EPStatement cepStatementIn = cepAdm.createEPL("select * from CheckFence() " +  
				"WHERE ((inOut = 0) and (GisFn.STWithin(curLoc, fence) = true))");  
		cepStatementIn.addListener(new CEPListenerIn()); 
		EPStatement cepStatementOut = cepAdm.createEPL("select * from CheckFence() " +  
				"WHERE ((inOut = 1) and (GisFn.STWithin(curLoc, fence) = false))");  
		cepStatementOut.addListener(new CEPListenerOut()); 
	}

	/**
	 * Test method for gcep epl integration.  Several events are sent to the ESPER engine.  Events that result in movement in or out of
	 * a fence will generate events that are sent to the listener.
	 */
	@Test
	public void testSTFence()
	{
		for (int i = 0; i < 8; i++) {
			Geometry curPoint = points[i];  
			FenceEvent fenceEvent = new FenceEvent(theFence, curPoint, loc);
			if(LOGGER.isInfoEnabled())
			{
				LOGGER.info("Sending event:" + curPoint.toString()); 			
			}
			if (i < 3){
				Assert.assertEquals(loc,OUT, "out of fence");
			}
			if ((i > 2)&&(i < 5)){
				Assert.assertEquals(loc,IN, "in the fence");
			}
			if (i > 5){
				Assert.assertEquals(loc,OUT, "out of fence");
			}
			cepRT.sendEvent(fenceEvent);  
		}	
	}
}

