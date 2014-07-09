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

import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.MessageType;
import net.mystrobe.client.WicketDSRuntimeException;
import net.mystrobe.client.connector.DAOCommands;
import net.mystrobe.client.connector.DSRequest;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.IConfig;
import net.mystrobe.client.connector.IDAOResponse;
import net.mystrobe.client.connector.QuarixServerConnector;
import net.mystrobe.client.connector.quarixbackend.json.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TVH Group NV
 */
public class DSTransactionManager extends AbstractTransactionManager implements IDSTransactionManager, Serializable {
	
	private static final long serialVersionUID = -1707263438109563380L;
	
	protected static final Logger logger = LoggerFactory.getLogger(DSTransactionManager.class);
	
	@Deprecated
	public DSTransactionManager(IDSSchema dsSchema, IAppConnector appConnector) {
		super(dsSchema, appConnector);
	}
	
	@Deprecated
	public DSTransactionManager(IDSSchema dsSchema, IAppConnector appConnector, IDSTransactionable<? extends IDataBean> ... dsTransactionables ) {
		super(dsSchema, appConnector);
		addTransactionParticipant(dsTransactionables);
	}
	
	public DSTransactionManager(IDSSchema dsSchema, IConfig config, String appName) {
		super(dsSchema, config, appName);
	}
	
	public DSTransactionManager(IDSSchema dsSchema, IConfig config, String appName, IDSTransactionable<? extends IDataBean> ... dsTransactionables ) {
		super(dsSchema, config, appName);
		addTransactionParticipant(dsTransactionables);
	}

	@Override
	public void commit() throws WicketDSBLException {
		
		if (logger.isTraceEnabled()) {
			logger.trace("Start transaction");
		}
		
		if (this.transactionParticipants == null || this.transactionParticipants.isEmpty()) {
			//no data has to be sent to the server
			logger.warn("Commit called for transaction without adding participants");
			return;
		}
		
		boolean commitSuccess = true;
		
		DSRequest dsRequest = new DSRequest();
		dsRequest.setActionCommand(DAOCommands.submitCommit.name());
		
		Message errorMessage = null;
		
		for (IDSTransactionable<? extends IDataBean> transactionable : transactionParticipants) {
			dsRequest.addDAORequest(transactionable.getTransactionCommitData());
		}
		
		this.dsRequest = dsRequest;
		
		IAppConnector appConnector = QuarixServerConnector.getAppConnector(this.appName, this.appServerConfig);
		this.dsResponse = appConnector.dataRequest(this.dsSchema, this.dsRequest);
		
		if (this.dsResponse == null) {
			throw new WicketDSRuntimeException("DS response can not be null");
		}
		
		if (this.dsResponse.hasMessageType(MessageType.Error)) {
			commitSuccess = false;

			for (Message message : this.dsResponse.getDSMessages()) {
				if (MessageType.Error.equals(message.getMessageType())) {
					errorMessage = message;
					break;
				}
			}
		}

		for (IDSTransactionable<? extends IDataBean> transactionable : transactionParticipants) {
			IDAOResponse<? extends IDataBean> daoResponse = this.dsResponse.getDAOResponse(transactionable.getDaoId(), transactionable.getDataBeanClass());
		
			if (daoResponse != null && daoResponse.hasMessageType(MessageType.Error)) {
				commitSuccess = false;
				
				for (Message message : daoResponse.getMessages()) {
					if (MessageType.Error.equals(message.getMessageType()) ) {
						errorMessage = message;
						break;
					}
				}
				break;
			}
		}
		
		if (logger.isTraceEnabled()) {
			logger.trace("Transaction commit success:" + commitSuccess);
		}
		
		for (IDSTransactionable<? extends IDataBean> transactionable : transactionParticipants) {
			IDAOResponse<? extends IDataBean> daoResponse = this.dsResponse.getDAOResponse(transactionable.getDaoId(), transactionable.getDataBeanClass());
			if (commitSuccess) {
				transactionable.succesfullCommitCallback(daoResponse);
				inTransaction = false;
			} else {
				transactionable.unSuccesfullCommitCallback(daoResponse);
			}
		}
		
		if (!commitSuccess) {
			throw new WicketDSBLException(errorMessage);
		} else {
			transactionParticipants.clear();
		}
	}
	
	@Override
	public void rollback() {
		for (IDSTransactionable<? extends IDataBean> transactionable : transactionParticipants) {
			transactionable.rollback();
		}
		transactionParticipants.clear();
		inTransaction = false;
	}

	@Override
	public void startTransaction() {
		for (IDSTransactionable<? extends IDataBean> transactionable : transactionParticipants) {
			transactionable.startTransaction();
		}
		inTransaction = true;
	}

	@Override
	public void addTransactionParticipant(IDSTransactionable<? extends IDataBean>... dsTransactionables) {
		for (IDSTransactionable<? extends IDataBean> transactionable : dsTransactionables) {
			transactionParticipants.add(transactionable);
			transactionable.accept(this);
		}
	}
}
