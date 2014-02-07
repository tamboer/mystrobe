package net.quarix.qrx4.samples.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.IDAOMessage;
import net.mystrobe.client.MessageType;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig;
import net.mystrobe.client.dynamic.navigation.CRUDAjaxOperationsPanel;
import net.mystrobe.client.dynamic.navigation.CRUDOperationsPanel.CRUDButton;
import net.mystrobe.client.dynamic.navigation.NavigationPanel;
import net.mystrobe.client.dynamic.panel.DynamicFormDataViewPanel;
import net.quarix.qrx4j.samples.BasePage;
import net.quarix.qrx4j.samples.HeaderLink;
import net.quarix.qrx4j.samples.Qrx4jSampleApplication;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.beans.meta.CustomerSchema;
import net.quarix.qrx4j.samples.data.dao.CustomerDataObject;
import org.apache.wicket.Application;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

public class DynamicFormCRUD extends BasePage {

	
	
	protected DynamicFormDataViewPanel<Customer> custForm;
	
	protected NavigationPanel navigationPanel;
	
	protected CRUDAjaxOperationsPanel<Customer> crudOperations;
	
	protected FeedbackPanel feedbackPanel;
	
	protected CustomerDataObject customerDao;
	
	public DynamicFormCRUD () { 
                Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
		customerDao = new CustomerDataObject(application.getMystrobeConfig(), application.getAppName());
		
		feedbackPanel = new FeedbackPanel("feedbackPanelId");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		Form form = new Form("formId", Model.of(new Customer())); 
		
		navigationPanel = new NavigationPanel("navigationPanelId", form);
		ComponentLinker.bindNavigation(navigationPanel, customerDao);
		form.add(navigationPanel);
		
		IDynamicFormConfig<Customer> custFormConfig = new DynamicFormConfig<Customer>(
				customerDao.getSchema(), false);
		custFormConfig.setColumnProperty(CustomerSchema.Cols.CUSTNUM, IDynamicFormFieldConfig.Property.Required, Boolean.TRUE);
		
		custForm = new DynamicFormDataViewPanel<Customer>(
				  "dynamicFormId"
				, new Customer()
				, custFormConfig );
		
		form.add(custForm);
		ComponentLinker.bindData( customerDao, custForm, null);
		ComponentLinker.bindUpdate(custForm, customerDao);
		
		crudOperations = new CRUDAjaxOperationsPanel<Customer>("operationsPanelId",
				form, new HashSet<CRUDButton>(Arrays.asList(new CRUDButton [] {CRUDButton.Edit, CRUDButton.Save, 
		    			CRUDButton.Cancel, CRUDButton.Reset}))){

			private static final long serialVersionUID = -5203307062178874059L;

			@Override
			protected void onCancel(AjaxRequestTarget target) {
				super.onCancel(target);
				target.add(custForm);
				target.add(navigationPanel);
				target.add(crudOperations);
			}

			@Override
			protected void onEdit(AjaxRequestTarget target) {
				super.onEdit(target);
				target.add(custForm);
				target.add(navigationPanel);
				target.add(crudOperations);
			}

			@Override
			protected void onReset(AjaxRequestTarget target) {
				super.onReset(target);
				target.add(custForm);
			}

			@Override
			protected void onSave(AjaxRequestTarget target) {
				super.onSave(target);
				
				Collection<IDAOMessage> errorMessages = customerDao.getMessages(MessageType.Error); 
				if ( errorMessages != null && !errorMessages.isEmpty()) {
					target.add(feedbackPanel);
					target.add(custForm);
				} else {
					target.add(custForm);
					target.add(navigationPanel);
					target.add(crudOperations);
				}
			}
		};
		ComponentLinker.bindUpdateUI(crudOperations, custForm);
		ComponentLinker.bindState(customerDao, crudOperations);
		
		ComponentLinker.bindState(custForm, navigationPanel);
		//ComponentLinker.bindState(custForm, crudOperations);
		
		form.add(crudOperations);
		
		add(form);
		
		customerDao.fetchFirst();
	}

	@Override
	protected HeaderLink getHeaderActiveLink() {
		// TODO Auto-generated method stub
		return null;
	}

}
