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
import net.mystrobe.client.connector.quarixbackend.datatypes.State;
import net.mystrobe.client.connector.quarixbackend.datatypes.StateDSSchema;
import net.mystrobe.client.connector.quarixbackend.datatypes.StateDataObject;
import net.mystrobe.client.connector.quarixbackend.datatypes.StateSchema;
import net.mystrobe.client.connector.transaction.DSTransactionManager;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.impl.FilterParameter;
import net.mystrobe.client.navigator.DataObjectIterator;

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
        Config.setValue(IConfig.APP_SERVER_URL, "AppServer://tvh:5162/QRX_DEMO");
//		Config.setValue(IConfig.APP_DATABEAN_PACKAGES, "net.mystrobe.client.connector.quarixbackend.datatypes");
    }
    
    public static String AppName= "qrxexmpl";	
	
    protected static IAppConnector appConnector = null;
   
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
			
        	stateDataObject = new StateDataObject( appConnector);
			stateDataObject.createData(false);

			State state = stateDataObject.getData();
			state.setRegion("East");
			state.setStatecode("SP");
			state.setStatename("Test State");
			stateDataObject.updateData(state);

			stateDataObject.clearFilters();
			stateDataObject.addFilter(new FilterParameter( StateSchema.Cols.STATECODE.name(), FilterOperator.EQ, "SP"));
			
			//stateDataObject.fetchFirst() will not bring data if firts record in buffer
			stateDataObject.resetDataBuffer();
			
			state = stateDataObject.getData();

			assertEquals(stateDataObject.getCursorState(), CursorStates.OnlyRecordAvailable);
			assertNotNull(state);
			assertEquals(state.getStatecode(), "SP");
			assertEquals(state.getRegion(), "East");
			assertEquals(state.getStatename(), "Test State");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    

    @Test
    public void testCrudDataObjectUpdateOne() {
        try {
			stateDataObject = new StateDataObject(appConnector  );
			
			//Set<IFilterParameter> filters = new HashSet<IFilterParameter>();
			stateDataObject.addFilter(new FilterParameter(  StateSchema.Cols.STATECODE.name(), FilterOperator.EQ, "SP"));
			stateDataObject.resetDataBuffer();
			
			State state = stateDataObject.getData();
			state.setStatename("Test State 2");
			stateDataObject.updateData( state );
			
			assertEquals(stateDataObject.getCursorState(), CursorStates.OnlyRecordAvailable);
			assertNotNull(state);
			assertEquals(state.getStatecode(), "SP");
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
			stateDataObject = new StateDataObject(appConnector);
			stateDataObject.resetDataBuffer();
			
			stateDataObject.createData(false);
			State state = stateDataObject.getData();
			
			state.setRegion("East");
			state.setStatecode("SP");
			state.setStatename("Test State 3");
			stateDataObject.updateData(state);
			
			
		} catch (WicketDSBLException e) {
			assertTrue(stateDataObject.hasMessageType(MessageType.Error));
			IDAOMessage message = (IDAOMessage) stateDataObject.getMessages()
					.iterator().next();
			assertNotNull(message);
			assertEquals(message.getColumn(), "State");
			assertEquals(message.getMessageType(), MessageType.Error);
			assertEquals(message.getMessage(), "msg_err_data_dupe");
		}
    }

    
    @Test
    public void testCrudDataObjectDeleteOne() {
        
    	try {
			stateDataObject = new StateDataObject( appConnector);
			stateDataObject.addFilter(new FilterParameter( StateSchema.Cols.STATECODE.name(), FilterOperator.EQ, "SP"));
			stateDataObject.resetDataBuffer();
			
			State state = stateDataObject.getData();
			stateDataObject.deleteData(state);

			stateDataObject.resetDataBuffer();
			assertEquals(stateDataObject.getCursorState(), CursorStates.NoRecordAvailable);
		} catch (WicketDSBLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void testDataTransactions() {
        
    	StateDataObject stateDataObject = new StateDataObject(appConnector );
		
		stateDataObject.addFilter(new FilterParameter( StateSchema.Cols.STATECODE.name(), FilterOperator.EQ, "XX"));
		stateDataObject.resetDataBuffer();
		
		
		DSTransactionManager transactionManager = new DSTransactionManager(new StateDSSchema(), appConnector);
		transactionManager.addTransactionParticipant(stateDataObject);
		
		try {
			
			DataObjectIterator<State> doIterator = new DataObjectIterator<>(stateDataObject); 
			
			while (doIterator.hasNext()) {
				stateDataObject.deleteData(doIterator.next());
			}
   
			transactionManager.commit();
			
			transactionManager.commit();
			
			stateDataObject.createData();
			
			State state = stateDataObject.getData();
			
			state.setRegion("East");
			state.setStatecode("XX");
			state.setStatename("XX State");
			stateDataObject.updateData(state);
			
			assertNotNull(state.getRowId());
			
			
		} catch (WicketDSBLException e) {
			e.printStackTrace();
		}
	}

}