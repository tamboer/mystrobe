package net.quarix.qrx4.samples.ui;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.navigation.NavigationPanel;
import net.mystrobe.client.dynamic.panel.DynamicFormDataViewPanel;
import net.quarix.qrx4j.samples.BasePage;
import net.quarix.qrx4j.samples.HeaderLink;
import net.quarix.qrx4j.samples.Qrx4jSampleApplication;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.dao.CustomerDataObject;
import org.apache.wicket.Application;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;


public class DynamicForm2 extends BasePage {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public DynamicForm2(final PageParameters parameters) {
    	Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
    	CustomerDataObject customerDao = new CustomerDataObject(application.getMystrobeConfig(), application.getAppName());
    	customerDao.fetchFirst();
    	Customer customer = customerDao.getData();

    	IDynamicFormConfig<Customer> custFormConfig = new DynamicFormConfig(
    			customerDao.getSchema(), true);
    	
    	DynamicFormDataViewPanel<Customer> custForm  = new DynamicFormDataViewPanel<Customer>(
    			  "dynamicForm"
    			, customer
    			, custFormConfig );
    	
    	add(custForm);
    	ComponentLinker.bindData( customerDao, custForm, null);
    	
    	Form form = new Form("form", new Model<Customer>(customer));        
		add(form);
		
    	NavigationPanel navigationPanel = new NavigationPanel("navigationPanel", form);
        ComponentLinker.bindNavigation(navigationPanel, customerDao);
        
        form.add(navigationPanel);
    }

	@Override
	protected HeaderLink getHeaderActiveLink() {
		// TODO Auto-generated method stub
		return null;
	}
}
