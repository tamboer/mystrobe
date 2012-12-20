/**
 * Copyright (C) 2010-2011 TVH Group NV. <kalman.tiboldi@tvh.com>
 *
 * This file is part of the MyStroBe project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package net.mystrobe.client.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.mystrobe.client.CursorStates;
import net.mystrobe.client.FilterOperator;
import net.mystrobe.client.IDAOMessage;
import net.mystrobe.client.MessageType;
import net.mystrobe.client.connector.quarixbackend.datatypes.StateState;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.impl.FilterParameter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * @author TVH Group NV
 */
@RunWith(JUnit4.class)
public class CRUDDataObjectTest {


	public static Config Config = new Config();
    
    static {
        Config.setValue(IConfig.APP_SERVER_URL, "AppServerDC://cargomate.yonder.local:3511/qrx_rcfpg_demo");
		Config.setValue(IConfig.APP_DATABEAN_PACKAGES, "net.mystrobe.client.connector.quarixbackend.datatypes");
    }
    
    public static String AppName= "wicketds";	
	
    protected static IAppConnector appConnector = null;
    final String daoId = "tt-state";
	protected StateDataObject stateDataObject = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        appConnector = (new QuarixServerConnector( Config ).getAppConnector(AppName));
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testCrudDataObjectAddOne() {
        try {
			stateDataObject = new StateDataObject( );
			stateDataObject.setAppConnector( appConnector );
			stateDataObject.resetDataBuffer();
			
			stateDataObject.createData(false);

			StateState state = (StateState) stateDataObject.getData();
			state.setRegion("East");
			state.setState("SP");
			state.setStatename("Test State 2");
			stateDataObject.updateData(state);

			stateDataObject.clearFilters();
			stateDataObject.addFilter(new FilterParameter( "state", FilterOperator.EQ, "SP"));
			stateDataObject.fetchFirst();
			
			state = (StateState) stateDataObject.getData();

			assertEquals(stateDataObject.getCursorState(), CursorStates.OnlyRecordAvailable);
			assertNotNull(state);
			assertEquals(state.getState(), "SP");
			assertEquals(state.getRegion(), "East");
			assertEquals(state.getStatename(), "Test State 2");
		} catch (WicketDSBLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    

    @Test
    public void testCrudDataObjectUpdateOne() {
        try {
			stateDataObject = new StateDataObject( );
			stateDataObject.setAppConnector( appConnector );
			stateDataObject.resetDataBuffer();
			
			//Set<IFilterParameter> filters = new HashSet<IFilterParameter>();
			stateDataObject.addFilter(new FilterParameter( "state", FilterOperator.EQ, "SP"));
			stateDataObject.fetchFirst();
			
			StateState state = (StateState) stateDataObject.getData();
			state.setStatename("Test State 2");
			stateDataObject.updateData( state );
			stateDataObject.fetchFirst();

			state = (StateState) stateDataObject.getData();
			assertEquals(stateDataObject.getCursorState(), CursorStates.OnlyRecordAvailable);
			assertNotNull(state);
			assertEquals(state.getState(), "SP");
			assertEquals(state.getRegion(), "East");
			assertEquals(state.getStatename(), "Test State 2");
		} catch (WicketDSBLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    @Test
    public void testCrudDataObjectAddError() {
        try {
			stateDataObject = new StateDataObject();
			stateDataObject.setAppConnector(appConnector);
			stateDataObject.resetDataBuffer();
			stateDataObject.createData(false);
			StateState state = (StateState) stateDataObject.getData();
			state.setRegion("East");
			state.setState("SP");
			state.setStatename("Test State 2");
			stateDataObject.updateData(state);
			assertTrue(stateDataObject.hasMessageType(MessageType.Error));
			IDAOMessage message = (IDAOMessage) stateDataObject.getMessages()
					.iterator().next();
			assertNotNull(message);
			assertEquals(message.getColumn(), "State");
			assertEquals(message.getMessageType(), MessageType.Error);
			assertEquals(message.getMessage(), "msg_err_data_dupe");
		} catch (Exception e) {
			// TODO: handle exception
		}
    }

    
    @Test
    public void testCrudDataObjectDeleteOne() {
        
    	try {
			stateDataObject = new StateDataObject( );
			stateDataObject.setAppConnector( appConnector );
			stateDataObject.resetDataBuffer();
			
			stateDataObject.addFilter(new FilterParameter( "state", FilterOperator.EQ, "SP"));
			stateDataObject.fetchFirst();
			
			StateState state = (StateState) stateDataObject.getData();
			stateDataObject.deleteData(state);

			stateDataObject.fetchFirst();
			assertEquals(stateDataObject.getCursorState(), CursorStates.NoRecordAvailable);
		} catch (WicketDSBLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}