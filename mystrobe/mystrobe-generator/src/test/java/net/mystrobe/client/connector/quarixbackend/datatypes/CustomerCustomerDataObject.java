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

import java.io.Serializable;

import net.mystrobe.client.DataObjectAdaptor;
import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;

/**
 * 
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 * 
 * Used parameters: appName: wicketds dsName: server.customer doName: tt-customer
 * 
 */

@GeneratorMeta(appName="wicketds", urn="server.customer", dsName="server.customer", dsId="dsCustomer", daoId="tt-customer", isLocked=false)
public class CustomerCustomerDataObject extends DataObjectAdaptor<CustomerCustomer> implements Serializable {

	@Generated
	private static final long serialVersionUID = 1308428577748L;

	public CustomerCustomerDataObject(IAppConnector connector) {
		super(connector, new CustomerDSSchema(), new CustomerCustomerSchema());
	}
}

