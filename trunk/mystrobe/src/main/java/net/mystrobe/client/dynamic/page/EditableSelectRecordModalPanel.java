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
import java.util.List;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.IDAOMessage;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.IFilterParameter;
import net.mystrobe.client.MessageType;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.navigation.CRUDAjaxOperationsPanel;
import net.mystrobe.client.dynamic.navigation.CRUDOperationsPanel;
import net.mystrobe.client.dynamic.panel.DynamicFormDataViewPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TVH Group NV
 */
public class EditableSelectRecordModalPanel<T extends IDataBean> extends FilteredSelectRecordModalPanel<T> {

	private static final long serialVersionUID = 4906685294193149877L;

	private static final Logger logger = LoggerFactory.getLogger(EditableSelectRecordModalPanel.class);
	
	protected EditPanel editPanel;
	
	protected IDynamicFormConfig<T> editableFieldsConfig;
	
	public EditableSelectRecordModalPanel(String panelId,
			ModalWindow modalWindow) {
		super(panelId, modalWindow);
		
	}
	
	public EditableSelectRecordModalPanel(String panelId, ModalWindow modalWindow,
			IDataObject<T> dataObject) {
		super(panelId, modalWindow, dataObject);
	}
	
	public EditableSelectRecordModalPanel(String panelId, ModalWindow modalWindow,
			IDataObject<T> dataObject, List<IFilterParameter> filterParameters, IDynamicFormConfig<T> editFieldsConfig) {
		super(panelId, modalWindow, dataObject, filterParameters);
		this.editableFieldsConfig = editFieldsConfig;
	}
	
	@Override
	protected void initializeRecordData() {
		super.initializeRecordData();
		
		//set edit fields configuration
	}
	
	@Override
	public void initializePageComponents() throws WicketDSBLException {
		
		super.initializePageComponents();
		
		initializeEditComponents();
	}
	
	
	protected void initializeEditComponents()throws  WicketDSBLException {
		
		editPanel = new EditPanel("editPanel", this.editableFieldsConfig);
		editPanel.setVisible(true);
		EditableSelectRecordModalPanel.this.replaceWith(editPanel);
		
		this.dataObject.resetDataBuffer();
	}

	@Override
	protected void selectedDataChanged(AjaxRequestTarget target) {
		super.selectedDataChanged(target);
		editPanel.dataChanged(target);
	}
	
	
	protected class EditPanel extends Panel {

		protected static final String EDIT_FORM_WEB_MARKUP_ID = "formId";  
		protected static final String EDIT_FORM_FIELDS_WEB_MARKUP_ID = "formFieldsId";  
		protected static final String EDIT_FEEDBACK_PANEL_WEB_MARKUP_ID = "feedbackPanelId";  
		protected static final String EDIT_OPERATIONS_PANEL_WEB_MARKUP_ID = "opertaionsPanelId";  
		
		protected DynamicFormDataViewPanel<T> editRecordFormFields;
		
		protected CRUDAjaxOperationsPanel<T> operationsPanel;
		
		protected IDynamicFormConfig<T> editFieldsConfig;
		
		protected FeedbackPanel feedbackPanel;
		
		protected Form<T> editRecordForm;
		
		public EditPanel(String id, IDynamicFormConfig<T> editFieldsConfig) {
			super(id);
			this.editFieldsConfig = editFieldsConfig;
			
			if (this.editFieldsConfig == null) {
				this.editFieldsConfig = EditableSelectRecordModalPanel.this.tableColumnsConfig;
			}
			
			T dataBean = EditableSelectRecordModalPanel.this.dataObject.getData();
			if (dataBean == null) {
				try {
					dataBean = EditableSelectRecordModalPanel.this.dataObject.getSchema().getIDataTypeClass().newInstance();
				} catch (InstantiationException e) {
					logger.error("Can not instantiate bean class " + EditableSelectRecordModalPanel.this.dataObject.getSchema().getIDataTypeClass().getName()  ,e);
				} catch (IllegalAccessException e) {
					logger.error("Can not instantiate bean class " + EditableSelectRecordModalPanel.this.dataObject.getSchema().getIDataTypeClass().getName()  ,e);
				}
			}
			
			this.feedbackPanel = new FeedbackPanel(EDIT_FEEDBACK_PANEL_WEB_MARKUP_ID );
			this.feedbackPanel.setOutputMarkupId(true);
			add(feedbackPanel);
			
			this.editRecordForm = new Form<T>(EDIT_FORM_WEB_MARKUP_ID, Model.of(dataBean));
			add(this.editRecordForm);
			
			this.editRecordFormFields = new DynamicFormDataViewPanel<T>(EDIT_FORM_FIELDS_WEB_MARKUP_ID, dataBean, this.editFieldsConfig);
			ComponentLinker.bindUpdate(this.editRecordFormFields, EditableSelectRecordModalPanel.this.dataObject );
			ComponentLinker.bindData(EditableSelectRecordModalPanel.this.dataObject , this.editRecordFormFields, null);
			
			this.editRecordForm.add(this.editRecordFormFields);
			
			
			this.operationsPanel = new CRUDAjaxOperationsPanel<T>(EDIT_OPERATIONS_PANEL_WEB_MARKUP_ID, this.editRecordForm, CRUDOperationsPanel.ALL ){

				private static final long serialVersionUID = -4877968117584483422L;
				
				@Override
				protected void onCancel(AjaxRequestTarget target) {
					super.onCancel(target);
					target.add(EditPanel.this.editRecordFormFields);
					target.add(this);
				}
				
				@Override
				protected void onEdit(AjaxRequestTarget target) {
					super.onEdit(target);
					target.add(EditPanel.this.editRecordFormFields);
					target.add(this);
				}

				@Override
				protected void onAdd(AjaxRequestTarget target) {
					super.onAdd(target);
					target.add(EditPanel.this.editRecordFormFields);
					target.add(this);
				}
				
				@Override
				protected void onSaveError(AjaxRequestTarget target, Form<?> form) {
					target.add(EditPanel.this.editRecordFormFields);
					target.add(EditPanel.this.feedbackPanel);
				}
				
				@Override
				protected void onSave(AjaxRequestTarget target) {
					super.onSave(target);
					
					Collection<IDAOMessage> errorMessages = EditableSelectRecordModalPanel.this.dataObject.getMessages(MessageType.Error); 
					if ( errorMessages == null || errorMessages.isEmpty()) {
						EditPanel.this.editRecordFormFields.disableFormInputFields();
					} else {
						target.add(EditPanel.this.feedbackPanel);
					}
					target.add(EditPanel.this.editRecordFormFields);
					target.add(EditableSelectRecordModalPanel.this.dataTablePanel);
					target.add(this);
				}
				
				@Override
				protected void onReset(AjaxRequestTarget target) {
					super.onReset(target);
					
					EditPanel.this.editRecordFormFields.resetRecord();
					
					target.add(EditPanel.this.editRecordFormFields);
					target.add(EditableSelectRecordModalPanel.this.dataTablePanel);
				}
			};

			ComponentLinker.bindUpdateUI(this.operationsPanel, this.editRecordFormFields);
	        ComponentLinker.bindState(this.editRecordFormFields, this.operationsPanel);
	        ComponentLinker.bindState(EditableSelectRecordModalPanel.this.dataObject, this.operationsPanel);
	        
	        this.editRecordForm.add(this.operationsPanel);
	    }
		
		public void dataChanged(AjaxRequestTarget target) {

			this.editRecordFormFields.cancelRecord();

			target.add(this.operationsPanel);
			target.add(this.editRecordFormFields);
			target.add(this.feedbackPanel);
		}
		
	}
}
