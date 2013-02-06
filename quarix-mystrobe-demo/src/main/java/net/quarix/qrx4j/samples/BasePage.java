package net.quarix.qrx4j.samples;

import net.mystrobe.client.ajax.indicator.AjaxBusyIndicator;
import net.mystrobe.client.ui.UICssResourceReference;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public abstract class BasePage extends WebPage {
	
	public BasePage() {
		
		add(new HeaderLinksPanel("headerLinksPanel") {

			@Override
			protected HeaderLink getCurrentActiveLink() {
				return getHeaderActiveLink();
			}
			
		});
		
		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		
		add(new AjaxBusyIndicator("busyIndicator"));
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(CssHeaderItem.forReference(UICssResourceReference.get()));
	}
	
	
	protected abstract HeaderLink getHeaderActiveLink(); 
	
}
