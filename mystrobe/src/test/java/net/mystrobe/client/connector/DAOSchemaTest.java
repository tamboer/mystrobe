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

import java.util.Collection;
import java.util.Map;

import junit.framework.Assert;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.SchemaColumnProperties;
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
public class DAOSchemaTest {

	private static String daoID = "tt-state";
	private static IDSSchema dsSchema;
	private static IDAOSchema daoSchema;

	@BeforeClass
	public static void setUp() throws Exception {
		IConfig config = new Config();
		IAppConnector appConnector = QuarixServerConnector.getAppConnector("wicketds", config);
		dsSchema = appConnector.getSchema("server.state");
		if (dsSchema != null) {
			daoSchema = dsSchema.getDataObjectSchema(daoID);
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		daoSchema = null;
		dsSchema = null;
	}

	@Test
	public void getBatchSize() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		long x = daoSchema.getBatchSize();
		Assert.assertTrue(x > 0);
	}

	@Test
	public void getColumnNames() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		Collection<String> colnames = daoSchema.getColumnNames();
		Assert.assertTrue(colnames.size() == 5);
		Assert.assertTrue(colnames.contains("rowid"));
        Assert.assertTrue(colnames.contains("state"));
	}

	@Test
	public void getColumnProperties() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		Map<SchemaColumnProperties, String> props = daoSchema
				.getColumnProperties("rowid");
		Assert.assertTrue(props.size() == 9);
	}

	@Test
	public void getDAOId() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		Assert.assertEquals(daoID, daoSchema.getDAOId());
	}

	@Test
	public void getMargin() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		int x = daoSchema.getMargin();
		Assert.assertTrue(x == 5);
	}

	@Test
	public void isAutosync() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		Assert.assertTrue(daoSchema.isAutosync());
	}

	@Test
	public void isOpenOnInit() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		Assert.assertTrue(daoSchema.isOpenOnInit());
	}

	@Test
	public void isReadOnly() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		Assert.assertFalse(daoSchema.isReadOnly());
	}

	@Test
	public void isSendChangesOnly() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		Assert.assertTrue(daoSchema.isSendChangesOnly());
	}

	@Test
	public void isSetFilterEveryTime() {
		Assert.assertNotNull(dsSchema);
		Assert.assertNotNull(daoSchema);
		Assert.assertTrue(daoSchema.isSetFilterEveryTime());
	}

}
