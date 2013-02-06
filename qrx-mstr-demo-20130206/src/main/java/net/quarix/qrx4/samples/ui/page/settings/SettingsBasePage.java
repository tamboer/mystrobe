package net.quarix.qrx4.samples.ui.page.settings;

import net.quarix.qrx4j.samples.BasePage;
import net.quarix.qrx4j.samples.HeaderLink;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class SettingsBasePage extends BasePage {

	public enum SettingsTab {
		Employees,
		Items,
		States;
	}
	
	private SettingsTab  activeTab = SettingsTab.Employees;
	
	public SettingsBasePage(PageParameters parameters) {
		initTabs();
	}
	
	protected void initTabs() {
			
		activeTab = getCurrentActiveTab();
		PropertyModel<SettingsTab > tabModel = PropertyModel.<SettingsTab >of(this, "activeTab");
		
		AjaxLink<SettingsTab> employeesTab = new AjaxLink<SettingsTab >("employeesTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(EmployeesPage.class);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				tag.put("href", "#tabs-1");
			}
		};
		add(employeesTab);
		
		AjaxLink<SettingsTab> itemsTab = new AjaxLink<SettingsTab>("itemsTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(ItemsPage.class);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				tag.put("href", "#tabs-2");
			}
		};
		add(itemsTab);
		
//		AjaxLink<SettingsTab> salesRepTab = new AjaxLink<SettingsTab>("statesTab", tabModel) {
//
//			@Override
//			public void onClick(AjaxRequestTarget target) {
//				setResponsePage(StatesPage.class);
//			}
//			
//			@Override
//			protected void onComponentTag(ComponentTag tag) {
//				super.onComponentTag(tag);
//				
//				tag.put("href", "#tabs-3");
//			}
//		};
//		add(salesRepTab);
	}

	public SettingsTab  getActiveTab() {
		return activeTab;
	}


	public void setActiveTab(SettingsTab  activeTab) {
		this.activeTab = activeTab;
	}

	
	@Override
	protected HeaderLink getHeaderActiveLink() {
		return HeaderLink.Settings;
	}
	
	protected abstract SettingsTab getCurrentActiveTab(); 
}
