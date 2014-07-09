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

/**
 * Data fetch transaction manager.</br>
 *
 * In order to improve performance by reducing the number of data requests performed
 *  to retrieve data from the BL use the data request transaction manager to fetch data 
 *  for multiple data objects at once.</br>
 *  
 * Data request transaction manager is current replacement of missing relationships
 *  in the data beans. We can not for example fetch a parent object and all its children 
 *  in a single server call, also there is no mechanism to save a list of children
 *  for a parent bean. Implementations will have to take care of correctly fetching the 
 *  children/secondary data and store it into local objects.</p>     
 * 
 * Add the data objects to the request transaction manager and fetch data by calling
 *  on of the <tt>dataRequest</tt> methods or by marking on of the data objects
 *   main request data object and make requests on that object.</br>   
 *   
 * Navigation will only be supported over the main data object. For all secondary objects
 *  buffer data will be completely replace each time a new call is made. Implementations will
 *  have to expose hooks after each data fetch so that appropriate implementations will handle 
 *  secondary/children data storing.          
 * 
 * @author TVH Group NV
 */
public interface IDSRequestTransactionManager {
	
	/**
	 * Simple data request that will completely replace buffers
	 */
	public void dataRequest(); 

	/**
	 * Data request that will use <tt>appendPosition</tt> to add data
	 *  for the main object data buffer and will replace that for secondary objects
	 *  
	 * @param appendPosition Main data object buffer append position. 
	 */
	public void dataRequest(AppendPosition appendPosition); 
	
	/**
	 * Data request that will use <tt>appendPosition</tt> to add data
	 *  for the main object data buffer and will replace that for secondary objects
	 *  
	 * @param appendPosition Main data object buffer append position. 
	 */
	public void dataRequest(AppendPosition appendPosition, int positionInBuffer); 
	
	
	/**
	 * Simple data request that witch command name:<tt>commandName</tt>.</br>
	 * 
	 * Use this method when different command than <b>sendRows</b> has to be called on the BL. 
	 * 
	 * @param commandName Command name to send to BL.
	 */
	public void dataRequest(String commandName);

	/**
	 * Data request with command name:<tt>commandName</tt> that will use <tt>appendPosition</tt> to add data
	 *  for the main object data buffer and will replace that for secondary objects. </br>
	 *  
	 * Use this method when different command than <b>sendRows</b> has to be called on the BL.  
	 *   
	 * @param commandName Command name to send to BL.
	 * @param appendPosition Main data object buffer append position. 
	 */
	public void dataRequest(String commandName, AppendPosition appendPosition, int positionInBuffer);
	
	
	/**
	 * Add transaction participant.
	 * 
	 * @param transactionable Transactionable object.
	 * @param dataRequestMainObject Main data object flag
	 */
	public void addRequestTransactionParticipant(IDSTransactionable<? extends IDataBean>  transactionable, boolean dataRequestMainObject);
	
	/**
	 * Add list of transactionable objects.
	 * 
	 * As a convention first object in list in considered as main data object. 
	 * 
	 * @param transactionables
	 */
	public void addRequestTransactionParticipant(IDSTransactionable<? extends IDataBean> ... transactionables);
}
