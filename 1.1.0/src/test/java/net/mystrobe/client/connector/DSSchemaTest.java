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

import java.util.Enumeration;

import junit.framework.Assert;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDSRelation;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.connector.Config;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.IConfig;
import net.mystrobe.client.connector.IServerConnector;
import net.mystrobe.client.connector.QuarixServerConnector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author TVH Group NV
 */
@RunWith(JUnit4.class)
public class DSSchemaTest {

	private static String daoID = "tt-state";
	private static IDSSchema dsSchema;

	@BeforeClass
	public static void setUp() throws Exception {
		IConfig config = new Config();
		IServerConnector srvConnector = new QuarixServerConnector(config);
		IAppConnector appConnector = srvConnector.getAppConnector("wicketds");
		dsSchema = appConnector.getSchema("server.state");
	}

	@AfterClass
	public static void tearDown() throws Exception {
		dsSchema = null;
	}

	@Test
	public void getId() {
		Assert.assertNotNull(dsSchema);
		Assert.assertEquals("dsState", dsSchema.getId());
	}

	@Test
	public void getBatchSize() {
		Assert.assertNotNull(dsSchema);
		long x = dsSchema.getBatchSize();
		Assert.assertTrue(x > 0);
	}
	
	@Test
	public void getDataObjectIds() {
		Assert.assertNotNull(dsSchema);
		Enumeration<String> ids = dsSchema.getDataObjectIds();
		Assert.assertNotNull(ids);
		Assert.assertTrue(ids.hasMoreElements());
		Assert.assertEquals(daoID, ids.nextElement());		
	}

	@Test
	public void getDataObjectSchema() {
		Assert.assertNotNull(dsSchema);
		IDAOSchema daoSchema = dsSchema.getDataObjectSchema(daoID);
		Assert.assertNotNull(daoSchema);
	}

	@Test
	public void getMargin() {
		Assert.assertNotNull(dsSchema);
		int x = dsSchema.getMargin();
		Assert.assertTrue(x == 5);
	}

	@Test
	public void getRelations() {
		Assert.assertNotNull(dsSchema);
		Enumeration<IDSRelation> rels = dsSchema.getRelations();
		Assert.assertNotNull(rels);
		Assert.assertFalse(rels.hasMoreElements());
	}

	@Test
	public void getURN() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(dsSchema.getURN());
	}

	@Test
	public void hasAtomicChanges() {
		Assert.assertNotNull(dsSchema);
		Assert.assertFalse(dsSchema.hasAtomicChanges());
	}

	@Test
	public void hasFilteredChildren() {
		Assert.assertNotNull(dsSchema);
		Assert.assertTrue(dsSchema.hasFilteredChildren());
	}
	
}
