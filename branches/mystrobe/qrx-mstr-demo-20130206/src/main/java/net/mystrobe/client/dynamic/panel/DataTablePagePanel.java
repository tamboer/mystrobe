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
 package net.mystrobe.client.dynamic.panel;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.DataObjectAdaptor;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.navigation.CRUDAjaxOperationsPanel;
import net.mystrobe.client.dynamic.navigation.CRUDOperationsPanel;
import net.mystrobe.client.dynamic.navigation.DataTableNavigationPanel;
import net.mystrobe.client.dynamic.page.EditRecordModalPageCreator;
import net.mystrobe.client.dynamic.page.ModalWindowUpdateMode;
import net.mystrobe.client.dynamic.table.view.SimpleDataTableViewPanel;
import net.mystrobe.client.error.DefaultErrorHandler;
import net.mystrobe.client.ui.UICssResourceReference;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author TVH Group NV
 */
public class DataTablePagePanel<T extends IDataBean> extends Panel {

	private static final long serialVersionUID = -5313048120511317276L;
	
	protected final static Logger logger = LoggerFactory.getLogger(DataTablePagePanel.class);

	protected SimpleDataTableViewPanel<T> simpleDataTableViewPanel;
   
	protected CRUDAjaxOperationsPanel<T> operationsPanel; 
   
	protected FeedbackPanel feedback;
	
	protected DynamicFormDataViewPanel<T> dynamicPersonFormPanel;
	
	protected boolean isEditOperation = true;
	
	protected int tableDataSize = 20;
	
	private DataObjectAdaptor<T> dataObjectAdaptor;
	
	private DataTableNavigationPanel<T> dataTableNavigationPanel;
	
	private EditRecordModalPageCreator<T> editRecordModalPageCreator;
	
	private ModalWindow editRecordModalWindow;
	
	/**
	 * Data table page, navigation and edit record modal window panel.  
	 * 
	 * @param id Data page panel id.
	 * @param dataObject Data object to use for all operations. 
	 * @param dataBeanClass Data bean class.
	 * @param tableColumnsConfig Data table configuration.
	 * @param tableDataSize Data table size. 
	 * @param navigationVisibleFieldsConfig Data fields displayed on top of the data table. 
	 * @param dynamicFormFieldsConfig Edit record form visible fields.
	 * 
	 * @throws IllegalAccessException Data bean class constructor not accessible. 
	 * @throws InstantiationException Can not instantiate data bean.
	 */
	public DataTablePagePanel(String id, DataObjectAdaptor<T> dataObject, Class<T> dataBeanClass, IDynamicFormConfig<T> tableColumnsConfig, int tableDataSize,
			IDynamicFormConfig<T> navigationVisibleFieldsConfig, final IDynamicFormConfig<T> dynamicFormFieldsConfig ) throws IllegalAccessException, InstantiationException {
		
		super(id);
		this.dataObjectAdaptor = dataObject;
		this.tableDataSize = tableDataSize;
		
		initialize();
		
		feedback = new FeedbackPanel("feedbackpanel");
		feedback.setOutputMarkupId(true);
		add( feedback );
		
		dynamicPersonFormPanel  = new DynamicFormDataViewPanel<T>("navigationVisibleFieldsId", dataBeanClass.newInstance(), navigationVisibleFieldsConfig);
        ComponentLinker.bindUpdate(dynamicPersonFormPanel, dataObjectAdaptor);
        ComponentLinker.bindData(dataObjectAdaptor, dynamicPersonFormPanel, null);
        
        add(dynamicPersonFormPanel);
		
		simpleDataTableViewPanel = new SimpleDataTableViewPanel<T>("dataTableId", tableColumnsConfig, tableDataSize) {
		
			private static final long serialVersionUID = -3689021114533748379L;

			@Override
			protected void onDataRowClick(AjaxRequestTarget target){
				target.add(dynamicPersonFormPanel);
			}
		};
        ComponentLinker.bindDataTableData(dataObject, simpleDataTableViewPanel);
        
        dataTableNavigationPanel = new DataTableNavigationPanel<T>("dataTableNavigationId", this.tableDataSize);
        ComponentLinker.bindDataTableNavigation(dataTableNavigationPanel, dataObject);
        
		add(dataTableNavigationPanel);
		add(simpleDataTableViewPanel);
        
        editRecordModalWindow = new ModalWindow("editRecordModalWindow");
        add(editRecordModalWindow);

        editRecordModalWindow.setCookieName("editRecord");

        editRecordModalPageCreator = new EditRecordModalPageCreator<T>(editRecordModalWindow, dataObjectAdaptor, tableColumnsConfig); 
        editRecordModalWindow.setPageCreator(editRecordModalPageCreator);
        
        editRecordModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
        {
            
			private static final long serialVersionUID = -3973669282678474350L;

			public void onClose(AjaxRequestTarget target)
            {
                target.add(simpleDataTableViewPanel);
                target.add(dynamicPersonFormPanel);
            }
        });
        
        editRecordModalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
        {
            
			private static final long serialVersionUID = 7167060234810101666L;

			public boolean onCloseButtonClicked(AjaxRequestTarget target)
            {
				dataObjectAdaptor.cancelCRUDOpertaion();
            	target.add(dynamicPersonFormPanel);
            	return true;
            }
        });

        IModel<T> dataBeanModel = new Model<T>(this.dataObjectAdaptor.getData());
        Form<T> form = new Form<T>("operationsFormId", dataBeanModel);
        add(form);
        
        operationsPanel = new CRUDAjaxOperationsPanel<T>("operationsPanelId", form, CRUDOperationsPanel.CRUD) {
        	
			private static final long serialVersionUID = 1536418446385212137L;

			@Override
			public void onEdit(AjaxRequestTarget target)
			{
				editRecordModalPageCreator.setWindowUpdateMode(ModalWindowUpdateMode.Edit);
				editRecordModalWindow.show(target);
			}
  
		    @Override
		    public void onAdd(AjaxRequestTarget target)
		    {
		    	editRecordModalPageCreator.setWindowUpdateMode(ModalWindowUpdateMode.Add);
		    	editRecordModalWindow.show(target);
		    }
		      
			@Override
			public void onDelete(AjaxRequestTarget target)
			{
				try {
					dataObjectAdaptor.deleteData();
					DataTablePagePanel.this.dataObjectAdaptor.resetDataTableNavigation(dataTableNavigationPanel, DataTablePagePanel.this.tableDataSize);
				  	
					target.add(simpleDataTableViewPanel);
				  	target.add(dynamicPersonFormPanel);
				} catch (WicketDSBLException e) {
					//when errors
					DefaultErrorHandler.handleErrors(dataObjectAdaptor, this, null);
					target.add(feedback);
				}
			}
        };
        ComponentLinker.bindState(dataObjectAdaptor, operationsPanel);
        form.add(operationsPanel);
        
        dataObject.resetDataBuffer();
		
	}
	
	protected void initialize() {
		
	}
	
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(UICssResourceReference.get()));
	}
	
	

}
