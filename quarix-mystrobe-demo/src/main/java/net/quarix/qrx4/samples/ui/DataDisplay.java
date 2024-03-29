package net.quarix.qrx4.samples.ui;

import net.quarix.qrx4j.samples.BasePage;
import net.quarix.qrx4j.samples.HeaderLink;
import net.quarix.qrx4j.samples.Qrx4jSampleApplication;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.dao.CustomerDataObject;
import org.apache.wicket.Application;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class DataDisplay extends BasePage {
	private static final long serialVersionUID = 1L;
	
    public DataDisplay(final PageParameters parameters) {
    	Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
    	CustomerDataObject customerDao = new CustomerDataObject(application.getMystrobeConfig(), application.getAppName());
    	
    	add(new Label("custnum"));
    	add(new Label("name"));
    	add(new Label("phone"));
    	
    	customerDao.fetchFirst();
    	Customer customer = customerDao.getData();
    	
    	setDefaultModel(new CompoundPropertyModel<Customer>(customer));
    }

	@Override
	protected HeaderLink getHeaderActiveLink() {
		// TODO Auto-generated method stub
		return null;
	}
}
