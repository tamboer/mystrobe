package net.quarix.qrx4.samples.ui;

import java.util.Set;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.CursorStates;
import net.mystrobe.client.DataLinkParameters;
import net.mystrobe.client.IDataListener;
import net.mystrobe.client.IDataSource;
import net.mystrobe.client.dynamic.navigation.NavigationPanel;
import net.quarix.qrx4j.samples.AppConnector;
import net.quarix.qrx4j.samples.BasePage;
import net.quarix.qrx4j.samples.HeaderLink;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.dao.CustomerDataObject;


import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;


public class DynamicForm3 extends BasePage implements IDataListener<Customer>{
	private static final long serialVersionUID = 1L;
	
	protected IDataSource<Customer> dataSource = null;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public DynamicForm3(final PageParameters parameters) {
    	
    	CustomerDataObject customerDao = new CustomerDataObject();
    	customerDao.setAppConnector(AppConnector.getInstance());
    	
    	add(new Label("custnum"));
    	add(new Label("name"));
    	add(new Label("phone"));
    	
    	// position the cursor on the first record
    	customerDao.fetchFirst();
    	Customer customer = customerDao.getData();
    	
    	setDefaultModel(new CompoundPropertyModel<Customer>(customer));
    	// make the data object the default data source for this page
    	ComponentLinker.bindData(customerDao, this, null);
    	
    	// create and add the navigation form
		Form form = new Form("form", new Model<Customer>(customer));        
		add(form);
			
    	NavigationPanel navigationPanel = new NavigationPanel("navigationPanel", form);
    	form.add(navigationPanel);
    	
    	// make the navigation panel a navigator for the data object
        ComponentLinker.bindNavigation(navigationPanel, customerDao);                        
    }

	/**
	 * Every time a new record is selected in the default data object
	 * will be called with the new record
	 */
	public void dataAvailable(Customer modelInstance) {
		setDefaultModelObject(modelInstance);
	}

	public IDataSource<Customer> getDataSource() {
		return dataSource;
	}

	public void setDataSource(IDataSource<Customer> dataSource) {
		this.dataSource = dataSource;
	}

	public Set<DataLinkParameters> getDataLinkParameters() {return null;}

	public void setDataLinkParameters(Set<DataLinkParameters> dataLinkParametersSet) {}

	public void addDataLinkParameter(DataLinkParameters dataLinkParameter) {}

	public void clearDataLinkParameters() {
		// TODO Auto-generated method stub
		
	}

	public void dataAvailable(Customer modelInstance, CursorStates cursorState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HeaderLink getHeaderActiveLink() {
		// TODO Auto-generated method stub
		return null;
	}

   
}
