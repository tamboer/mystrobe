package net.quarix.qrx4.samples.ui.page.customer;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class EditCustomerPage extends CustomerBasePage{

	public EditCustomerPage(PageParameters parameters) {
		super(parameters);
		
	}

	@Override
	protected CustomerTab getCurrentActiveTab() {
		return CustomerTab.Edit;
	}

}
