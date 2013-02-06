package net.quarix.qrx4.samples.ui.panel;

import net.quarix.qrx4.samples.ui.page.customer.CustomerInfoPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public abstract class CustomerTabsPanel extends Panel {

	public enum CustomerTab {
		Search,
		Edit,
		Orders;
	}
	
	private CustomerTab activeTab = CustomerTab.Search;
	
	public CustomerTabsPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
		initPanelComponents();
	}

	
	protected void initPanelComponents() {
		
		activeTab = getCurrentActiveTab();
		PropertyModel<CustomerTab> tabModel = PropertyModel.<CustomerTab>of(this, "activeTab");
		
		AjaxLink<CustomerTab> searchTab = new AjaxLink<CustomerTabsPanel.CustomerTab>("searchTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(CustomerInfoPage.class);
			}
		};
		add(searchTab);
		
		AjaxLink<CustomerTab> editTab = new AjaxLink<CustomerTabsPanel.CustomerTab>("editTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(CustomerInfoPage.class);
			}
		};
		add(editTab);
		
		AjaxLink<CustomerTab> ordersTab = new AjaxLink<CustomerTabsPanel.CustomerTab>("ordersTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(CustomerInfoPage.class);
			}
		};
		add(ordersTab);
	}

	public CustomerTab getActiveTab() {
		return activeTab;
	}


	public void setActiveTab(CustomerTab activeTab) {
		this.activeTab = activeTab;
	}
	
	protected abstract CustomerTab getCurrentActiveTab();
}
