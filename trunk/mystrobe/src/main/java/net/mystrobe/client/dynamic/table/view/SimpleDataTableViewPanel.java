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
 package net.mystrobe.client.dynamic.table.view;

import java.util.ArrayList;
import java.util.List;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataTableDataListener;
import net.mystrobe.client.IDataTableDataSource;
import net.mystrobe.client.ISortListener;
import net.mystrobe.client.ISortSource;
import net.mystrobe.client.SortState;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.ui.UICssResourceReference;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink.ICssProvider;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;


/**
 * Data table view component.<br/>
 * 
 * Displays multiple data objects information as a html table. Table configurations can be set 
 *  using a {@link IDynamicFormConfig} constructor parameter.<br/>
 *  
 * Table data is fetched through the {@link IDataTableDataListener} interface. Component has to be linked 
 *  to a {@ IDataTableDataSource} component in order to receive data.  
 * 
 * @author TVH Group NV
 *
 * @param <T> IDataBean object to be displayed in teh table rows.
 */
public class SimpleDataTableViewPanel<T extends IDataBean> extends DataBeanDataGridView<T> implements  IDataTableDataListener<T>, ISortSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Table data source
	 */
	protected IDataTableDataSource<T> tableDataSource;
	
	/**
	 * Sort listener
	 */
	protected ISortListener sortListener;
	
	/**
	 * Constructor. 
	 * 
	 * @param id Data table panel id.
	 * @param columnsConfig Table column configuration.
	 * @param tableSize Number of rows to be displayed. 
	 */
	public SimpleDataTableViewPanel(String id, final IDynamicFormConfig<T> columnsConfig, int tableSize) {
		super(id, new ArrayList<T>(0), columnsConfig, tableSize);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id Data table panel id.
	 * @param columnsConfig Table column configuration.
	 * @param tableSize Number of rows to be displayed.
	 * @param sortableColumns Flag set to true to see sort actions for sortable columns
	 */
	public SimpleDataTableViewPanel(String id, final IDynamicFormConfig<T> columnsConfig, int tableSize, boolean sortableColumns) {
		super(id, new ArrayList<T>(0), columnsConfig, tableSize, sortableColumns);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id Data table panel id.
	 * @param columnsConfig Table column configuration.
	 * @param tableSize Number of rows to be displayed.
	 * @param cssSortProvider Css sort links display provider.
	 */
	public SimpleDataTableViewPanel(String id, IDynamicFormConfig<T> columnsConfig, int tableSize, ICssProvider cssSortProvider) {
		super(id, new ArrayList<T>(0), columnsConfig, tableSize, cssSortProvider);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id Data table panel id.
	 * @param columnsConfig Table column configuration.
	 * @param tableSize Number of rows to be displayed.
	 * @param additionalTableColumns Table columns to be added to the data table rows.
	 */
	public SimpleDataTableViewPanel(String id, IDynamicFormConfig<T> columnsConfig, int tableSize, List<DataTableColumn<T, ?>> additionalTableColumns ) {
		super(id, new ArrayList<T>(0), columnsConfig, tableSize, additionalTableColumns);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id Data table panel id.
	 * @param columnsConfig Table column configuration.
	 * @param tableSize Number of rows to be displayed.
	 * @param cssSortProvider Css sort links display provider. 
	 * @param additionalTableColumns Table columns to be added to the data table rows.
	 */
	public SimpleDataTableViewPanel(String id, IDynamicFormConfig<T> columnsConfig, int tableSize, ICssProvider cssSortProvider, List<DataTableColumn<T,?>> additionalTableColumns) {
		super(id, new ArrayList<T>(0), columnsConfig, tableSize, additionalTableColumns, cssSortProvider);
	}
	
	
	@Override
	protected void selectDataRowWithId(String rowId){
		this.tableDataSource.moveToRow(rowId);
		super.selectDataRowWithId(rowId);
	}
	
	@Override
	protected boolean isDataRowSelected(IDataBean dataRow){
		if (dataRow == null || dataRow.getRowId() == null || this.tableDataSource.getData() == null)
			return false;
		
		boolean response = dataRow.getRowId().equals(this.tableDataSource.getData().getRowId());
		
		if (response) {
			this.selectedRowId = dataRow.getRowId();
		}
		
		return response;
	}
	
	public void setDataTableDataSource(IDataTableDataSource<T> source) {
		this.tableDataSource = source;  
	}
	
	
	public ISortListener getSortListener() {
		return this.sortListener;
	}

	public void setSortListener(ISortListener dataObject) {
		this.sortListener = dataObject;
		
	}
	
	public void dataAvailable(List<T> listData, int navigatorSize) {
		logger.debug("data available received navigator size:" + navigatorSize + " this table size:" + tableSize );
		if (this.tableSize == navigatorSize) {
			updateDataBeanGridViewData(listData);
		}
	}
	
	@Override
	protected void onChangeSortState(SortState sortState) {
		if (this.sortListener != null) { 
			this.sortListener.applySortState(sortState);
		}
	}
	
	public int getTableDataSize() {
		return super.getTableDataSize();
	}

	@Override
	public int getSize() {
		return this.tableSize;
	}
	
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(UICssResourceReference.get()));
	 }
}
