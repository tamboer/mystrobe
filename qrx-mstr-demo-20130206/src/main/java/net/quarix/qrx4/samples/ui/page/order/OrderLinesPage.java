package net.quarix.qrx4.samples.ui.page.order;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class OrderLinesPage extends OrderBasePage {

	public OrderLinesPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected OrderTab getCurrentActiveTab() {
		return null;// OrderTab.Lines;
	}

}
