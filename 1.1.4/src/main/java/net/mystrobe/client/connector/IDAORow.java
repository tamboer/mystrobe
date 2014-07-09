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
 package net.mystrobe.client.connector;

import net.mystrobe.client.IDataBean;

/**
 * Represents a row of data / entity / instance to be sent to the server. It has a
 * row state associated to signalize the backend the operation that was the
 * generator (New/Update/Delete/Unmodified) of the current row, together with a
 * before image marking the original state of the row before the operation.
 * 
 * @author TVH Group NV
 */
public interface IDAORow<T extends IDataBean> {

	/**
	 * Returns the original state of the row before the operation.
	 */
	public T getBeforeImage();

	/**
	 * Returns the actual state of the row after the operation.
	 */
	public T getRowData();

	/**
	 * Returns the operation that was executed on the row: Update/New/Delete
	 */
	public RowState getRowState();


	/**
	 * Returns an unique string the identfies the record in the backend data object.
	 */
	public String getRowId();


    /**
	 * Sets the unique string the identfies the record in the backend data object.
     *
     * @param rowId the unique identifier of a record
	 */
    public void setRowId( String rowId );
    

    /**
     * Sets the row state of this row
     *
     * @param  rowState the row state of this row
     */
    public void setRowState( RowState rowState);

	/**
	 * Sets the original state of the row before the operation.
	 * 
	 * @param beforeImage
	 */
	public void setBeforeImage(T beforeImage);

	/**
	 * Sets the actual state of the row after the operation.
	 * 
	 * @param rowData
	 */
	public void setRowData(T rowData);
	
	/**
	 * Copy row data from parameter to current row data.
	 * 
	 * @param rowData
	 */
	public void copyDataToRowData(T dataBean);
    
	
	/**
	 * Copy row data from parameter to current row before image data.
	 * 
	 * @param rowData
	 */
	public void copyDataToBeforeImageRowData(T dataBean);
	
	/**
	 * Mark row to as considered for update operations
	 * 
	 * @return
	 */
	public boolean isConsideredForUpdate();

	/**
	 * Considered for update flag
	 * 
	 * @param isConsideredForUpdate
	 */
	public void setConsideredForUpdate(boolean isConsideredForUpdate);

}