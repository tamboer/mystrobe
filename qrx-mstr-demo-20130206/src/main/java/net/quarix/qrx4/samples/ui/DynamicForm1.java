package net.quarix.qrx4.samples.ui;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.panel.DynamicFormDataViewPanel;
import net.quarix.qrx4j.samples.AppConnector;
import net.quarix.qrx4j.samples.BasePage;
import net.quarix.qrx4j.samples.HeaderLink;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.dao.CustomerDataObject;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class DynamicForm1 extends BasePage {
	private static final long serialVersionUID = 1L;
	
    public DynamicForm1(final PageParameters parameters) {
    	
    	CustomerDataObject customerDao = new CustomerDataObject();
    	customerDao.setAppConnector(AppConnector.getInstance());
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
    }

	@Override
	protected HeaderLink getHeaderActiveLink() {
		// TODO Auto-generated method stub
		return null;
	}
}
