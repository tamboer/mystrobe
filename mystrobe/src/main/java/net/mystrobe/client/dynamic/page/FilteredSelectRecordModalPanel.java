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

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.IFilterParameter;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;


/**
 * Extends {@link SelectRecordModalPanel} and adds filtering fields
 *  on top of the displayed selection table.
 * 
 * @author TVH Group NV
 *
 * @param <T> Selectable data object type.
 */
public class FilteredSelectRecordModalPanel<T extends IDataBean> extends SelectRecordModalPanel<T>{

	private static final long serialVersionUID = -1109878506876466165L;

	protected static final String FILTER_SECTION_WEB_MARKUP_ID = "searchFilterSection";  
	protected static final String FILTER_FORM_WEB_MARKUP_ID = "filterFormId";  
	protected static final String FILTER_SEARCH_BUTTON_WEB_MARKUP_ID = "searchButtonId";  
	protected static final String FILTER_REPEATING_VIEW_WEB_MARKUP_ID = "filtersViewId";
	protected static final String FILTER_TEXTFIELD_WEB_MARKUP_ID = "filterTextFieldId";  
	protected static final String FILTER_FIELD_LABEL_WEB_MARKUP_ID = "filterLabelId";  
	
	protected Form<List<Serializable>> filterForm;
	
	protected AjaxButton searchButton;
	
	protected RepeatingView filtersView;
	
	protected List<IFilterParameter> filterParameters;
	
	protected WebMarkupContainer searchFilterContainer = null;
	
	public FilteredSelectRecordModalPanel(String panelId,
			ModalWindow modalWindow) {
		super(panelId, modalWindow);
	}
	
	public FilteredSelectRecordModalPanel(String panelId,
			ModalWindow modalWindow, IDataObject<T> dataObject) {
		super(panelId, modalWindow, dataObject);
	}
	
	public FilteredSelectRecordModalPanel(String panelId,
			ModalWindow modalWindow, IDataObject<T> dataObject, List<IFilterParameter> filterParameters) {
		super(panelId, modalWindow, dataObject);
		this.filterParameters = filterParameters;
	}
	
	@Override
	protected void initializeRecordData() {
		super.initializeRecordData();
		
		//should set filter components as desired
	}
	
	
	@Override
	public void initializePageComponents() throws WicketDSBLException {
		
		super.initializePageComponents();
		
		initializeFilterComponents();
	}

	protected void initializeFilterComponents() throws WicketDSBLException{
		
		searchFilterContainer = new WebMarkupContainer(FILTER_SECTION_WEB_MARKUP_ID); 
		add(searchFilterContainer);
		
		filterForm = new Form<List<Serializable>>(FILTER_FORM_WEB_MARKUP_ID);
		searchFilterContainer.add(filterForm);
		
		filtersView = new RepeatingView(FILTER_REPEATING_VIEW_WEB_MARKUP_ID);
		filterForm.add(filtersView);
		
		searchButton = new AjaxButton(FILTER_SEARCH_BUTTON_WEB_MARKUP_ID) {

			private static final long serialVersionUID = 2161786874603515379L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				FilteredSelectRecordModalPanel.this.dataObject.clearFilters();
				
				FilteredSelectRecordModalPanel.this.dataObject.addFilters(new HashSet<IFilterParameter>(FilteredSelectRecordModalPanel.this.filterParameters));
				FilteredSelectRecordModalPanel.this.dataObject.resetDataBuffer();
				
				target.add(FilteredSelectRecordModalPanel.this.dataTablePanel);
				target.add(FilteredSelectRecordModalPanel.this.dataTableNavigator);
				
				selectedDataChanged(target);
			
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				
			}
		};
		
		filterForm.add(searchButton);
		
		if (this.filterParameters == null || this.filterParameters.isEmpty() ) {
			
			filterForm.setRenderBodyOnly(true);
			filtersView.setRenderBodyOnly(true);
			
			searchFilterContainer.setVisible(false);
			
			return;
		}
		
		for (IFilterParameter filterParameter : this.filterParameters) {
			
			WebMarkupContainer item = new WebMarkupContainer(filtersView.newChildId());
			
			Map<IDynamicFormFieldConfig.Property, Object> configurationMap =  this.tableColumnsConfig.getColumnProperties(filterParameter.getColumn());
			String label = (String) configurationMap.get(IDynamicFormFieldConfig.Property.Label);
			
			Label filterLabel = new Label(FILTER_FIELD_LABEL_WEB_MARKUP_ID, label);
			TextField<Serializable> parameterTextField = new TextField<Serializable>(FILTER_TEXTFIELD_WEB_MARKUP_ID, new PropertyModel<Serializable>(filterParameter, "value"));
			
			item.add(filterLabel);
			item.add(parameterTextField);
			
			filtersView.add(item);
		}
		
		FilteredSelectRecordModalPanel.this.dataObject.addFilters(new HashSet<IFilterParameter>(FilteredSelectRecordModalPanel.this.filterParameters));
		FilteredSelectRecordModalPanel.this.dataObject.fetchFirst();
	}

	@Override
	protected void selectedDataChanged(AjaxRequestTarget target) {
		super.selectedDataChanged(target);
		
	}
	
	
}
