package net.quarix.qrx4j.samples;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
    	
	}

	@Override
	protected HeaderLink getHeaderActiveLink() {
		return HeaderLink.Home;
	}
}
