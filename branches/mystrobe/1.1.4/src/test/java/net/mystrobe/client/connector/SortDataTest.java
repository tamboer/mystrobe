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
import net.mystrobe.client.SortState;
import net.mystrobe.client.SortState.Sort;
import net.mystrobe.client.connector.quarixbackend.datatypes.StateState;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author TVH Group NV
 */
@RunWith(JUnit4.class)
public class SortDataTest {

	protected static IDSResponse dsResponse;
	protected static IDAOResponse daoResponse;
	protected static List<IDAORow> daoRows;

	@BeforeClass
	public static void setUp() throws Exception {
		Config config = new Config();
		config.setValue(IConfig.APP_SERVER_URL, "AppServerDC://cargomate.yonder.local:3511/qrx_rcfpg_demo");
		config.setValue(IConfig.APP_DATABEAN_PACKAGES, "net.mystrobe.client.connector.quarixbackend.datatypes");
		IAppConnector appConnector = QuarixServerConnector.getAppConnector("wicketds", config);

		final String daoId = "tt-state";

		IDSRequest dsReq = new DSRequest();

		IDAORequest daoRequest = DAORequest.FetchFirst("tt-state");
		SortState ss = new SortState();
		ss.setSortColumn("state");
		ss.setSortOrder(Sort.Descending);
		daoRequest.setSortState(ss);
		dsReq.addDAORequest(daoRequest);

		// IDSSchema dsSchema = appConnector.getSchema("server.state");
		dsResponse = appConnector.dataRequest("server.state", dsReq);
		if (dsResponse != null) {
			daoResponse = dsResponse.getDAOResponse(daoId, StateState.class);
			daoRows = new ArrayList<IDAORow>(daoResponse.getDAORows());
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		daoResponse = null;
	}


	@Test
	public void sortDescending() {
		Assert.assertNotNull(daoResponse);
		Assert.assertNotNull(daoRows);
		Assert.assertTrue(daoRows.size() > 0);
		Assert.assertEquals("WY", ((StateState) daoRows.get(0)
				.getRowData()).getState());
	}

}
