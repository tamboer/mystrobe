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
 package net.mystrobe.client.connector.quarixbackend.datatypes;

import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.impl.DSSchema;

/**
 * 
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 * 
 * Used parameters: appName: wicketds dsName: server.salesrep doName: tt-salesrep
 * 
 */

@GeneratorMeta(appName="wicketds", urn="server.salesrep", dsName="server.salesrep", dsId="dsSalesRep", isLocked=false)
public class SalesRepDSSchema extends DSSchema {

	@Generated
	public SalesRepDSSchema() {
		id = "dsSalesRep";
		bathchSize = 50;
		margin = 5;
		urn = "server.salesrep";
		hasFilteredChildren = true;

		loadRelations(new String[] {});

		daoMap.put("tt-salesrep", new SalesRepSalesrepSchema(this));
	}
	
	
}

