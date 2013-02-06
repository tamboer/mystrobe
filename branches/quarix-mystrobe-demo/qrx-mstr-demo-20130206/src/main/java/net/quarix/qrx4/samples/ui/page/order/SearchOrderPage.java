package net.quarix.qrx4.samples.ui.page.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.DataLinkParameters;
import net.mystrobe.client.FilterOperator;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.connector.transaction.DSTransactionManager;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig.Property;
import net.mystrobe.client.dynamic.navigation.DataTablePagesNavigationPanel;
import net.mystrobe.client.dynamic.page.ModalWindowUpdateMode;
import net.mystrobe.client.dynamic.panel.EditRecordModalPanel;
import net.mystrobe.client.dynamic.table.view.DataRowActionsToolbarColumn;
import net.mystrobe.client.dynamic.table.view.DataRowActionsToolbarColumn.DataRecordAction;
import net.mystrobe.client.dynamic.table.view.DataTableColumn;
import net.mystrobe.client.dynamic.table.view.IMyStrobeColumn;
import net.mystrobe.client.dynamic.table.view.SimpleDataTableViewPanel;
import net.mystrobe.client.filter.SearchFilter;
import net.mystrobe.client.impl.FilterParameter;
import net.mystrobe.client.util.StringUtil;
import net.quarix.qrx4.samples.ui.panel.LookupFormComponent;
import net.quarix.qrx4j.samples.AppConnector;
import net.quarix.qrx4j.samples.HeaderLink;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.beans.Order;
import net.quarix.qrx4j.samples.data.beans.OrderLine;
import net.quarix.qrx4j.samples.data.beans.SalesRep;
import net.quarix.qrx4j.samples.data.beans.meta.CustomerSchema;
import net.quarix.qrx4j.samples.data.beans.meta.OrderInfoDSSchema;
import net.quarix.qrx4j.samples.data.beans.meta.OrderLineSchema;
import net.quarix.qrx4j.samples.data.beans.meta.OrderSchema;
import net.quarix.qrx4j.samples.data.beans.meta.SalesRepSchema;
import net.quarix.qrx4j.samples.data.dao.CustomerDataObject;
import net.quarix.qrx4j.samples.data.dao.OrderDataObject;
import net.quarix.qrx4j.samples.data.dao.OrderLineDataObject;
import net.quarix.qrx4j.samples.data.dao.SalesRepDataObject;
import net.quarix.qrx4j.samples.order.OrderStatus;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class SearchOrderPage extends OrderBasePage {
	
	private OrderDataObject orderDo;

	private OrderLineDataObject orderLineDo;
	
	public SearchOrderPage(final PageParameters parameters) {
		
		super(parameters);
		
		Form<Void> modalWindowForm = new Form<Void>("editOrderForm");
		ModalWindow editOrderWindow = new ModalWindow("editOrderWindow");
		editOrderWindow.setInitialHeight(500);
		editOrderWindow.setInitialWidth(700);
		
		editOrderWindow.setWindowClosedCallback(new WindowClosedCallback() {
			public void onClose(AjaxRequestTarget target) {
				refreshPageComponents(target);
			}
		});
		modalWindowForm.add(editOrderWindow);
		
		ModalWindow editOrderLineWindow = new ModalWindow("editOrderLineWindow");
		editOrderLineWindow.setInitialHeight(500);
		editOrderLineWindow.setInitialWidth(700);
		
		editOrderLineWindow.setWindowClosedCallback(new WindowClosedCallback() {
			public void onClose(AjaxRequestTarget target) {
				refreshPageComponents(target);
			}
		});
		modalWindowForm.add(editOrderLineWindow);
		
		add(modalWindowForm);
		
		orderDo = new OrderDataObject() {
			@Override
			protected Serializable getSearchBean() {
				return getSearchForm().getModelObject();
			}
		};
		orderDo.setAppConnector(AppConnector.getInstance());
		
		orderLineDo = new OrderLineDataObject();
		orderLineDo.setAppConnector(AppConnector.getInstance());
		
		initSearchForm(parameters);
		
		String [] orderVisibleColumns = new String [] {OrderSchema.Cols.ORDERDATE.id(),
				OrderSchema.Cols.CUSTNAME.id(),
				OrderSchema.Cols.ORDERSTATUS.id(), OrderSchema.Cols.PROMISEDATE.id(),
				OrderSchema.Cols.SALESREPNAME.id(), OrderSchema.Cols.SHIPDATE.id()};
		
		IDynamicFormConfig<Order> orderFormConfig = new DynamicFormConfig<Order>(orderDo.getSchema(), orderVisibleColumns, true);
		
		orderFormConfig.setColumnProperty(OrderSchema.Cols.ORDERDATE, Property.Label, 
				new StringResourceModel("LBL_DATE", SearchOrderPage.this, null));
		orderFormConfig.setColumnProperty(OrderSchema.Cols.ORDERSTATUS, Property.Label, 
				new StringResourceModel("LBL_STATUS", SearchOrderPage.this, null));
		
		List<DataTableColumn<Order, ?>> additionalColumns = new ArrayList<DataTableColumn<Order, ?>>(1);
		additionalColumns.add(new DataTableColumn<Order, Void>(0, getActionsColumn()));
		
		SimpleDataTableViewPanel<Order> tableView = new SimpleDataTableViewPanel<Order>("orderViewId", orderFormConfig, 10, additionalColumns){
	
			@Override
				protected void onSortClick(AjaxRequestTarget target) {
					super.onSortClick(target);
					target.add(SearchOrderPage.this.get("orderNavigatorId"));
					target.add(SearchOrderPage.this.get("orderLineNavigatorId"));
					target.add(SearchOrderPage.this.get("orderLineViewId"));
				}
	
				@Override
				protected void onDataChanged(AjaxRequestTarget target) {
					super.onDataChanged(target);
					target.add(SearchOrderPage.this.get("orderLineNavigatorId"));
					target.add(SearchOrderPage.this.get("orderLineViewId"));
				}
		};
		
		add(tableView);
		ComponentLinker.bindDataTableData(orderDo, tableView);
		ComponentLinker.bindSort(tableView, orderDo);
		
		DataTablePagesNavigationPanel<Order> pagesNavigator = new DataTablePagesNavigationPanel<Order>("orderNavigatorId", 10, 4) {
	
				private static final long serialVersionUID = -4658560448403518488L;
	
				@Override
				public void onRefreshContent(AjaxRequestTarget target) {
					super.onRefreshContent(target);
					target.add(SearchOrderPage.this.get("orderViewId"));
					target.add(SearchOrderPage.this.get("orderLineNavigatorId"));
					target.add(SearchOrderPage.this.get("orderLineViewId"));
				}
		};
		
		add(pagesNavigator);
		ComponentLinker.bindDataTableNavigation(pagesNavigator, orderDo);
		
		//order line 
		String [] orderLineVisibleColumns = new String [] {OrderLineSchema.Cols.LINENUM.id(), 
				OrderLineSchema.Cols.ITEMNAME.id(),
				OrderLineSchema.Cols.ORDERLINESTATUS.id(),
				OrderLineSchema.Cols.QTY.id(), OrderLineSchema.Cols.PRICE.id(), 
				OrderLineSchema.Cols.DISCOUNT.id(), OrderLineSchema.Cols.EXTENDEDPRICE.id()};
		
		IDynamicFormConfig<OrderLine> orderLineFormConfig = new DynamicFormConfig<OrderLine>(orderLineDo.getSchema(), orderLineVisibleColumns, true);
		
		orderLineFormConfig.setColumnProperty(OrderLineSchema.Cols.EXTENDEDPRICE, Property.Label,
				new StringResourceModel("LBL_TOTAL_PRICE", SearchOrderPage.this, null));
		orderLineFormConfig.setColumnProperty(OrderLineSchema.Cols.ITEMNAME, Property.Label,
				new StringResourceModel("LBL_ITEM", SearchOrderPage.this, null));
		orderLineFormConfig.setColumnProperty(OrderLineSchema.Cols.LINENUM, Property.Width, "50px;");
		orderLineFormConfig.setColumnProperty(OrderLineSchema.Cols.LINENUM, Property.Label, 
				new StringResourceModel("LBL_NO", SearchOrderPage.this, null));
		orderLineFormConfig.setColumnProperty(OrderLineSchema.Cols.ORDERLINESTATUS, Property.Label, 
				new StringResourceModel("LBL_STATUS", SearchOrderPage.this, null));
		
		SimpleDataTableViewPanel<OrderLine> orderLineTableView = new SimpleDataTableViewPanel<OrderLine>("orderLineViewId", orderLineFormConfig, 10){
			
				@Override
				protected void onSortClick(AjaxRequestTarget target) {
					super.onSortClick(target);
					target.add(SearchOrderPage.this.get("orderLineNavigatorId"));
				}
	
				@Override
				protected void onDataChanged(AjaxRequestTarget target) {
					super.onDataChanged(target);
				}
		};
		ComponentLinker.bindDataTableData(orderLineDo, orderLineTableView);
		ComponentLinker.bindSort(orderLineTableView, orderLineDo);
		add(orderLineTableView);
		
		DataTablePagesNavigationPanel<OrderLine> orderLinePagesNavigator = new DataTablePagesNavigationPanel<OrderLine>("orderLineNavigatorId", 10, 4) {
			
			@Override
			public void onRefreshContent(AjaxRequestTarget target) {
				super.onRefreshContent(target);
				target.add(SearchOrderPage.this.get("orderLineViewId"));
			}
		};
		
		add(orderLinePagesNavigator);
		ComponentLinker.bindDataTableNavigation(orderLinePagesNavigator, orderLineDo);
		
		DataLinkParameters orderOrderLineLink = new DataLinkParameters(OrderSchema.Cols.ORDERNUM.id());
		Set<DataLinkParameters> linkParams = new HashSet<DataLinkParameters>(1);
		linkParams.add( orderOrderLineLink);
		ComponentLinker.bindData(orderDo, orderLineDo, linkParams);
		
		orderDo.fetchFirst();
	}

	private IMyStrobeColumn<Order, Void> getActionsColumn() {
	
		DataRecordAction [] actions = new DataRecordAction [] {DataRecordAction.Edit, DataRecordAction.Delete};
		
		return new DataRowActionsToolbarColumn<Order>("", actions, orderDo){
			
			@Override
			protected void onEditRecordClicked(AjaxRequestTarget target,
					IModel<Order> model) {
				
				super.onEditRecordClicked(target, model);
				
				Integer orderNumber = model.getObject().getOrdernum();
				PageParameters parameters = new PageParameters();
				parameters.add("orderNumber", orderNumber);
				
				EditOrderPage editOrderPage = new EditOrderPage(parameters);
				setResponsePage(editOrderPage);
				
			}
			
			@Override
			protected void onDeleteRecordClicked(AjaxRequestTarget target,
					IModel<Order> model) {
				
				orderDo.moveToRow(model.getObject().getRowId());
				
				DSTransactionManager transactionManager = new DSTransactionManager(new OrderInfoDSSchema(), AppConnector.getInstance());
				transactionManager.addTransactionParticipant(orderDo);
				
				try {
					orderDo.deleteData();
					transactionManager.commit();
					
					target.add(SearchOrderPage.this.get("orderNavigatorId"));
					target.add(SearchOrderPage.this.get("orderViewId"));
					target.add(SearchOrderPage.this.get("orderLineViewId"));
					target.add(SearchOrderPage.this.get("orderLineNavigatorId"));
					
				} catch (WicketDSBLException e) {
					error(e.getBLErorMessage().getMessage());
					target.add(getSearchForm().get("feedback"));
				}
			}
			
			@Override
			public void populateItem(Item<ICellPopulator<Order>> cellItem,
					String componentId, IModel<Order> rowModel) {
				super.populateItem(cellItem, componentId, rowModel);
			
				cellItem.add(new AttributeAppender("width", Model.of("50px")));
			}
			
		};
	}
	
//	private IMyStrobeColumn<OrderLine, Void> getOLActionsColumn() {
//		
//		DataRecordAction [] actions = new DataRecordAction [] {DataRecordAction.Edit, DataRecordAction.Delete};
//		
//		return new DataRowActionsToolbarColumn<OrderLine>("", actions, orderLineDo){
//			
//			@Override
//			protected void onEditRecordClicked(AjaxRequestTarget target,
//					IModel<OrderLine> model) {
//				
//				super.onEditRecordClicked(target, model);
//				
//				target.add(SearchOrderPage.this.get("orderLineViewId"));
//				
//				ModalWindow editOrderLineWindow = getEditOrderLineWindow(ModalWindowUpdateMode.Edit);
//				editOrderLineWindow.show(target);
//			}
//			
//			@Override
//			protected void onDeleteRecordClicked(AjaxRequestTarget target,
//					IModel<OrderLine> model) {
//				
//				super.onDeleteRecordClicked(target, model);
//				try {
//					orderLineDo.deleteData();
//				} catch (WicketDSBLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			
//				target.add(SearchOrderPage.this.get("orderLineViewId"));
//				target.add(SearchOrderPage.this.get("orderLineNavigatorId"));
//			}
//			
//			@Override
//			public void populateItem(Item<ICellPopulator<OrderLine>> cellItem,
//					String componentId, IModel<OrderLine> rowModel) {
//				super.populateItem(cellItem, componentId, rowModel);
//			
//				cellItem.add(new AttributeAppender("width", Model.of("50px")));
//			}
//		};
//	}

	protected void refreshPageComponents(AjaxRequestTarget target) {
		target.add(SearchOrderPage.this.get("orderNavigatorId"));
		target.add(SearchOrderPage.this.get("orderViewId"));
		target.add(SearchOrderPage.this.get("orderLineViewId"));
		target.add(SearchOrderPage.this.get("orderLineNavigatorId"));
		target.add(getSearchForm());
	}

	private ModalWindow getEditOrderWindow(final ModalWindowUpdateMode updateMode) {
		
		ModalWindow editorderWindow = (ModalWindow) get("editOrderForm").get("editOrderWindow");
		
		String [] visibleColumns = new String [] {OrderSchema.Cols.CUSTNUM.id(), OrderSchema.Cols.PO.id(), 
				OrderSchema.Cols.SALESREP.id(), OrderSchema.Cols.PROMISEDATE.id(),
				OrderSchema.Cols.INSTRUCTIONS.id(), OrderSchema.Cols.TERMS.id(),
				OrderSchema.Cols.CARRIER.id(), OrderSchema.Cols.ORDERSTATUS.id()};
		IDynamicFormConfig<Order> editPanelConfig = new DynamicFormConfig<Order>(new OrderSchema(), visibleColumns, true);
		
		String [] customerVisibleColumns = new String [] {CustomerSchema.Cols.NAME.id(), CustomerSchema.Cols.CONTACT.id(),
    			CustomerSchema.Cols.STATE.id(), CustomerSchema.Cols.COUNTRY.id(),
    			CustomerSchema.Cols.CITY.id(), CustomerSchema.Cols.COMMENTS.id()};
		IDynamicFormConfig<Customer> customerDisplay = new DynamicFormConfig<Customer>(new CustomerSchema(), customerVisibleColumns, true );
		CustomerDataObject customerDO = new CustomerDataObject();
		customerDO.setAppConnector(AppConnector.getInstance()); 
		editPanelConfig.setColumnProperty(OrderSchema.Cols.CUSTNUM, Property.SelectableFieldValue, Boolean.TRUE);
		editPanelConfig.setColumnProperty(OrderSchema.Cols.CUSTNUM, Property.LinkedColumnName, CustomerSchema.Cols.CUSTNUM.id());
		editPanelConfig.setColumnProperty(OrderSchema.Cols.CUSTNUM, Property.LinkedDataObject, customerDO);
		editPanelConfig.setColumnProperty(OrderSchema.Cols.CUSTNUM, Property.SelectRecordTableConfig, customerDisplay);
		editPanelConfig.setColumnProperty(OrderSchema.Cols.CUSTNUM, Property.VisibleColumnName, CustomerSchema.Cols.NAME.id());
		editPanelConfig.setColumnProperty(OrderSchema.Cols.CUSTNUM, Property.Required, true);
		editPanelConfig.setColumnProperty(OrderSchema.Cols.CUSTNUM, Property.DescriptionColumn, OrderSchema.Cols.CUSTNAME.id());
		
		String [] salesRepVisibleColumns = new String [] {SalesRepSchema.Cols.REPNAME.id(), SalesRepSchema.Cols.SALESREP.id(),
				SalesRepSchema.Cols.REGION.id()};
		IDynamicFormConfig<SalesRep> salesRepDisplay = new DynamicFormConfig<SalesRep>(new SalesRepSchema(), salesRepVisibleColumns, true );
		SalesRepDataObject salesRepDO = new SalesRepDataObject();
		salesRepDO.setAppConnector(AppConnector.getInstance()); 
		editPanelConfig.setColumnProperty(OrderSchema.Cols.SALESREP, Property.SelectableFieldValue, Boolean.TRUE);
		editPanelConfig.setColumnProperty(OrderSchema.Cols.SALESREP, Property.LinkedColumnName, SalesRepSchema.Cols.SALESREP.id());
		editPanelConfig.setColumnProperty(OrderSchema.Cols.SALESREP, Property.SelectRecordTableConfig, salesRepDisplay);
		editPanelConfig.setColumnProperty(OrderSchema.Cols.SALESREP, Property.LinkedDataObject, salesRepDO);
		editPanelConfig.setColumnProperty(OrderSchema.Cols.SALESREP, Property.VisibleColumnName, SalesRepSchema.Cols.REPNAME.id());
		editPanelConfig.setColumnProperty(OrderSchema.Cols.SALESREP, Property.Required, true);
		editPanelConfig.setColumnProperty(OrderSchema.Cols.SALESREP, Property.DescriptionColumn, OrderSchema.Cols.SALESREPNAME.id());
		
		editPanelConfig.setColumnProperty(OrderSchema.Cols.ORDERSTATUS, Property.ReadOnly, Boolean.TRUE);
		
		EditRecordModalPanel<Order> editCustomerPanel = new EditRecordModalPanel<Order>(editorderWindow.getContentId(), 
				orderDo, editorderWindow, editPanelConfig, updateMode, null){
			
			@Override
			public IModel<String> getTitleModel() {
				return Model.of("Edit Order");
			}
			
			@Override
			protected void additionalSettings() {
				if (updateMode.equals(ModalWindowUpdateMode.Add)) {
					orderDo.getData().setOrderdate(new Date());
					orderDo.getData().setOrderstatus(OrderStatus.ORDERED.getStatusText());
					
					OrderSearchBean searchBean = getSearchForm().getModelObject();
	    			searchBean.setPo(null);
	    			searchBean.setCustnum(null);
	    			searchBean.setOrderstatus(null);
				}
			}
			
			@Override
			protected void onSaveSuccess(AjaxRequestTarget target, ModalWindowUpdateMode updateMode){
				orderDo.clearFilters();
				orderDo.addFilter(new FilterParameter(OrderSchema.Cols.ORDERNUM, FilterOperator.EQ, orderDo.getData().getOrdernum()));
				orderDo.resetDataBuffer();
			}
		};
	
		editorderWindow.setContent(editCustomerPanel);
		
		return editorderWindow;
	}
	
//	private ModalWindow getEditOrderLineWindow(ModalWindowUpdateMode updateMode) {
//		
//		ModalWindow editOrderLineWindow = (ModalWindow) get("editOrderForm").get("editOrderLineWindow");
//		
//		String [] visibleColumns = new String [] {OrderLineSchema.Cols.ITEMNUM.id(), OrderLineSchema.Cols.PRICE.id(),
//				OrderLineSchema.Cols.QTY.id(), OrderLineSchema.Cols.DISCOUNT.id()};
//		IDynamicFormConfig<OrderLine> editOrderLinePanelConfig = new DynamicFormConfig<OrderLine>(new OrderLineSchema(), visibleColumns, true);
//		
//		ItemDataObject itemDo = new ItemDataObject();
//		itemDo.setAppConnector(AppConnector.getInstance());
//		
//		editOrderLinePanelConfig.setColumnProperty(OrderLineSchema.Cols.ITEMNUM, Property.SelectableFieldValue, Boolean.TRUE);
//		editOrderLinePanelConfig.setColumnProperty(OrderLineSchema.Cols.ITEMNUM, Property.LinkedColumnName, ItemSchema.Cols.ITEMNUM.id());
//		editOrderLinePanelConfig.setColumnProperty(OrderLineSchema.Cols.ITEMNUM, Property.LinkedDataObject, itemDo);
//		editOrderLinePanelConfig.setColumnProperty(OrderLineSchema.Cols.ITEMNUM, Property.VisibleColumnName, ItemSchema.Cols.ITEMNAME.id());
//		editOrderLinePanelConfig.setColumnProperty(OrderLineSchema.Cols.ITEMNUM, Property.Required, Boolean.TRUE);
//		
//		editOrderLinePanelConfig.setColumnProperty(OrderLineSchema.Cols.QTY, Property.Required, Boolean.TRUE);
//		editOrderLinePanelConfig.setColumnProperty(OrderLineSchema.Cols.PRICE, Property.Required, Boolean.TRUE);
//		
//		String [] itemVisibleColumns = new String [] {ItemSchema.Cols.ITEMNAME.id(), ItemSchema.Cols.CATDESCRIPTION.id(),
//				ItemSchema.Cols.PRICE.id(), ItemSchema.Cols.WEIGHT.id()};
//		IDynamicFormConfig<net.quarix.qrx4j.samples.data.beans.Item> itemConfig = 
//				new DynamicFormConfig<net.quarix.qrx4j.samples.data.beans.Item> (new ItemSchema(), itemVisibleColumns, true);
//		editOrderLinePanelConfig.setColumnProperty(OrderLineSchema.Cols.ITEMNUM, Property.SelectRecordTableConfig, itemConfig);
//		
//		if (ModalWindowUpdateMode.Add.equals(updateMode)) {	
//			editOrderLinePanelConfig.setColumnProperty(OrderLineSchema.Cols.ITEMNUM, Property.ReadOnly, Boolean.FALSE);
//		}
//		
//		EditRecordModalPanel<OrderLine> editCustomerPanel = new EditRecordModalPanel<OrderLine>(editOrderLineWindow.getContentId(), 
//				orderLineDo, editOrderLineWindow, editOrderLinePanelConfig, updateMode, null){
//			
//			@Override
//			public IModel<String> getTitleModel() {
//				return Model.of("Edit Order Line");
//			}
//		};
//	
//		editOrderLineWindow.setContent(editCustomerPanel);
//		
//		return editOrderLineWindow;
//	}

	protected  void initSearchForm(PageParameters parameters) {
		
		String custName = null;
		OrderSearchBean orderSearchBean = new OrderSearchBean();
		
		if (!(parameters.get("cust_name").isEmpty())) {
			
			custName = parameters.get("cust_name").toString();
			orderSearchBean.setCustnum(parameters.get("cust_num").toInteger());
			
			orderDo.addFilter(new FilterParameter(OrderSchema.Cols.CUSTNUM, FilterOperator.EQ, orderSearchBean.getCustnum()));
		}
		
		Form<OrderSearchBean> searchForm = new Form<OrderSearchBean>("searchForm",
				new CompoundPropertyModel<OrderSearchBean>(orderSearchBean));
		searchForm.setOutputMarkupId(true);
		
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		searchForm.add(feedback);
		
		TextField<String> poNumberTextField = new TextField<String>("po");
		searchForm.add(poNumberTextField);
		
		IDataObject<Customer> customereDO = new CustomerDataObject();
		customereDO.setAppConnector(AppConnector.getInstance());
		
		String [] customerVisibleColumns = new String [] {CustomerSchema.Cols.NAME.id(), CustomerSchema.Cols.CONTACT.id(),
    			CustomerSchema.Cols.STATE.id(), CustomerSchema.Cols.COUNTRY.id(),
    			CustomerSchema.Cols.CITY.id(), CustomerSchema.Cols.COMMENTS.id()};
		IDynamicFormConfig<Customer> customerDisplay = new DynamicFormConfig<Customer>(new CustomerSchema(), customerVisibleColumns, true );
		IModel<Integer> custNumModel = ((CompoundPropertyModel<OrderSearchBean>)searchForm.getModel()).<Integer>bind("custnum"); 
		LookupFormComponent<Integer, Customer> custNumLookup = new LookupFormComponent<Integer, Customer>
			("custnum", custNumModel, CustomerSchema.Cols.NAME.id(),
				CustomerSchema.Cols.CUSTNUM.id(), customereDO, customerDisplay, custName);
		searchForm.add(custNumLookup);
		
		DropDownChoice<String> orderstatusDropDown = new DropDownChoice<String>("orderstatus",
				Arrays.asList(OrderStatus.getOrderStatusTextArray()));
		searchForm.add(orderstatusDropDown);
		
		AjaxButton searchButton = new AjaxButton("searchButton"){
	
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
				
				orderDo.clearFilters();
				
				OrderSearchBean searchBean = getSearchForm().getModelObject();
				
				if (!StringUtil.isNullOrEmpty(searchBean.getPo())) {
					orderDo.addFilter(new FilterParameter(OrderSchema.Cols.PO, FilterOperator.MATCHES, searchBean.getPo()));
				}
				
				if ( searchBean.getCustnum() != null && searchBean.getCustnum() > 0) {
					orderDo.addFilter(new FilterParameter(OrderSchema.Cols.CUSTNUM, FilterOperator.EQ, searchBean.getCustnum()));
				}
				
				if ( !StringUtil.isNullOrEmpty(searchBean.getOrderstatus())) {
					orderDo.addFilter(new FilterParameter(OrderSchema.Cols.ORDERSTATUS, FilterOperator.EQ, searchBean.getOrderstatus()));
				}
				
				orderDo.resetDataBuffer();
				
				refreshPageComponents(target);
			}
			
			@Override
			public void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError();
			}
		};
		
		AjaxButton resetButton = new AjaxButton("resetButton"){
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
				
				orderDo.clearFilters();
				
				OrderSearchBean searchBean = getSearchForm().getModelObject();
				searchBean.setPo(null);
				searchBean.setCustnum(null);
				searchBean.setOrderstatus(null);
				
				orderDo.resetDataBuffer();
				
				refreshPageComponents(target);
			}
		};
		
		AjaxLink<Void> addButton = new AjaxLink<Void>("addButton"){
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				orderDo.cancelCRUDOperation();
				
				Order order = new Order();
				orderDo.createData(order);
				
				ModalWindow editCustomerWindow = getEditOrderWindow(ModalWindowUpdateMode.Add);
				editCustomerWindow.show(target);
			}
		};
		
		searchForm.add(searchButton);
		searchForm.add(resetButton);
		searchForm.add(addButton);
		 
		add(searchForm);
	}

	protected Form<OrderSearchBean> getSearchForm() {
		return (Form<OrderSearchBean>) get("searchForm");
	}	
	
	public class OrderSearchBean implements Serializable {
		
		@SearchFilter(name = "po") 
		private String po;
		
		@SearchFilter(name = "custnum") 
		private Integer custnum;
	
		@SearchFilter(name = "orderstatus") 
		private String orderstatus;

		public String getPo() {
			return po;
		}

		public void setPo(String po) {
			this.po = po;
		}

		public Integer getCustnum() {
			return custnum;
		}

		public void setCustnum(Integer custnum) {
			this.custnum = custnum;
		}

		public String getOrderstatus() {
			return orderstatus;
		}

		public void setOrderstatus(String orderstatus) {
			this.orderstatus = orderstatus;
		}
	}

	@Override
	protected HeaderLink getHeaderActiveLink() {
		return HeaderLink.Orders;
	}

	@Override
	protected OrderTab getCurrentActiveTab() {
		return OrderTab.Search;
	}
}
