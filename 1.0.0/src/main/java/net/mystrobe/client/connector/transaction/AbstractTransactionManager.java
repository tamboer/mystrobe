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
 package net.mystrobe.client.connector.transaction;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.IDSRequest;
import net.mystrobe.client.connector.IDSResponse;

/**
 * @author TVH Group NV
 */
public abstract class AbstractTransactionManager implements Serializable {
	
	private static final long serialVersionUID = -5536189171929492348L;

	protected IDSRequest dsRequest;
	
	protected IDSResponse dsResponse;
	
	protected boolean inTransaction = false;
	
	/**
	 * Set of data objects that will participate in the transaction. 
	 */
	protected Set<IDSTransactionable<? extends IDataBean>> transactionParticipants = new HashSet<IDSTransactionable<? extends IDataBean>>();
	
	/**
	 * Data set upon which transaction will be executed
	 */
	protected IDSSchema dsSchema;
	
	/**
	 * App connector 
	 */
	protected IAppConnector appConnector;

	
	public AbstractTransactionManager(IDSSchema dsSchema, IAppConnector appConnector) {
		this.dsSchema = dsSchema;
		this.appConnector = appConnector;
	}
}
