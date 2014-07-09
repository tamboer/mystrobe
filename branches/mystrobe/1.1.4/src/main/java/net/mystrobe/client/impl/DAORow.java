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
 package net.mystrobe.client.impl;

import java.io.Serializable;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.connector.IDAORow;
import net.mystrobe.client.connector.RowState;
import net.mystrobe.client.util.DataBeanUtil;


/**
 * The default IDAORow implementation. Be carefull two dao rows are considered
 * equal if they have the same row id and row state.
 * 
 * @author TVH Group NV
 */
public class DAORow<T extends IDataBean> implements IDAORow<T>, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5863585746325482361L;
	
	protected RowState rowState = RowState.Unmodified;
    protected T rowData = null;
    protected T beforeImage = null;
    protected String rowId = null;
    protected boolean isConsideredForUpdate = false;
    
    public T getBeforeImage() {
        return beforeImage;
    }

    public T getRowData() {
        return rowData;
    }

    public RowState getRowState() {
        return rowState;
    }

    public void setRowState(RowState rowState) {
        this.rowState = rowState;
    }
    

    public void setBeforeImage(T beforeImage) {
        this.beforeImage = beforeImage;
    }

    public void setRowData(T rowData) {
        this.rowData = rowData;
    }

    public String getRowId() {
        if( this.rowId != null ) return this.rowId;

        if( this.getRowData() != null && this.getRowData().getRowId() != null ) {
            this.rowId =  this.getRowData().getRowId();
        } else if( this.getBeforeImage() != null && this.getBeforeImage().getRowId() != null ) {
            this.rowId =  this.getBeforeImage().getRowId();
        }

        return this.rowId;
    }
    
    public void setRowId(String rowId) {
        this.rowId = rowId;
    }
    
    public void copyDataToRowData(IDataBean dataBean) {
	   DataBeanUtil.copyData(dataBean, this.rowData, false, null, true); 	
	}
    
    public void copyDataToBeforeImageRowData(IDataBean dataBean) {
    	DataBeanUtil.copyData(dataBean, this.beforeImage, false, null, true); 	
    }
    
    public boolean equals(Object obj) {
        if( ! (obj instanceof IDAORow<?>) ) return false;

        IDAORow<?> daoRow = (IDAORow<?>) obj;

        if( this.getRowId() != null && daoRow.getRowId() != null ) {
            return this.getRowId().equals(  daoRow.getRowId() );
        }

        return false;
    }
    

    @Override
    public int hashCode() {

        if( this.getRowId() != null ) return this.getRowId().hashCode();
       
        return "".hashCode();
    }

	@Override
	public String toString() {
		return "DAORow [rowId=" + getRowId() + ", rowState=" + rowState + "]";
	}

	public boolean isConsideredForUpdate() {
		return isConsideredForUpdate;
	}

	public void setConsideredForUpdate(boolean isConsideredForUpdate) {
		this.isConsideredForUpdate = isConsideredForUpdate;
	}
}
