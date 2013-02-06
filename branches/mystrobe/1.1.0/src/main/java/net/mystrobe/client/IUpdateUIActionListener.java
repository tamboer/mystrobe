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

/**
 * @author TVH Group NV
 */
public interface IUpdateUIActionListener extends LinkListener, IStateSource {

	/**
	 * Create new data record.
	 */
	public void addRecord();
	
	/**
	 * Create new data record and initialize its fields 
	 * with data from the <tt>initValuesMap</tt>.
	 * 
	 * @param initValuesMap Map having field names as keys and field
	 * 		initialization values as vale in the map. 
	 */
	public void addRecord(Map<String, Object> initValuesMap);
		
	
	/**
	 * Cancel add/update record operation record
	 */
	public void cancelRecord();
	
	
	/**
	 * Create new data record as a copy of current selected record.
	 */
	public void copyRecord();
	
	/**
	 * Delete current selected record
	 */
	public void deleteRecord();

	/**
	 * Reset current record info to initial values.
	 * 
	 * Method used with add/update record operations.
	 */
	public void resetRecord();
	
	/**
	 * Reset current record info to initial values and
	 *  set initialization values from the <tt>initValuesMap</tt>. 
	 * 
	 * Method used with add/update record operations.
	 * 
	 * @param initValuesMap Map having field names as keys and field
	 * 		initialization values as vale in the map.  
	 */
	public void resetRecord(Map<String, Object> initValuesMap);

	/**
	 * Edit current selected record
	 */
	public void editRecord();
	
	/**
	 * Commit changes done to new/edited record 
	 */
	public void saveRecord();
	
	/**
	 * Update current editing state to <tt>updateState</tt> 
	 * 
	 * @param updateState New update state.
	 */
	public void updateMode(UpdateStates updateState);

}
