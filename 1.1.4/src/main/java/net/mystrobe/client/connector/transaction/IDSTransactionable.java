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

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.DataSourceAdaptor.AppendPosition;
import net.mystrobe.client.connector.IDAORequest;
import net.mystrobe.client.connector.IDAOResponse;

/**
 * @author TVH Group NV
 */
public interface IDSTransactionable<T extends IDataBean> {
	
	/**
	 * Get all data changes that have to be sent to the back end
	 *  in form of a IDAORequest.
	 * 
	 * @return
	 */
	public IDAORequest<T> getTransactionCommitData();

	
	/**
	 * Get data request settings of current transactionable object.
	 * 
	 * @return
	 */
	public IDAORequest<T> getDataRequest();
	
	/**
	 * Rollback all data changes. 
	 * 
	 */
	public void rollback();
	
	/**
	 * Start a new transaction. 
	 */
	public void startTransaction();
	
	/**
	 * Update data with information received from back end
	 *  when transaction is successful . 
	 * 
	 * @param daoResponse
	 */
	public void succesfullCommitCallback(IDAOResponse daoResponse);
	
	/**
	 * Update data with information received from back end
	 *  when transaction is successful . 
	 * 
	 * @param daoResponse
	 */
	public void unSuccesfullCommitCallback(IDAOResponse daoResponse);
	
	/**
	 * Accept transaction manager
	 * 
	 */
	public void accept(IDSTransactionManager dsTransactionManager);
	
	/**
	 * Accept transaction manager
	 * 
	 */
	public void acceptRequestTransaction(IDSRequestTransactionManager dsRequestTransactionManager, boolean mainDataRequestObject);
	
	/**
	 * Each transactionable object must provide a daoId.
	 * 
	 */
	public String getDaoId();
	
	/**
	 * Each transactionable object must provide a daoId.
	 * 
	 */
	public Class<T> getDataBeanClass();
	
	/**
	 * Process data request response.
	 * 
	 */
	public void processDataResponse(IDAOResponse daoResponse, AppendPosition appendPosition, int positionInBuffer);
	
	/**
	 * Get dao request main object flag.  
	 */
	public boolean getDataRequestMainObject();
	
	/**
	 * Get dao request main object flag.  
	 */
	public void setDataRequestMainObject(boolean dataRequestMainObject);
	
}
