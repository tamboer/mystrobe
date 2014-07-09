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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.mystrobe.client.IDAOMessage;
import net.mystrobe.client.connector.quarixbackend.datatypes.StateState;
import net.mystrobe.client.impl.DAORow;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author TVH Group NV
 */
@RunWith(JUnit4.class)
public class DAORequestCrudTest {

	protected static List<IDAORow<StateState>> daoRows;
	protected static IAppConnector appConnector;
	protected final String daoId = "tt-state";
	

	public static Config config = new Config();
    
    static {
        config.setValue(IConfig.APP_SERVER_URL, "AppServerDC://cargomate.yonder.local:3511/qrx_rcfpg_demo");
		config.setValue(IConfig.APP_DATABEAN_PACKAGES, "net.mystrobe.client.connector.quarixbackend.datatypes");
    }
    
    public static String AppName= "wicketds";	

    protected static StateState state = new StateState();

	@BeforeClass
	public static void setUp() throws Exception {
		appConnector = QuarixServerConnector.getAppConnector("wicketds", config);				
		IDAORequest daoRequest = DAORequest.FetchFirst("tt-state");
		appConnector.dataRequest("server.state", new DSRequest(daoRequest));
	}

	@Test
	public void addOne() {
                
        DAORow<StateState> row = new DAORow<StateState>();
        
        state.setRowid("appending0");
        state.setRegion("East");
        state.setState("TT");
        state.setStatename("Test Test 2");
        state.setRowstate(RowState.New.ordinal());
        row.setRowState(RowState.New);
        row.setRowData(state);
        row.setBeforeImage(new StateState());
        
        IDAORequest daoRequest = DAORequest.SubmitCommit(daoId);
        daoRequest.getRows().add(row);
        IDSRequest dsReq = new DSRequest(daoRequest);
        
        IDSResponse dsResponse = appConnector.dataRequest("server.state", dsReq);
        IDAOResponse<StateState> daoResponse = null;
        
		if (dsResponse != null) {
			
			if (dsResponse.getDSMessages() != null && dsResponse.getDSMessages().size() > 0 ) {
				for (IDAOMessage message : dsResponse.getDSMessages()) {
					Assert.fail(message.getMessage());
				}
			}
			
			daoResponse = dsResponse.getDAOResponse(daoId, StateState.class);
			Assert.assertNotNull(daoResponse);
			Assert.assertEquals("Response should hold only newly created row", 1, daoResponse.getDAORows().size());
			state = (StateState) daoResponse.getDAORows().iterator().next().getRowData();
		} else {
			Assert.fail("Can not create new state data");
		}
	}

	@Test
	public void updateOne() {

        state.setRowstate(RowState.Unmodified.ordinal());
        
        StateState changedState = new StateState();
        changedState.setRowid( state.getRowId());
        changedState.setRowstate( RowState.Updated.ordinal());
        changedState.setRegion( state.getRegion() );
        changedState.setState( state.getState() );
        changedState.setStatename( state.getStatename() + " Modified");

        DAORow<StateState> row = new DAORow<StateState>();
        row.setBeforeImage(state);
        row.setRowData(changedState);
        row.setRowState(RowState.Updated);

        IDSRequest dsReq = new DSRequest();

        IDAORequest daoRequest = DAORequest.SubmitCommit(daoId);
        daoRequest.setSkipRow(false);
        daoRequest.setStartRowId( state.getRowId() );
		daoRequest.getRows().add(row);

        dsReq.addDAORequest(daoRequest);


        //IDSSchema dsSchema = appConnector.getSchema("server.state");
		IDSResponse dsResponse = appConnector.dataRequest("server.state", dsReq);
        IDAOResponse<StateState> daoResponse = null;

		if (dsResponse != null) {
			daoResponse = dsResponse.getDAOResponse(daoId, StateState.class);
			daoRows = new ArrayList<IDAORow<StateState>>(daoResponse.getDAORows());
		}

		Assert.assertNotNull(daoResponse);
		Assert.assertNotNull(daoRows);
		Assert.assertTrue(daoRows.size() > 0);

        state = (StateState) daoRows.get(0).getRowData();
	}


	@Test
	public void deleteOne() {

        state.setRowstate(RowState.Deleted.ordinal());
        DAORow<StateState> row = new DAORow<StateState>();
        row.setBeforeImage(state);
        row.setRowState(RowState.Deleted);

        IDSRequest dsReq = new DSRequest();

        IDAORequest daoRequest = DAORequest.SubmitCommit(daoId);
        daoRequest.setSkipRow(false);
        daoRequest.setStartRowId("last");
        daoRequest.setBatchSize(-10);
		daoRequest.getRows().add(row);

        dsReq.addDAORequest(daoRequest);

		IDSResponse dsResponse = appConnector.dataRequest("server.state", dsReq);
        IDAOResponse<StateState> daoResponse = null;

		if (dsResponse != null) {
			daoResponse = dsResponse.getDAOResponse(daoId, StateState.class);
			daoRows = new ArrayList<IDAORow<StateState>>(daoResponse.getDAORows());
		}

		Assert.assertNotNull(daoResponse);
		Assert.assertNotNull(daoRows);	
	}
    
}
