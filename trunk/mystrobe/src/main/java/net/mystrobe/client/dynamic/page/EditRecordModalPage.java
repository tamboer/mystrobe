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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.IDAOMessage;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.MessageType;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.navigation.CRUDAjaxOperationsPanel;
import net.mystrobe.client.dynamic.navigation.CRUDOperationsPanel;
import net.mystrobe.client.dynamic.navigation.CRUDOperationsPanel.CRUDButton;
import net.mystrobe.client.dynamic.panel.DynamicFormDataViewPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TVH Group NV
 */
public class EditRecordModalPage<T extends IDataBean> extends WebPage {
	
	private static final Logger logger = LoggerFactory.getLogger(EditRecordModalPage.class); 

	protected DynamicFormDataViewPanel<T> dataViewPanel;
	
	protected CRUDAjaxOperationsPanel<T> crudOperationsPanel; 
	
	protected FeedbackPanel feedbackPanel;
	
	protected Form<T> form;
	
	protected ModalWindowUpdateMode windowUpdateMode = ModalWindowUpdateMode.View;
	
	protected IDataObject<T> dataObjectAdaptor;
	
	private Map<String, Object> initRecordValuesMap;

	public EditRecordModalPage(IDataObject<T> dataObjectAdaptor, final ModalWindow modalWindow, 
			IDynamicFormConfig<T> formConfig, ModalWindowUpdateMode windowUpdateMode, Map<String, Object> initRecordValuesMap) {
		
		super();
		
		this.dataObjectAdaptor = dataObjectAdaptor;
		this.windowUpdateMode = windowUpdateMode;
		this.initRecordValuesMap = initRecordValuesMap;
		
		feedbackPanel = new FeedbackPanel("feedbackpanel");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		T dataBean =  this.dataObjectAdaptor.getData();
		
		if (dataBean == null) {
			try {
				dataBean = dataObjectAdaptor.getSchema().getIDataTypeClass().newInstance();
			} catch (InstantiationException e) {
				logger.error("Can not instantiate bean class.", e);
			} catch (IllegalAccessException e) {
				logger.error("Can not instantiate bean class.", e);
			}
		}
		
		IModel<T> dataBeanModel = new Model<T>(dataBean);
		form = new Form<T>("updateDataFormId", dataBeanModel);
		add(form);
		
		dataViewPanel = new DynamicFormDataViewPanel<T>("formFieldsPanelId", this.dataObjectAdaptor.getData(), formConfig);
		ComponentLinker.bindUpdate(dataViewPanel, this.dataObjectAdaptor );
		ComponentLinker.bindState(dataViewPanel, this.dataObjectAdaptor );
		ComponentLinker.bindData(this.dataObjectAdaptor, dataViewPanel, null);
		form.add(dataViewPanel);
		
		Set<CRUDButton> buttons = ModalWindowUpdateMode.View.equals(windowUpdateMode) ? null : CRUDOperationsPanel.EDIT;
		
		crudOperationsPanel = new CRUDAjaxOperationsPanel<T>("crudPanelId", form, buttons) {
			private static final long serialVersionUID = 960653709569043187L;

			@Override
			protected void onCancel(AjaxRequestTarget target) {
				super.onCancel(target);
				modalWindow.close(target);
			}
			
			@Override
			protected void onSaveError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
				target.add(dataViewPanel);
			}
			
			@Override
			protected void onSave(AjaxRequestTarget target) {
				super.onSave(target);
				
				Collection<IDAOMessage> errorMessages = EditRecordModalPage.this.dataObjectAdaptor.getMessages(MessageType.Error); 
				if ( errorMessages == null || errorMessages.isEmpty()) {
					if (ModalWindowUpdateMode.Add.equals(EditRecordModalPage.this.windowUpdateMode)) {
						EditRecordModalPage.this.dataObjectAdaptor.resetDataTableNavigation(null, 10);
					}
					modalWindow.close(target);
				} else {
					target.add(feedbackPanel);
					target.add(dataViewPanel);
				}
			}
			
			@Override
			protected void onReset(AjaxRequestTarget target) {
				super.onReset(target);
				
				//reset data bean
				if (ModalWindowUpdateMode.Add.equals(EditRecordModalPage.this.windowUpdateMode)) {
					EditRecordModalPage.this.dataViewPanel.resetRecord(EditRecordModalPage.this.initRecordValuesMap);
				} else {
					EditRecordModalPage.this.dataViewPanel.resetRecord();
				}
				
				target.add(dataViewPanel);
			}
		};
		ComponentLinker.bindUpdateUI(crudOperationsPanel, dataViewPanel);
        ComponentLinker.bindState(dataViewPanel, crudOperationsPanel);
	    
        crudOperationsPanel.setVisible(!ModalWindowUpdateMode.View.equals(windowUpdateMode));
        
	    form.add(crudOperationsPanel);
		
	    if (!this.windowUpdateMode.equals(ModalWindowUpdateMode.View)) {
	    	dataViewPanel.enableFormInputFields();
	    }
	    
	    if (ModalWindowUpdateMode.Add.equals(windowUpdateMode)) {
	    	dataViewPanel.addRecord(initRecordValuesMap);
	    } else if (ModalWindowUpdateMode.Edit.equals(windowUpdateMode)) {
	    	dataViewPanel.editRecord();
	    }
	}
	
//	@Override
//    public void renderHead(IHeaderResponse response) {
//        response.renderCSSReference("http://sc.tvh.com/css/tvh-webapp.css");
//    }
	
}
