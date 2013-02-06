package net.quarix.qrx4j.samples;

import org.apache.wicket.markup.html.link.Link;

public class CommonPage extends BasePage{
	
	public CommonPage() {
		
		add(new Link<Void>("homePageLink"){

			@Override
			public void onClick() {
				setResponsePage(HomePage.class);  
			}
			
		});
	}

	@Override
	protected HeaderLink getHeaderActiveLink() {
		// TODO Auto-generated method stub
		return null;
	}
}
