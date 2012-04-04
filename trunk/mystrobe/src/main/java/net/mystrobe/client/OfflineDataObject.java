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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.mystrobe.client.connector.IDAORow;
import net.mystrobe.client.connector.IDaoRowList;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.impl.DAORow;
import net.mystrobe.client.util.DataBeanUtil;

/**
 * @author TVH Group NV
 */
public class OfflineDataObject<T extends IDataBean> extends DataObjectAdaptor<T> implements IOfflineDataObject<T> {

	private static final long serialVersionUID = -3601724816495558251L;

	private int rowId = 0;
	
	private Collection<T> originalValuesList = null;
	
	private Comparator<T> itemsComparator = null;

	public OfflineDataObject(final Collection<T> values, final IDAOSchema<T> schema) {
		this.offlineMode = true;
		this.schema = schema;
		this.originalValuesList = values;
	}
	
	public void setItemsComparator(Comparator<T> itemsComparator) {
		this.itemsComparator = itemsComparator;
	}

	protected void initializeBufferData(final Collection<T> values) {
		this.hasFirstRow = true;
		this.hasLastRow = true;

		//use sorted set // define comparator according to sort state
		Collection<IDAORow<T>> rows = new ArrayList<IDAORow<T>>(values.size());
		for (T value : values) {
			DAORow<T> row = new DAORow<T>();

			this.dataTypeClass = getSchema().getIDataTypeClass();
			T beforeImage = null;
			
			try {
				beforeImage = this.dataTypeClass.newInstance();
			} catch (InstantiationException ex) {
				getLog().error("Can not instantiate bean class " + this.dataTypeClass.getName(), ex);
			} catch (IllegalAccessException ex) {
				getLog().error("Can not instantiate bean class " + this.dataTypeClass.getName(), ex);
			}

			String rowId = getNextRowId();
			value.setRowid(rowId);
			beforeImage.setRowid(rowId);
			row.setRowId(rowId);
						
			row.setRowData(value);
			row.setBeforeImage(beforeImage);
			row.copyDataToBeforeImageRowData(value);
			
			if (applyFiltersOnRow(row.getRowData())) {
				rows.add(row);
			}
		}
		
		this.dataBuffer = new IDaoRowList<T>(rows);
	}

	private String getNextRowId() {
		this.rowId++;
		return String.valueOf(rowId);
	}

	@Override
	public void resetDataBuffer() {
		
		//Collections.sort
		if (itemsComparator != null ) {
			initializeBufferData(applySort(originalValuesList));
		} else {
			initializeBufferData(originalValuesList);
		}
		
		publishOnDataBufferReplaced();
		
		cursorPosition = -1;
		cursorPreviousPosition = -1;
		
		this.currentData = null;

		moveToRow(0, false);
	}
	
	@Override
	public void resetDataBuffer(Collection<T> values) {
		this.originalValuesList = values;
		resetDataBuffer();
	}
	
	@Override
	public void deleteData(T dataType) throws WicketDSBLException {
		super.deleteData(dataType);
		this.originalValuesList.remove(dataType);
	}
	
	@Override
	public void createData(boolean copyData) {
		super.createData(copyData);
	} 
	
	@Override
	public void updateData() {
		
		IDAORow<T> row = this.dataBuffer.get(this.cursorPosition);

		switch (row.getRowState()) {
			case New:
				this.originalValuesList.add(row.getRowData());
				break;
	
			case Updated:
				break;
		}
	}
	
	@Override
	public void cancelCRUDOpertaion() {
		
		if (this.cursorPosition < 0 || this.dataBuffer.isEmpty()) {
			return;
		}

		IDAORow<T> row = this.dataBuffer.get(this.cursorPosition);
		
		switch (row.getRowState()) {
			case New:
				this.originalValuesList.remove(row.getRowData());
				break;
	
			case Updated:
				break;
		}
		
		super.cancelCRUDOpertaion();
	}
	
	protected boolean applyFiltersOnRow(T row) {
		
		if (this.filters.isEmpty()) {
			return true;
		}
		
		boolean validColumnValue = true;
		
		for (IFilterParameter filter : this.filters) {
			
			String filterColumnName = filter.getColumn();
			Object filterValue = filter.getValue();
			
			Object rowColumnValue = DataBeanUtil.getFieldValue(row, filterColumnName, null);
			
			if (rowColumnValue == null) {
				//filter column not in data object - ignore
				getLog().warn("No field found for column filter name:" + filterColumnName);
				continue;
			}
			
			if (filterValue == null && rowColumnValue != null) {
				return false;
			}
			
			if(rowColumnValue != null && filterValue != null) { 
				switch (filter.getOperator()) {
					case EQ:
						validColumnValue = filterValue.equals(rowColumnValue);
						break;
						
					case BEGINS:	
						validColumnValue = rowColumnValue.toString().toLowerCase().startsWith(filterValue.toString().toLowerCase());
						break;
						
					case GT:
					case LT:	
					case GE:
					case LE:
						long filterLongValue = 0l;
						long columnLongValue = 0l;
						if (rowColumnValue instanceof Number) {
							Number filterNumberValue = (Number) filterValue; 
							Number columnNumberValue = (Number) rowColumnValue; 
							
							filterLongValue = filterNumberValue.longValue();
							columnLongValue = columnNumberValue.longValue();
						} else if (rowColumnValue instanceof Date) {
							Date filterDate = (Date) filterValue; 
							Date columnDate = (Date) rowColumnValue;
							
							filterLongValue = filterDate.getTime();
							columnLongValue = columnDate.getTime();
						}
						
						if (filter.getOperator().equals(FilterOperator.GT)) {
							validColumnValue =  columnLongValue > filterLongValue;
						} else if (filter.getOperator().equals(FilterOperator.GE)) {
							validColumnValue = columnLongValue >= filterLongValue;
						} else if (filter.getOperator().equals(FilterOperator.LT)) {
							validColumnValue = columnLongValue < filterLongValue;
						} else {
							validColumnValue = columnLongValue <= filterLongValue;
						}
						
					case MATCHES:	
						break;
				}
			}
			
			if (!validColumnValue) {
				return false;
			}
		}
		return true;
	}
	
	protected List<T> applySort(Collection<T> items) {
		
		List<T> itemsList = new ArrayList<T>(items.size());
		itemsList.addAll(items);
		
		Collections.sort(itemsList, itemsComparator);
		
		return itemsList;
	}

	@Override
	protected void assignValues() {
		// TODO Auto-generated method stub
	}
}
