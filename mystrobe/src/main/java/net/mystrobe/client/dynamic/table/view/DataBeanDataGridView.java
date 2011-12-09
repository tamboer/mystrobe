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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.ISortListener;
import net.mystrobe.client.SortState;
import net.mystrobe.client.connector.quarixbackend.NamingHelper;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig;
import net.mystrobe.client.util.StringUtil;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink.ICssProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TVH Group NV
 */
public class DataBeanDataGridView<T extends IDataBean> extends Panel  {

	private static final long serialVersionUID = 1L;

	protected static final Logger logger = LoggerFactory.getLogger(DataBeanDataGridView.class);
	
	protected static final String DATA_TABLE_ROW_MARKUP_ID = "rows";
	
	protected static final String DATA_TABLE_HEADERS_MARKUP_ID = "headers";
	
	protected static final String DATA_TABLE_HEADER_MARKUP_ID = "header";

	protected static final String DATA_TABLE_HEADER_LABEL_MARKUP_ID = "header_label";
	/**
	 * Current page
	 */
	protected int tableSize = 30;
	
	/**
	 * Columns config.
	 */
	protected IDynamicFormConfig<T> columnsConfig;
	
	/**
	 * Sort listener
	 */
	protected ISortListener sortListener;
	
	/**
	 * Additional table columns
	 */
	protected List<DataTableColumn<T>> additionalTableColumns = null;
	
	/**
	 * Selected row css class
	 */
	protected static final String CSS_CLASS_SELECTED = "selected";
	
	protected DataGridView<T> dataTableView;
	
	protected List<ICellPopulator<T>> dataTableColumns;
	
	protected boolean sortableColumns = true;
	
	protected ICssProvider cssSortProvider = new OrderByLink.CssProvider("sort-asc-btn", "sort-desc-btn", "sort-btn");
	
	protected boolean selectableRows = true;

	protected String selectedRowCSSClassName = null;
	
	protected String selectedRowId = null;
	
	public DataBeanDataGridView(String id, List<T> dataList, final IDynamicFormConfig<T> columnsConfig, int tableSize) {
		super(id);
		setTableConfigurationValues(columnsConfig, tableSize, this.cssSortProvider, this.additionalTableColumns, this.sortableColumns);
		initialize(dataList);
	}
	
	public DataBeanDataGridView(String id, List<T> dataList, final IDynamicFormConfig<T> columnsConfig, int tableSize, boolean sortableColumns) {
		super(id);
		setTableConfigurationValues(columnsConfig, tableSize, this.cssSortProvider, this.additionalTableColumns, sortableColumns);
		initialize(dataList);
	}
	
	public DataBeanDataGridView(String id, List<T> dataList, final IDynamicFormConfig<T> columnsConfig, int tableSize, ICssProvider cssProvider) {
		super(id);
		setTableConfigurationValues(columnsConfig, tableSize, cssProvider, this.additionalTableColumns, sortableColumns);
		initialize(dataList);
	}
	
	public DataBeanDataGridView(String id, List<T> dataList, IDynamicFormConfig<T> tableColumnsConfig, int tableSize, List<DataTableColumn<T>> additionalTableColumns) {
		super(id);
		setTableConfigurationValues(tableColumnsConfig, tableSize, this.cssSortProvider, additionalTableColumns, this.sortableColumns);
		initialize(dataList);
	}

	public DataBeanDataGridView(String id, List<T> dataList, IDynamicFormConfig<T> tableColumnsConfig, int tableSize, List<DataTableColumn<T>> additionalTableColumns, ICssProvider cssSortProvider) {
		super(id);
		setTableConfigurationValues(tableColumnsConfig, tableSize, cssSortProvider, additionalTableColumns, true);
		initialize(dataList);
	}
	
	private void setTableConfigurationValues(IDynamicFormConfig<T> columnsConfig, int tableSize, ICssProvider cssSortProvider, List<DataTableColumn<T>> additionalTableColumns, boolean sortableColumns) {
		setOutputMarkupId(true);
		this.columnsConfig = columnsConfig;
		this.tableSize = tableSize;
		this.cssSortProvider = cssSortProvider;
		this.additionalTableColumns = additionalTableColumns;
		this.sortableColumns = sortableColumns;
	}
	
	protected void initialize(List<T> listData) {
		//build data bean visible properties columns
		this.dataTableColumns = buildTableColumns();
		
		buildTableHeaders();
		
		//create data table grid
		this.dataTableView = buildDataView(listData);
		
		this.dataTableView.setItemsPerPage(this.tableSize);
		
		this.dataTableView.setOutputMarkupId(true);
		
		add(dataTableView);
	}
	
	/**
	 * Refresh current table visible data.
	 * 
	 * Used on ajax requests to simply display different data
	 *  without having to rebuild headers and columns structures. 
	 * 
	 * @param listData New data to show in the grid.
	 */
	public void updateDataBeanGridViewData(List<T> listData) {
		
		logger.debug("update data available called with size: " + listData.size());
		
		this.selectedRowId = null;
		
		//refresh current grid view with new data
		dataTableView = buildDataView(listData);
		dataTableView.setItemsPerPage(this.tableSize);
		dataTableView.setOutputMarkupId(true);
		
		if (this.get(DATA_TABLE_ROW_MARKUP_ID) != null) {
			logger.debug("replace used");
			this.get(DATA_TABLE_ROW_MARKUP_ID).replaceWith(dataTableView);
		} else {
			logger.debug("add used");
			add(dataTableView);
		}
	}
	
	/**
	 * Method creates data table headers.
	 */
	protected void buildTableHeaders() {
		//build data record column headers  
		RepeatingView tableHeaders = new RepeatingView(DATA_TABLE_HEADERS_MARKUP_ID);
		
		final PropertiesSort propertiesSort = new PropertiesSort() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onChangeSortState(SortState sortState) {
				DataBeanDataGridView.this.onChangeSortState(sortState);
			}
		};
		
		for (ICellPopulator<T> cellPopulator : dataTableColumns) {
			final IColumn<T> column = (IColumn<T>) cellPopulator;
				
			WebMarkupContainer item = new WebMarkupContainer(tableHeaders.newChildId());
			WebMarkupContainer headerSort = null;
			
			if (!column.isSortable() || !sortableColumns ) {
				headerSort = new WebMarkupContainer(DATA_TABLE_HEADER_MARKUP_ID);
				headerSort.setRenderBodyOnly(true);
			} else {
				propertiesSort.addSortableProperty(column.getSortProperty(), SortOrder.NONE);
//				headerSort = new AjaxFallbackOrderByLink(DATA_TABLE_HEADER_MARKUP_ID, column.getSortProperty(), propertiesSort, this.cssSortProvider) {
//					
//					private static final long serialVersionUID = -9165302627136687555L;
//
//					@Override
//					public void onClick(AjaxRequestTarget target) {
//						target.add(DataBeanDataGridView.this);
//						onSortClick(target);						
//					}
//				};
				
				headerSort = new WebMarkupContainer(DATA_TABLE_HEADER_MARKUP_ID);
				headerSort.add(new Behavior() {
					private static final long serialVersionUID = 1L;

					public void onComponentTag(Component component, ComponentTag tag)
			        {
				            super.onComponentTag(component, tag);
				            ISortState sortState = propertiesSort.getSortState();
				            String cssClass = DataBeanDataGridView.this.cssSortProvider.getClassAttributeValue(sortState, column.getSortProperty());
				            if(!StringUtil.isNullOrEmpty(cssClass))
				                tag.append("class", cssClass, " ");
			        }
					
				}); 
				
				item.add(new AjaxEventBehavior("onclick") {

					private static final long serialVersionUID = -8262903803276071092L;

					@Override
					protected void onEvent(AjaxRequestTarget target) {
						 ISortState state = propertiesSort.getSortState();
					     SortOrder order = state.getPropertySortOrder(column.getSortProperty());
					     state.setPropertySortOrder(column.getSortProperty(), nextSortOrder(order));
					     
					     target.add(DataBeanDataGridView.this);
						 onSortClick(target);
					}
					
					
					protected SortOrder nextSortOrder(SortOrder order)
				    {
				        if(order == SortOrder.NONE)
				            return SortOrder.ASCENDING;
				        else
				            return order != SortOrder.ASCENDING ? SortOrder.ASCENDING : SortOrder.DESCENDING;
				    } 
					
				});
				
				item.add(new AttributeAppender("style", new Model<String>("cursor:pointer;"), ""));
			}
			
			Component label = column.getHeader(DATA_TABLE_HEADER_LABEL_MARKUP_ID);
			label.setRenderBodyOnly(true);
			
			item.add(label);
			item.add(headerSort);
			
			tableHeaders.add(item);
		}
		
		add(tableHeaders);
	}
	
	/**
	 * Build data table columns.<br/>
	 * 
	 * Each visible data bean property column is build using a {@link PropertyColumn} instance.
	 * 
	 * @return List of data beans properties columns.
	 */
	protected List<ICellPopulator<T>> buildTableColumns() {
		
		List<String> visibleColumns = this.columnsConfig.getVisibleColumnNames();
		List<ICellPopulator<T>> tableColumns = new ArrayList<ICellPopulator<T>>(visibleColumns.size() + 1);
		
		Class<?> tClass = null;
		ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
		if (superclass != null && superclass.getActualTypeArguments() != null && 
				superclass.getActualTypeArguments().length >= 1) {
			
			Type aType =  ((ParameterizedType) superclass).getActualTypeArguments()[0]; 
			if ( aType instanceof Class<?> ) {
				tClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
			}
		}
		
		boolean alignRight = false;
		for (String columnName : visibleColumns) {
			Map<IDynamicFormFieldConfig.Property, Object> columnProperties  = this.columnsConfig.getColumnProperties(columnName);
			
			IModel<String> headerLabelModel = null;
			Object headerLabel = columnProperties.get(IDynamicFormFieldConfig.Property.Label);
			
			if (headerLabel instanceof IModel<?>) {
				headerLabelModel = (IModel<String>) headerLabel;
			} else {
				String headerLabelString = (String) headerLabel;
				if (this.columnsConfig.getLocalizableFormLabels()) {
					headerLabelModel = new ResourceModel(headerLabelString, StringUtil.buildDefaultResourceValue(headerLabelString));
				} else {
					 if (StringUtil.isNullOrEmpty(headerLabelString)) {
						 headerLabel = columnName.toUpperCase();
					 }
					 headerLabelModel = Model.of(headerLabelString);
				}
			}
			
			boolean sortable = columnProperties.get(IDynamicFormFieldConfig.Property.Sortable) != null && 
					(Boolean)columnProperties.get(IDynamicFormFieldConfig.Property.Sortable);
			
			String sortProperty = sortable ? columnName : null;
			if (tClass != null) {
				try {
					Class<?> fieldClass = tClass.getDeclaredField(columnName).getType();
					if (Number.class.isAssignableFrom(fieldClass) ||
							Date.class.isAssignableFrom(fieldClass)) {
						alignRight = true;
					} else {
						alignRight = false;
					}
				} catch (Exception e) {
					alignRight = false;
				}
			}
			
			String width = null;
			if (columnProperties.containsKey(IDynamicFormFieldConfig.Property.Width)) {
				width = columnProperties.get(IDynamicFormFieldConfig.Property.Width).toString();
			}
			
			IColumn<T> column = new DataBeanPropertyColumn<T>(headerLabelModel, sortProperty, NamingHelper.getFieldName(columnName), alignRight, width);			
			tableColumns.add(column); 
		}
		
		//check for additional columns to add
		if (this.additionalTableColumns != null) {
			for (DataTableColumn<T> columnData : this.additionalTableColumns) {
				if (columnData.getColumnPosition() >= 0 && columnData.getColumnPosition() <= tableColumns.size()) {
					tableColumns.add(columnData.getColumnPosition(), columnData.getColumnInfo());
				} else if (columnData.getColumnPosition() < 0) {
					tableColumns.add(0, columnData.getColumnInfo());
				}  else {
					tableColumns.add(tableColumns.size(), columnData.getColumnInfo());
				}
			}
		}
		
		return tableColumns;
	}
	
	/**
	 * Build data grid view .
	 * 
	 * @return
	 */
	protected DataGridView<T> buildDataView(List<T> listData) {
		
		return new DataGridView<T>(DATA_TABLE_ROW_MARKUP_ID, this.dataTableColumns, new ListDataProvider<T>(listData)){
			
			private static final long serialVersionUID = 6433839814573681704L;

			@Override
			protected Item<T> newRowItem(String id, int index,
					IModel<T> model) {
				
				final Item<T> result =  super.newRowItem(id, index, model);
				final T dataBean = model.getObject();
				
				result.setOutputMarkupId(true);
				
				if (DataBeanDataGridView.this.selectableRows) {
					result.add(new AjaxEventBehavior("onclick") {
						private static final long serialVersionUID = 1L;
						
						@Override
						protected void onEvent(AjaxRequestTarget target) {
							selectDataRowWithId(dataBean.getRowId());
							target.appendJavaScript("selectTableRow('" + result.getMarkupId() + "' , '" + getSelectedRowCSSClassName() + "');");
							onDataRowClick(target);
						}
					});
				}
				
				result.add(new AttributeAppender("class",  new LoadableDetachableModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected String load() {
						if(DataBeanDataGridView.this.selectableRows && 
								(dataBean.getRowId() != null && 
										(dataBean.getRowId().equals(selectedRowId)) ||
								isDataRowSelected(dataBean))){
							return getSelectedRowCSSClassName();
						}
						return StringUtil.EMPTY_STRING;	
					}
				}, " "));
				
				//call data table method to support additional customizations
				DataBeanDataGridView.this.newRowItem(id, index, model, result);
				
				return result;
			}
		};
	}
	
	protected void onChangeSortState(SortState sortState) {
		//TODO implement default column sorting class
	}
	
	protected void onSortClick(AjaxRequestTarget target) {
		onDataChanged(target);
	}
	
	protected void selectDataRowWithId(String rowId){
		this.selectedRowId = rowId;
	}
	
	protected boolean isDataRowSelected(IDataBean dataRow){
		return this.selectedRowId != null && this.selectedRowId.equals(dataRow.getRowId());
	}
	
	protected void onDataRowClick(AjaxRequestTarget target) {
		onDataChanged(target);
	}
	
	protected void newRowItem(String id, int index,	IModel<T> model, Item<T> rowItem) {
	}
	
	/**
	 * @return the selectableRows
	 */
	public boolean isSelectableRows() {
		return selectableRows;
	}

	/**
	 * Method executed when current selected data is changed.
	 * 
	 * @param target Ajax request target.
	 */
	protected void onDataChanged(AjaxRequestTarget target) {
		
	}
	
	/**
	 * @param selectableRows the selectableRows to set
	 */
	public void setSelectableRows(boolean selectableRows) {
		this.selectableRows = selectableRows;
	}

	/**
	 * @return the selectedRowCSSClassName
	 */
	public String getSelectedRowCSSClassName() {
		return selectedRowCSSClassName != null ? selectedRowCSSClassName : CSS_CLASS_SELECTED;
	}

	/**
	 * @param selectedRowCSSClassName the selectedRowCSSClassName to set
	 */
	public void setSelectedRowCSSClassName(String selectedRowCSSClassName) {
		this.selectedRowCSSClassName = selectedRowCSSClassName;
	}
	
	/**
	 * Get nr. of rows displayed in the table.
	 * 
	 * @return Table rows count.
	 */
	public int getTableDataSize() {
		return this.dataTableView != null ? this.dataTableView.getItemCount() : 0;
	}
	
	/**
	 * Data bean property column
	 * 
	 * @author TVH Group NV
	 *
	 * @param <T> 
	 */
	private class DataBeanPropertyColumn<T extends IDataBean> extends PropertyColumn<T> {

		private static final long serialVersionUID = -8120184633999316727L;
		
		private boolean alignRight = false;
		
		private String width = null;
		
		public DataBeanPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression,
				boolean alignRight, String width) {
			super(displayModel, sortProperty, propertyExpression);
			this.alignRight = alignRight;
			this.width = width;
		}

		@Override
		public void populateItem(Item<ICellPopulator<T>> item,
				String componentId, IModel<T> rowModel) {
			
			super.populateItem(item, componentId, rowModel);
			
			if (alignRight) {
				item.add(new AttributeAppender("align", Model.of("right"), " "));
			}
			
			if (width != null) {
				item.add(new AttributeAppender("width", Model.of(width), " "));
			}
		}
	}
}
