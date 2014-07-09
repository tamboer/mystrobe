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
import net.mystrobe.client.DataSourceAdaptor.AppendPosition;
import net.mystrobe.client.connector.DAOCommands;
import net.mystrobe.client.connector.DSRequest;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.IConfig;
import net.mystrobe.client.connector.IDAORequest;
import net.mystrobe.client.connector.IDAOResponse;
import net.mystrobe.client.connector.QuarixServerConnector;
import net.mystrobe.client.connector.quarixbackend.json.Message;


/**
 * {@link IDSRequestTransactionManager} implementation.
 * 
 * @author TVH Group NV
 */
public class DSRequestTransactionManager extends AbstractTransactionManager implements IDSRequestTransactionManager, Serializable {
	
	private static final long serialVersionUID = 7001671415137585495L;

	@Deprecated
	public DSRequestTransactionManager(IDSSchema dsSchema, IAppConnector appConnector) {
		super(dsSchema, appConnector);
	}
	
	@Deprecated
	public DSRequestTransactionManager(IDSSchema dsSchema, IAppConnector appConnector, IDSTransactionable<? extends IDataBean> ... dsTransactionables) {
		super(dsSchema, appConnector);
		addRequestTransactionParticipant(dsTransactionables);
	}
	
	public DSRequestTransactionManager(IDSSchema dsSchema, IConfig config, String appName) {
		super(dsSchema, config, appName);
	}
	
	public DSRequestTransactionManager(IDSSchema dsSchema, IConfig config, String appName, IDSTransactionable<? extends IDataBean> ... dsTransactionables) {
		super(dsSchema, config, appName);
		addRequestTransactionParticipant(dsTransactionables);
	}
	
	@Override
	public void dataRequest() {
		dataRequest(DAOCommands.sendRows.name(), AppendPosition.REPLACE, 1);
	}
	
	@Override
	public void dataRequest(String commandName) {
		dataRequest(commandName, AppendPosition.REPLACE, 1);
	}
	
	@Override
	public void dataRequest(AppendPosition appendPosition, int positionInBuffer) {
		dataRequest(DAOCommands.sendRows.name(), appendPosition, positionInBuffer);
	}
	
	@Override
	public void dataRequest(AppendPosition appendPosition) {
		dataRequest(DAOCommands.sendRows.name(), appendPosition, -1);
		
	}
	
	@Override
	public void dataRequest(String commandName, AppendPosition appendPosition, int positionInBuffer) {
		this.dsRequest = new DSRequest();
		
		this.dsRequest.setActionCommand(commandName);
		
		for (IDSTransactionable<? extends IDataBean> transactionable : transactionParticipants) {
			IDAORequest<? extends IDataBean> daoRequest = transactionable.getDataRequest();
			dsRequest.addDAORequest(daoRequest);
		}
		
		IAppConnector appConnector = QuarixServerConnector.getAppConnector(this.appName, this.appServerConfig);
		this.dsResponse = appConnector.dataRequest(this.dsSchema, this.dsRequest);
		
		if (this.dsResponse == null) {
			throw new WicketDSRuntimeException("DS response can not be null");
		}
		
		//check for errors
		Message errorMessage = null;
		boolean hasErrors = false;
		for (IDSTransactionable<? extends IDataBean> transactionable : transactionParticipants) {
			IDAOResponse<? extends IDataBean> daoResponse = this.dsResponse.getDAOResponse(transactionable.getDaoId(), transactionable.getDataBeanClass());
		
			if (daoResponse != null && daoResponse.hasMessageType(MessageType.Error)) {
				for (Message message : daoResponse.getMessages()) {
					if (MessageType.Error.equals(message.getMessageType()) ) {
						errorMessage = message;
						break;
					}
				}
				hasErrors = true;
				break;
			}
		}
		
		if (!hasErrors) {
			if (this.dsResponse.hasMessageType(MessageType.Error)) {
				hasErrors = true;
				for (Message message : this.dsResponse.getDSMessages()) {
					if (MessageType.Error.equals(message.getMessageType()) ) {
						errorMessage = message;
						break;
					}
				}
			}
		}
		
		//process main data object last
		IDSTransactionable<? extends IDataBean> mainRequestObject = null;
		IDAOResponse<? extends IDataBean> mainRequestObjectResponse = null; 
		for (IDSTransactionable<? extends IDataBean> transactionable : transactionParticipants) {
			IDAOResponse<? extends IDataBean> daoResponse = this.dsResponse.getDAOResponse(transactionable.getDaoId(), transactionable.getDataBeanClass());
			if (transactionable.getDataRequestMainObject()) {
				mainRequestObject = transactionable;
				mainRequestObjectResponse = daoResponse;
			} else {
				transactionable.processDataResponse(daoResponse, appendPosition, -1);
			}
		}
		
		//main data object should be processed last
		if (mainRequestObject != null) {
			mainRequestObject.processDataResponse(mainRequestObjectResponse, appendPosition, positionInBuffer);
		}
		
		if (hasErrors) {
			throw new WicketDSRuntimeException(errorMessage);
		}
		
		afterDataRequest(appendPosition);
	}
	
	/**
	 * Method to be overridden in extending classes to add 
	 *  particular actions to handle data after fetch.
	 */
	public void afterDataRequest(AppendPosition appendPosition){}; 

	@Override
	public void addRequestTransactionParticipant(IDSTransactionable<? extends IDataBean> ... dsTransactionables) {
		boolean first = true; 
		for (IDSTransactionable<? extends IDataBean> transactionable : dsTransactionables) {
			transactionParticipants.add(transactionable);
			transactionable.acceptRequestTransaction(this, first);
			if (first) {
				first = false;
			}
		}
	}
	
	@Override
	public void addRequestTransactionParticipant(IDSTransactionable<? extends IDataBean>  transactionable, boolean dataRequestMainObject) {
		transactionParticipants.add(transactionable);
		transactionable.acceptRequestTransaction(this, dataRequestMainObject);
	}
}
