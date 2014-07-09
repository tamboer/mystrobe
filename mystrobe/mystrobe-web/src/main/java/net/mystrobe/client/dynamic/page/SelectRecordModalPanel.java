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
 package net.mystrobe.client.dynamic.page;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.IDataTableDataSource;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.navigation.DataTablePagesNavigationPanel;
import net.mystrobe.client.dynamic.table.view.SimpleDataTableViewPanel;
import net.mystrobe.client.util.StringUtil;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Select record panel<br/>
 * 
 * Contains a {@link SimpleDataTableViewPanel} table to select from 
 *  and a {@link DataTablePagesNavigationPanel} for table navigate.
 *   
 * @author TVH Group NV
 *
 * @param <T> Selectable data object type
 */
public class SelectRecordModalPanel<T extends IDataBean> extends AbstractSelectRecordModalPanel<T> {
	
	private static final long serialVersionUID = 1538558992608861647L;
	
	protected static final Logger logger = LoggerFactory.getLogger(SelectRecordModalPanel.class); 

	protected IDynamicFormConfig<T> tableColumnsConfig = null;
	
	protected SimpleDataTableViewPanel<T> dataTablePanel;
	
	protected DataTablePagesNavigationPanel<T> dataTableNavigator;
	
	protected AjaxLink<T> okLink, cancelLink; 
	
	protected int tableSize = 10;
	
	protected int navigatorVisiblePages = 4;
	
	protected boolean selected = false;
	
	protected IDataBean selectedData;
	
	
	public SelectRecordModalPanel(String panelId, ModalWindow modalWindow, IDataObject<T> dataObject, IDynamicFormConfig<T> tableColumnsConfig) {
		super(panelId, modalWindow, dataObject);
		this.tableColumnsConfig = tableColumnsConfig;
		initializeRecordData();		
	}
	
	public SelectRecordModalPanel(String panelId, ModalWindow modalWindow) {
		super(panelId, modalWindow);
		initializeRecordData();		
	}
	
	public SelectRecordModalPanel(String panelId, ModalWindow modalWindow, IDataObject<T> dataObject) {
		super(panelId, modalWindow, dataObject);
		this.tableColumnsConfig = new DynamicFormConfig<T>(dataObject.getSchema());
		initializeRecordData();
	}
	
	
	protected void initializeRecordData() {
		
	}
	
	@Override
	public void initializePageComponents() throws WicketDSBLException{
		initializeSelectRecordComponents();
	}

	protected void initializeSelectRecordComponents() throws WicketDSBLException {
	
		if (this.dataObject == null || this.tableColumnsConfig == null) {
			throw new IllegalArgumentException("Data object and tables config can not be null.");
		}
		
		add(getEditPanel());
		
		dataTablePanel = new SimpleDataTableViewPanel<T>("dataTableId", this.tableColumnsConfig, tableSize) {

			private static final long serialVersionUID = 3411447515495271953L;

			@Override
			protected void onSortClick(AjaxRequestTarget target) {
				super.onSortClick(target);
				target.add(dataTableNavigator);
				onTableRefresh(target);
			}
			

			@Override
			protected void onDataChanged(AjaxRequestTarget target)  {
				super.onDataChanged(target);
				target.add(dataTableNavigator);
				target.add(this);
				selectedDataChanged(target);
				onTableRefresh(target);
			}
		};
		dataTablePanel.setOutputMarkupPlaceholderTag(true);
		ComponentLinker.bindDataTableData((IDataTableDataSource<T>)dataObject, dataTablePanel);
		ComponentLinker.bindSort(dataTablePanel, dataObject);
		
		dataTableNavigator = new DataTablePagesNavigationPanel<T>("dataTableNavigatorId", tableSize, navigatorVisiblePages) {

			private static final long serialVersionUID = 6164813084362784488L;

			@Override
			public void onRefreshContent(AjaxRequestTarget target) {
				super.onRefreshContent(target);
				target.add(dataTablePanel);
				onTableRefresh(target);
			}
		};
		
		ComponentLinker.bindDataTableNavigation(dataTableNavigator, dataObject);
		
		add(dataTablePanel);
		add(dataTableNavigator);
		
		okLink = new AjaxLink<T>("selectLinkId") {

			private static final long serialVersionUID = -1997282787068168634L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				SelectRecordModalPanel.this.selected = true;
				SelectRecordModalPanel.this.dataObject.cancelCRUDOperation();
				SelectRecordModalPanel.this.selectedData = SelectRecordModalPanel.this.dataObject.getData();  
				SelectRecordModalPanel.this.modalWindow.close(target);	
			}
			
			@Override
			public boolean isEnabled() {
				return dataTablePanel.getTableDataSize() > 0;
			}
		};
		okLink.setOutputMarkupId(true);
		
		cancelLink = new AjaxLink<T>("cancelLinkId") {

			private static final long serialVersionUID = -1997282787068168634L;

			
			@Override
			public void onClick(AjaxRequestTarget target) {
				SelectRecordModalPanel.this.selected = false;
				SelectRecordModalPanel.this.dataObject.cancelCRUDOperation();
				SelectRecordModalPanel.this.modalWindow.close(target);
			}
		};
		
		add(okLink);
		add(cancelLink);
		
		this.dataObject.resetDataBuffer();
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
	
	protected void selectedDataChanged(AjaxRequestTarget target) {
		onTableRefresh(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mystrobe.client.dynamic.page.ISelectRecordComponent#setSelectedData(net.mystrobe.client.IDataBean)
	 */
	public void setSelectedData(IDataBean dataBean) {
		this.selectedData = dataBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mystrobe.client.dynamic.page.ISelectRecordComponent#getSelectedData()
	 */
	public IDataBean getSelectedData() {
		return this.selectedData;
	}
	
	protected Component getEditPanel() {
		WebMarkupContainer editPanel = new WebMarkupContainer("editPanel");
		editPanel.setVisible(false);
		return editPanel;
	}
	
	public void onTableRefresh(AjaxRequestTarget target) {
	
	}
}
