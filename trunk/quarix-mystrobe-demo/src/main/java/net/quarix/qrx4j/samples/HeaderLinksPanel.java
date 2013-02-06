package net.quarix.qrx4j.samples;

import net.quarix.qrx4.samples.ui.page.customer.CustomerInfoPage;
import net.quarix.qrx4.samples.ui.page.order.SearchOrderPage;
import net.quarix.qrx4.samples.ui.page.settings.EmployeesPage;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public abstract class HeaderLinksPanel extends Panel {

	private HeaderLink selectedLink;
	
	public HeaderLinksPanel(String id) {
		super(id);
		initPanelComponents(); 
	}
	
	protected void initPanelComponents() {
		
		setSelectedLink(getCurrentActiveLink());
		
		Form<Void> headerLinksForm = new Form<Void>("headerLinksForm");
		PropertyModel<HeaderLink> selectedLinkModel = PropertyModel.<HeaderLink>of(HeaderLinksPanel.this, "selectedLink");
		
		RadioGroup<HeaderLink> radioGroup = new RadioGroup<HeaderLink>("headerLinksGroup", selectedLinkModel);
		headerLinksForm.add(radioGroup);
		
		Radio<HeaderLink> homeChoice = new Radio<HeaderLink>("homeRadioLink", Model.of(HeaderLink.Home), radioGroup);
		homeChoice.add(new AjaxEventBehavior("click") {
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				setResponsePage(HomePage.class);
				
			}
		});
		radioGroup.add(homeChoice);
		
		Radio<HeaderLink> customerChoice = new Radio<HeaderLink>("customerRadioLink", Model.of(HeaderLink.Customers), radioGroup);
		customerChoice.add(new AjaxEventBehavior("click") {
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				setResponsePage(CustomerInfoPage.class);
				
			}
		});
		radioGroup.add(customerChoice);
		
		Radio<HeaderLink> orderChoice = new Radio<HeaderLink>("orderRadioLink", Model.of(HeaderLink.Orders), radioGroup);
		orderChoice.add(new AjaxEventBehavior("click") {
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				setResponsePage(SearchOrderPage.class);
				
			}
		});
		radioGroup.add(orderChoice);
		
//		Radio<HeaderLink> itemChoice = new Radio<HeaderLink>("itemsRadioLink", Model.of(HeaderLink.Purchase_Orders), radioGroup);
//		radioGroup.add(itemChoice);
		
		Radio<HeaderLink> settingsChoice = new Radio<HeaderLink>("settingsRadioLink", Model.of(HeaderLink.Settings), radioGroup);
		settingsChoice.add(new AjaxEventBehavior("click") {
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				setResponsePage(EmployeesPage.class);
				
			}
		});
		radioGroup.add(settingsChoice);
		
		add(headerLinksForm);
	}
	
	public HeaderLink getSelectedLink() {
		return selectedLink;
	}

	public void setSelectedLink(HeaderLink selectedLink) {
		this.selectedLink = selectedLink;
	}

	protected abstract HeaderLink getCurrentActiveLink();
}
