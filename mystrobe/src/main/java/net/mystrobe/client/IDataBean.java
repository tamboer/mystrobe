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

import java.io.Serializable;

/**
 * An actual instance of data containing a reference to the data source and an
 * rowId that uniquicly identifies the record on the backend data object.
 *
 * @author TVH Group NV
 */
public interface IDataBean extends Cloneable, Serializable  {

	/**
	 * Columns used for state management and identification  
	 */
	public static final String ROW_ID_FIELD_NAME = "rowid";
	
	public static final String ROW_STATE_FIELD_NAME = "rowstate";
	
	/**
	 * Returns an unique string the identfies the record in the backend data object.
	 */
	public String getRowId();

	
	/**
	 * Returns an unique string the identfies the record in the backend data object.
	 */
	public void setRowid(String rowId);
}