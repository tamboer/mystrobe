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
import net.mystrobe.client.CursorStates;
import net.mystrobe.client.connector.quarixbackend.datatypes.StateState;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author TVH Group NV
 */
public class DataObjectTest {
    
    protected static IAppConnector appConnector = null;
    protected StateDataObject stateDo = null;
    

    public DataObjectTest() {
        
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
		Config config = new Config();
		config.setValue(IConfig.APP_SERVER_URL, "AppServerDC://cargomate.yonder.local:3511/qrx_rcfpg_demo");
		config.setValue(IConfig.APP_DATABEAN_PACKAGES, "net.mystrobe.client.connector.quarixbackend.datatypes");
		IServerConnector srvConnector = new QuarixServerConnector(config);
		appConnector = srvConnector.getAppConnector("wicketds");
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        stateDo = new StateDataObject();
        stateDo.setAppConnector(appConnector);
       
    }

    @After
    public void tearDown() {
        
    }

    @Test
    public void DataObject_FetchFirst () {
    	stateDo.fetchFirst();
			
		assertEquals(CursorStates.FirstRecord, stateDo.getCursorState());
		assertNotNull(stateDo.getData());
		assertEquals("Alaska", ((StateState)stateDo.getData()).getStatename());
		assertEquals("AK", ((StateState)stateDo.getData()).getState());
	}

    @Test
    public void DataObject_FetchLast () {
    	
		stateDo.fetchLast();
		assertEquals(CursorStates.LastRecord, stateDo.getCursorState());
		assertNotNull(stateDo.getData());
		assertEquals("Wyoming", ((StateState) stateDo.getData())
				.getStatename());
		assertEquals("WY", ((StateState) stateDo.getData()).getState());
	}

    @Test
    public void DataObject_FetchNext () {
    	
		stateDo.fetchFirst();

		assertEquals(CursorStates.FirstRecord, stateDo.getCursorState());

		StateState  state = (StateState)stateDo.getData();

		assertNotNull(state);
		assertEquals("Alaska", state.getStatename());
		assertEquals("AK", state.getState());

		boolean hasMore = true;
		int i =0;
		System.out.println (" " + i  + ". " + state.getStatename() + " / " + state.getState());
		for( i = 1;  hasMore; i++ ) {
		    hasMore = stateDo.fetchNext();
		    state = (StateState)stateDo.getData();
		    if( hasMore && state != null ) {
		        System.out.println (" " + i  + ". " + state.getStatename() + " / " + state.getState());
		    }
		}

		assertEquals("Wyoming", state.getStatename());
	}

    
    @Test
    public void DataObject_FetchPrevious () {
    	
		stateDo.fetchLast();

		assertEquals(CursorStates.LastRecord, stateDo.getCursorState());

		StateState  state = (StateState)stateDo.getData();

		assertNotNull(state);
		assertEquals("Wyoming", state.getStatename());
		assertEquals("WY", state.getState());

		boolean hasMore = true;
		int i =0;
		System.out.println (" " + i  + ". " + state.getStatename() + " / " + state.getState());
		for( i = 1;  hasMore; i++ ) {
		    hasMore = stateDo.fetchPrev();
		    state = (StateState)stateDo.getData();
		    if( hasMore && state != null ) {
		        System.out.println (" " + i  + ". " + state.getStatename() + " / " + state.getState());
		    }
		}

		assertEquals("Alaska", state.getStatename());
	}

    /*
    @Test
    public void DataObject_SetFilters () {

        FilterParameter fp = new FilterParameter() {

            public String getColumn() {
                return "state";
            }

            public FilterOperator getOperator() {
                return FilterOperator.EQ;
            }

            public Object getValue() {
                return "KS";
            }

            public void setColumn(String column) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setOperator(FilterOperator filterOperator) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setValue(Object filterValue) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        HashSet<FilterParameter> filters = new HashSet<FilterParameter>();
        filters.add(fp);
        stateDo.setFilters(filters);
        stateDo.fetchFirst();

        assertEquals(CursorStates.OnlyRecordAvailable, stateDo.getCursorState());

        StateState  state = (StateState)stateDo.getData();

        assertNotNull(state);
        assertEquals("Kansas", state.getStatename());
        assertEquals("KS", state.getState());
    }
    */

}