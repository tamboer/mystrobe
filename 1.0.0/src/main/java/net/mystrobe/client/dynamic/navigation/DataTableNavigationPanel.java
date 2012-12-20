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
 package net.mystrobe.client.dynamic.navigation;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.navigator.IDataTableNavigatorSource;
import net.mystrobe.client.navigator.IDataTableNavigatorListener.DataTableNavigationDirection;
import net.mystrobe.client.navigator.IDataTableNavigatorListener.DataTableNavigationState;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author TVH Group NV
 */
public class DataTableNavigationPanel<T extends IDataBean> extends AbstractDataTableNavigationPanel<T> implements IDataTableNavigatorSource<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2030761860019041864L;

	/**
	 * Navigation buttons
	 */
	private Button nextPageButton, previousPageButton;
	
	/**
	 * Page number label
	 */
	private Label currentPageNumberLabel;
	
	/**
	 * Navigation Form
	 */
	private Form<Void> navigationForm;
	
	public DataTableNavigationPanel(String id, final int pageSize) {
		super(id, pageSize);
		this.pageSize = pageSize;
		setOutputMarkupId(true);
		
		navigationForm = new Form<Void>("navigationForm_Id"); 
		
		nextPageButton = new Button("nextPageButtonId") {

			private static final long serialVersionUID = -917977864182750756L;

			@Override
			public void onSubmit() {
				super.onSubmit();
				currentPageNumber++;
				navigationListener.nextPageData(DataTableNavigationPanel.this, lastDataRowId, pageSize, currentPageNumber, false);
				
				//last data row id is changed by the update navigator callback
				navigationListener.moveToRow(firstDataRowId);
			}
		};
		navigationForm.add(nextPageButton);
		
		previousPageButton = new Button("previousPageButtonId") {

			private static final long serialVersionUID = -1774153920726974815L;

			@Override
			public void onSubmit() {
				super.onSubmit();
				if (currentPageNumber > 1) {
					currentPageNumber--; 
				}
				navigationListener.previousPageData(DataTableNavigationPanel.this, firstDataRowId, pageSize, currentPageNumber, false);
				
				//last data row id is changed by the update navigator callback
				navigationListener.moveToRow(firstDataRowId);
			}
		};
		navigationForm.add(previousPageButton);
		
		currentPageNumberLabel = new Label("currentPageId", new LoadableDetachableModel<String>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected String load() {
				
				return String.valueOf(currentPageNumber);
			}
			
		});
		navigationForm.add(currentPageNumberLabel);
		
		add(navigationForm); 
	}
	
	@Override
	public void updateNavigatorState(DataTableNavigationState navigationState, DataTableNavigationDirection navigationDirection, String firstRowId, String lastRowId) {
		
		//call parent method to update local fields
		super.updateNavigatorState(navigationState, navigationDirection, firstRowId, lastRowId);
		
		switch (navigationState) {
			case FirstPage : 
				nextPageButton.setEnabled(true);	
				previousPageButton.setEnabled(false);
			break;
			
			case LastPage : 
				nextPageButton.setEnabled(false);	
				previousPageButton.setEnabled(true);
			break;
			
			case FirstAndLastPage :
				nextPageButton.setEnabled(false);	
				previousPageButton.setEnabled(false);
			break;
				
			case NotFirstOrLast :
				nextPageButton.setEnabled(true);	
				previousPageButton.setEnabled(true);
			break;
		}
	}
}
