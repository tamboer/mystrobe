package net.quarix.qrx4.samples.ui.page.order;

import net.quarix.qrx4j.samples.BasePage;
import net.quarix.qrx4j.samples.HeaderLink;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class OrderBasePage extends BasePage {

	
	public enum OrderTab {
		Search,
		Edit,
		Edit_Lines,
		Finish;
	}
	
	private OrderTab activeTab = OrderTab.Search;
	
	public OrderBasePage(PageParameters parameters) {
		initTabs();
	}
	
	protected void initTabs() {
			
		activeTab = getCurrentActiveTab();
		PropertyModel<OrderTab> tabModel = PropertyModel.<OrderTab>of(this, "activeTab");
		
		AjaxLink<OrderTab> searchTab = new AjaxLink<OrderTab>("searchTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				onSearchTab(target);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				tag.put("href", "#tabs-1");
			}
		};
		add(searchTab);
		
		AjaxLink<OrderTab> editTab = new AjaxLink<OrderTab>("editTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				onEditTab(target);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("href", "#tabs-2");
			}
		};
		add(editTab);
		
		
		AjaxLink<OrderTab> linesTab = new AjaxLink<OrderTab>("linesTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				onEditLinesTab(target);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("href", "#tabs-3");
			}
		};
		add(linesTab);
		
		AjaxLink<OrderTab> finishTab = new AjaxLink<OrderTab>("finishTab", tabModel) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				onFinishTab(target);
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("href", "#tabs-4");
			}
		};
		add(finishTab);
	}

	public OrderTab getActiveTab() {
		return activeTab;
	}


	public void setActiveTab(OrderTab activeTab) {
		this.activeTab = activeTab;
	}

	
	protected HeaderLink getHeaderActiveLink(){
		return HeaderLink.Orders;
	}
	
	protected abstract OrderTab getCurrentActiveTab(); 
	
	
	protected void onEditTab(AjaxRequestTarget target) {
		
	}

	protected void onEditLinesTab(AjaxRequestTarget target) {
		
	}
	
	protected void onFinishTab(AjaxRequestTarget target) {
		
	}
	
	protected void onSearchTab(AjaxRequestTarget target) {
		setResponsePage(SearchOrderPage.class);
	}
}
