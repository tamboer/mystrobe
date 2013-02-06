package net.quarix.qrx4.samples.ui.page.customer;

import net.quarix.qrx4j.samples.BasePage;
import net.quarix.qrx4j.samples.HeaderLink;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class CustomerBasePage extends BasePage {

	public enum CustomerTab {
		Search,
		Edit,
		Orders;
	}
	
	private CustomerTab activeTab = CustomerTab.Search;
	
	public CustomerBasePage(PageParameters parameters) {
		
		initTabs();
	}
	
	protected void initTabs() {
			
		activeTab = getCurrentActiveTab();
		PropertyModel<CustomerTab> tabModel = PropertyModel.<CustomerTab>of(this, "activeTab");
		
		AjaxLink<CustomerTab> searchTab = new AjaxLink<CustomerTab>("searchTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(CustomerInfoPage.class);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				tag.put("href", "#tabs-1");
			}
		};
		add(searchTab);
		
		/*
		AjaxLink<CustomerTab> editTab = new AjaxLink<CustomerTab>("editTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(EditCustomerPage.class);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				tag.put("href", "#tabs-2");
			}
		};
		add(editTab);
		
		AjaxLink<CustomerTab> ordersTab = new AjaxLink<CustomerTab>("ordersTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(CustomerInfoPage.class);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				tag.put("href", "#tabs-3");
			}
		};
		add(ordersTab);
		*/
	}

	public CustomerTab getActiveTab() {
		return activeTab;
	}


	public void setActiveTab(CustomerTab activeTab) {
		this.activeTab = activeTab;
	}

	
	@Override
	protected HeaderLink getHeaderActiveLink() {
		return HeaderLink.Customers;
	}
	
	protected abstract CustomerTab getCurrentActiveTab(); 

}
