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

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;


/**
 * Data table action buttons column. </br>
 * 
 * <p>
 * Displays toolbar with action icons for common operations like (Add,Delete,Edit etc).
 * Uses a constructor parameter to configure the visible action icons.</br>
 * 
 * Has to be extended to implement desired behavior for each action. Each icon has 
 *  a corresponding method that handles the action and can be overidden. 
 * </p>
 * 
 * @author TVH Group NV
 *
 * @param <T> Data bean type.
 */
public class DataRowActionsToolbarColumn<T extends IDataBean> extends MyStrobeColumn<T, Void>{

	private static final long serialVersionUID = -4803598130593808441L;

	/**
	 * Enumeration of data row possible actions.
	 */
	public enum DataRecordAction { Add, View, Edit, Go, Delete };
	
	protected String columnHeaderLabel;
	
	protected DataRecordAction [] dataRecordActions = null;
	
	protected IDataObject<T> dataObject = null;
	
	/**
	 * Cosntructor
	 * 
	 * @param dataRecordActions Tool bar actions.
 	 */
	public DataRowActionsToolbarColumn(String columnHeaderLabel, DataRecordAction[] dataRecordActions, IDataObject<T> dataObject) {
		this.dataRecordActions = dataRecordActions;
		this.columnHeaderLabel = columnHeaderLabel;
		this.dataObject = dataObject;
	}
	
	public Component getHeader(String componentId) {
		return new Label(componentId, "");
	}

	public Void getSortProperty() {
		return null;
	}

	public boolean isSortable() {
		return false;
	}

	public void populateItem(Item<ICellPopulator<T>> cellItem,
			String componentId, IModel<T> rowModel) {
		
		cellItem.add( new DataRowActionsToolbarPanel<T>(componentId, rowModel, this.dataRecordActions) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onActionClick(DataRecordAction action, AjaxRequestTarget target, IModel<T> model) {
				switch (action) {
					case Add:
							onAddRecordClicked(target, model);
						break;
					case Edit:
							onEditRecordClicked(target, model);
						break;
					case Delete:
							onDeleteRecordClicked(target, model);
						break;
					case Go:
							onGoRecordClicked(target, model);
						break;
					default:
							onViewRecordClicked(target, model);
						break;
				}
			}
		});
	}
	
	public void detach() {
		// nothing to do
	}
	
	private void selectRow(IModel<T> model) {
		if (this.dataObject != null) {
			this.dataObject.moveToRow(model.getObject().getRowId());
		}
	}
	
	/*
	 * Method to be overridden to set add record link behaviors 
	 */
	protected void onAddRecordClicked(AjaxRequestTarget target, IModel<T> model) {
		
	}
	
	/*
	 * Method to be overridden to set edit record link behaviors 
	 */
	protected void onEditRecordClicked(AjaxRequestTarget target, IModel<T> model) {
		selectRow(model);
	}
	
	/*
	 * Method to be overridden to set view record link behaviors 
	 */
	protected void onViewRecordClicked(AjaxRequestTarget target, IModel<T> model) {
		selectRow(model);
	}

	/*
	 * Method to be overridden to set go record link behaviors 
	 */
	protected void onGoRecordClicked(AjaxRequestTarget target, IModel<T> model) {
		selectRow(model);
	}
	
	/*
	 * Method to be overridden to set delete record link behaviors 
	 */
	protected void onDeleteRecordClicked(AjaxRequestTarget target, IModel<T> model) {
		selectRow(model);
	}
}
