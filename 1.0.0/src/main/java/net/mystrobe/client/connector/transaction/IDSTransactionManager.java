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

/**
 * DS transaction manager has to be used whenever 
 *  data from multiple data objects has to be sent to the BL.</br>
 *  
 * Data objects that will and receive data through a single server call
 *  need to be added to the transaction manager first, either through
 *  transaction manager constructor or add participant method.</br>       
 * 
 * To send data to the BL a call to commit method is required. This will trigger
 *  the send data on all transactionable objects that were added to current transaction.</p>
 * 
 * @author TVH Group NV
 */
public interface IDSTransactionManager {
	
	/**
	 * Add object that will send and receive data from the BL through current transaction manager
	 * 
	 * @param transactionable Normally it should be a data object.
	 */
	public void addTransactionParticipant(IDSTransactionable<? extends IDataBean> ... transactionable);
	
	@Deprecated
	public void startTransaction();
	
	/**
	 * Method to call to send data to BL.
	 * 
	 * @throws WicketDSBLException Application exception occurred.
	 */
	public void commit() throws WicketDSBLException;
	
	/**
	 * Roll back changes in transactionable objects.
	 */
	public void rollback();
}
