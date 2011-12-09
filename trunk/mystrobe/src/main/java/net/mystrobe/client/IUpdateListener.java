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
 package net.mystrobe.client;

import java.util.Map;

import net.mystrobe.client.connector.messages.IConnectorResponseMessages;
import net.mystrobe.client.connector.transaction.DSTransactionManager;
import net.mystrobe.client.connector.transaction.IDSTransactionManager;
import net.mystrobe.client.connector.transaction.WicketDSBLException;


/**
 * @author TVH Group NV
 */
public interface IUpdateListener<T extends IDataBean> extends IStateSource, IConnectorResponseMessages {

	/**
	 * Create new record
	 */
	public void createData(boolean copyData);
	
	/**
	 * Delete record.
	 *  
	 * @param dataType
	 */
	public void deleteData(T dataType) throws WicketDSBLException;
	
	/**
	 * Delete current record.
	 * @param dataType
	 */
	public void deleteData() throws WicketDSBLException;
	
	/**
	 * Update <tt>dataType</tt> record.
	 *  
	 * @param dataType Record to be updated.
	 */
	public void updateData(T dataType) throws WicketDSBLException;
	
	/**
	 * Update current record
	 */
	public void updateData() throws WicketDSBLException;

	/**
	 * Cancel current new/update operation. 
	 */
	public void cancelCRUDOpertaion();
    
	/**
	 * Reset data as it was before any changes were done. 
	 */
	@Deprecated
	public void resetData();
	
	/**
	 * Copy field values from map to current data bean. 
	 */
	public void setDataInitialValues(Map<String, Object> initialValues);
	
	/**
	 * True if last commit operation successful
	 */
	public boolean getUpdateDateCommitSuccess(); 
	
	/**
	 * True if last commit operation successful
	 */
	public void accept(IDSTransactionManager transactionManager); 
}
