package net.quarix.qrx4.samples.ui.page.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mystrobe.client.FilterOperator;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.connector.transaction.DSTransactionManager;
import net.mystrobe.client.connector.transaction.IDSTransactionable;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig.Property;
import net.mystrobe.client.impl.FilterParameter;
import net.mystrobe.client.navigator.DataObjectIterator;
import net.quarix.qrx4.samples.ui.page.settings.SelectCustomerPanel;
import net.quarix.qrx4.samples.ui.panel.LookupFormComponent;
import net.quarix.qrx4j.samples.Qrx4jSampleApplication;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.beans.Item;
import net.quarix.qrx4j.samples.data.beans.Order;
import net.quarix.qrx4j.samples.data.beans.OrderLine;
import net.quarix.qrx4j.samples.data.beans.meta.ItemSchema;
import net.quarix.qrx4j.samples.data.beans.meta.OrderInfoDSSchema;
import net.quarix.qrx4j.samples.data.beans.meta.OrderLineSchema;
import net.quarix.qrx4j.samples.data.beans.meta.OrderSchema;
import net.quarix.qrx4j.samples.data.dao.ItemDataObject;
import net.quarix.qrx4j.samples.data.dao.OrderDataObject;
import net.quarix.qrx4j.samples.data.dao.OrderLineDataObject;
import net.quarix.qrx4j.samples.order.OrderStatus;
import org.apache.wicket.Application;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.converter.BigDecimalConverter;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.validator.RangeValidator;

public class EditOrderPage extends OrderBasePage {

	private static final BigDecimal HUNDRED = new BigDecimal(100); 
	
	protected IDataObject<Order> orderDO;
	
	protected IDataObject<OrderLine> orderLineDO;  
	
	protected boolean addOrder = false;
	
	protected Integer orderNumber;
	
	protected SelectCustomerPanel selectCustomerPanel;
	
	protected OrderLineListView linesTable;

	protected FinishEditOrderPanel finishEditOrderPanel;

	protected WebMarkupContainer listContainer1;
	
	protected OrderLine currentOrderLine, originalOrderLine;
	
	protected int nextLineNumber = 0; 
	
	protected List<OrderLine> orderLinesList;
	
	boolean updateState = false;
	
	protected BigDecimalConverter bigDecimalConverter;
	
	protected BigDecimal totalOrderPrice = BigDecimal.ZERO;
	
	public EditOrderPage(PageParameters parameters) {
		super(parameters);
		
		bigDecimalConverter = new BigDecimalConverter();
		
		if (parameters != null) {
			if (parameters.get("orderNumber") != null) {
				orderNumber = parameters.get("orderNumber").toInteger();
			}
		}
		
		initPageComponents();
	}
	
	public EditOrderPage(IDataObject<Order> orderDO, IDataObject<OrderLine> orderLineDO, boolean addOrder) {
		super(null);
		this.orderDO = orderDO;
		this.orderLineDO = orderLineDO;
		this.addOrder = addOrder;
		initPageComponents();
	}

	protected void initPageComponents() {
		
		String [] orderLineVisibleColumns = new String [] {OrderLineSchema.Cols.LINENUM.id(), OrderLineSchema.Cols.ORDERLINESTATUS.id(),
				OrderLineSchema.Cols.QTY.id(), OrderLineSchema.Cols.PRICE.id(), 
				OrderLineSchema.Cols.DISCOUNT.id(), OrderLineSchema.Cols.EXTENDEDPRICE.id()};
		
		IDynamicFormConfig<OrderLine> orderLineFormConfig = new DynamicFormConfig<OrderLine>(new OrderLineSchema(), orderLineVisibleColumns, true);
		
		orderLineFormConfig.setColumnProperty(OrderLineSchema.Cols.EXTENDEDPRICE, Property.Label,
				new StringResourceModel("LBL_TOTAL_PRICE", EditOrderPage.this, null));
		orderLineFormConfig.setColumnProperty(OrderLineSchema.Cols.LINENUM, Property.Width, "50px;");
		orderLineFormConfig.setColumnProperty(OrderLineSchema.Cols.LINENUM, Property.Label, 
				new StringResourceModel("LBL_NO", EditOrderPage.this, null));
		orderLineFormConfig.setColumnProperty(OrderLineSchema.Cols.ORDERLINESTATUS, Property.Label, 
				new StringResourceModel("LBL_STATUS", EditOrderPage.this, null));
		
                Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
		this.orderDO = new OrderDataObject(application.getMystrobeConfig(), application.getAppName());
		
		if (!addOrder && orderNumber != null ){
			this.orderDO.addFilter(new FilterParameter(OrderSchema.Cols.ORDERNUM, FilterOperator.EQ, orderNumber));
		}
		
		this.orderLineDO = new OrderLineDataObject(application.getMystrobeConfig(), application.getAppName());
		this.orderLineDO.fetchAllRecords();
		if (!addOrder && orderNumber != null ){
			this.orderLineDO.addFilter(new FilterParameter(OrderLineSchema.Cols.ORDERNUM, FilterOperator.EQ, orderNumber));
		}
		
		this.orderDO.resetDataBuffer();
		add(new EditOrderForm("editOrderForm", new CompoundPropertyModel<Order>(this.orderDO.getData())));
		
		this.orderLineDO.resetDataBuffer();
		orderLinesList = new ArrayList<OrderLine>(orderLineDO.getDataBuffer().size());
		
		DataObjectIterator<OrderLine> orderLineIterator = new DataObjectIterator<OrderLine>(orderLineDO);
		while(orderLineIterator.hasNext()) {
			OrderLine currentLine =  orderLineIterator.next();
			OrderLine newLine = new OrderLine();
			copyOrderLine(currentLine, newLine );
			orderLinesList.add(newLine);
		}
		currentOrderLine = orderLinesList.size() > 0 ? orderLinesList.get(0) : new OrderLine();
		originalOrderLine = new OrderLine();
		copyOrderLine(currentOrderLine, originalOrderLine);
		nextLineNumber = orderLineDO.getDataBuffer().size() + 1;
		
		listContainer1 = new WebMarkupContainer("listHolder1");
		listContainer1.setOutputMarkupId(true);
		
		linesTable = new OrderLineListView("itemsList1", Model.ofList(orderLinesList));
		listContainer1.add(linesTable);
		WebMarkupContainer noResultId1 = new WebMarkupContainer("noResultId1"){
			@Override
			public boolean isVisible() {
				return orderLinesList == null || orderLinesList.isEmpty();
			}
		};
		listContainer1.add(noResultId1);
		listContainer1.add(new Label("totalOrderPrice", new LoadableDetachableModel<String>() {

						@Override
						protected String load() {
							String totPrice = bigDecimalConverter.convertToString(totalOrderPrice, getSession().getLocale());
							StringResourceModel resourceModel = new StringResourceModel("LBL_TOTAL_PRICE_PARAM", EditOrderPage.this, 
									null, "", totPrice);
							return resourceModel.getString();		
						}
					}) 
			{
			
				@Override
				public boolean isVisible() {
					// TODO Auto-generated method stub
					return orderLinesList != null && !orderLinesList.isEmpty();
				}
			}
		);
		add(listContainer1);
		
		ModalWindow selectCustomerWindow = new ModalWindow("selectCustomerWindow"); 
		selectCustomerWindow.setTitle(new StringResourceModel("LBL_SELECT_CUSTOMER", null, EditOrderPage.this));
		selectCustomerWindow.setInitialHeight(350);
		selectCustomerWindow.setInitialWidth(600);
		selectCustomerWindow.setWindowClosedCallback(new WindowClosedCallback() {
			public void onClose(AjaxRequestTarget target) {
				
			}
		});
		add(selectCustomerWindow);
		
		
		addOrderLineDetails();
		
		finishEditOrderPanel = new FinishEditOrderPanel("finishEditOrderPanel", orderDO,  orderLinesList) {

			@Override
			protected void onSaveChanges() throws Exception {
				Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
				DSTransactionManager transactionManager = new DSTransactionManager(new OrderInfoDSSchema(), application.getMystrobeConfig(), application.getAppName(), 
						new IDSTransactionable<?> [] {orderDO, orderLineDO});
				
				Map<String, Void> rowIdsMap = new HashMap<String, Void>(this.orderLinesList.size());
				for(OrderLine orderLine : this.orderLinesList ) {
					
					if (orderLine.getRowId() != null) {
						orderLineDO.moveToRow(orderLine.getRowId());
						
						if (orderLineHasChanges(orderLineDO.getData(), orderLine)) {
							orderLineDO.updateData();
						}
						
						rowIdsMap.put(orderLine.getRowId(), null);
					
					} else {
						
						//create order line
						orderLineDO.createData(orderLine);
						orderLineDO.updateData();
					}
				}
				
				List<OrderLine> orderLinesToRemove = new ArrayList<OrderLine>();
				for (OrderLine orderLine : orderLineDO.getDataBuffer()) {
					
					if (orderLine.getRowId() != null &&
							!rowIdsMap.containsKey(orderLine.getRowId())) {
						orderLinesToRemove.add(orderLine);
					}
				}
				
				for (OrderLine orderLine : orderLinesToRemove) {
					orderLineDO.moveToRow(orderLine.getRowId());
					orderLineDO.deleteData();
				}
				
				orderDO.updateData();
				
				transactionManager.commit();
			}
			
		};
		add(finishEditOrderPanel);
	}
	
	protected boolean orderLineHasChanges(OrderLine orderLine1, OrderLine orderLine2) {
		boolean hasChanges = false; 
		if (!orderLine1.getItemnum().equals(orderLine2.getItemnum())) {
			orderLine1.setItemnum(orderLine2.getItemnum());
			orderLine1.setItemname(orderLine2.getItemname());
			hasChanges = true;
		}
		
		if (!orderLine1.getQty().equals(orderLine2.getQty())) {
			orderLine1.setQty(orderLine2.getQty());
			orderLine1.setExtendedprice(orderLine2.getExtendedprice());
			hasChanges = true;
		}
		
		if (!orderLine1.getPrice().equals(orderLine2.getPrice())) {
			orderLine1.setPrice(orderLine2.getPrice());
			orderLine1.setExtendedprice(orderLine2.getExtendedprice());
			hasChanges = true;
		}
		
		if (!orderLine1.getOrderlinestatus().equals(orderLine2.getOrderlinestatus())) {
			orderLine1.setOrderlinestatus(orderLine2.getOrderlinestatus());
			hasChanges = true;
		}
		
		if ( (orderLine1.getDiscount() == null && orderLine2.getDiscount() != null ) ||
				(orderLine1.getDiscount() != null && !orderLine1.getDiscount().equals(orderLine2.getDiscount())) )	 {
			orderLine1.setDiscount(orderLine2.getDiscount());
			orderLine1.setExtendedprice(orderLine2.getExtendedprice());
			hasChanges = true;
		}
		
		return hasChanges;
	}
	
	protected void addOrderLineDetails() {
		
		WebMarkupContainer orderLineFormContainer = new WebMarkupContainer("orderLineFormContainer"){
			@Override
			public boolean isVisible() {
				return !orderLinesList.isEmpty() || updateState;
			}
		};
		orderLineFormContainer.setOutputMarkupPlaceholderTag(true);
		
		orderLineFormContainer.add(new EditOrderLineForm("editOrderLineForm",
				new CompoundPropertyModel<OrderLine>(currentOrderLine)));
		add(orderLineFormContainer);
		
		AjaxLink<Void> addOrderLineBtn = new  AjaxLink<Void>("addOrderLineBtn"){

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				updateState = true;
				
				currentOrderLine = new OrderLine();
				currentOrderLine.setOrdernum(orderDO.getData().getOrdernum());
				
				WebMarkupContainer orderLineFormContainer = (WebMarkupContainer) EditOrderPage.this.get("orderLineFormContainer");
				
				EditOrderLineForm lineForm = (EditOrderLineForm)EditOrderPage.this.get("orderLineFormContainer").get("editOrderLineForm");
				updateEditOrderLineFormModelObject(currentOrderLine, target);
				
				target.add(orderLineFormContainer);
				target.add(EditOrderPage.this.get("addOrderLineBtn"));
			}
			
			@Override
			public boolean isEnabled() {
				return !updateState;
			}
		};
		addOrderLineBtn.setOutputMarkupId(true);
		add(addOrderLineBtn);
	}
	
	protected class EditOrderForm extends Form<Order> {

		public EditOrderForm(String id, IModel<Order> model) {
			super(id, model);
			initFormComponents();
		}
		
		protected void initFormComponents() {
			
			FeedbackPanel feedback = new FeedbackPanel("feedback"); 
			feedback.setOutputMarkupId(true);
			add(feedback);
			
			TextField<String> instructionsTxt = new TextField<String>(OrderSchema.Cols.INSTRUCTIONS.id());
			add(instructionsTxt);
			
			TextField<String> carrierTxt = new TextField<String>(OrderSchema.Cols.CARRIER.id());
			add(carrierTxt);
			
			DateTextField promiseDate = new DateTextField(OrderSchema.Cols.PROMISEDATE.id());
			promiseDate.setRequired(true);
			promiseDate.add(new DatePicker());
			add(promiseDate);
			
			SalesRepresentativeAutocompleteTextField salesRepTxt = 
					new SalesRepresentativeAutocompleteTextField(OrderSchema.Cols.SALESREP.id());
			salesRepTxt.setRequired(true);
			add(salesRepTxt);
			
			Label customerLabel = new Label("customerLabel", getModelObject().getCustname() != null ?
					getModelObject().getCustname() : "");
			customerLabel.setOutputMarkupId(true);
			add(customerLabel);
			
			AjaxLink<Void> selectCustomerLink = new AjaxLink<Void>("selectCustomerLnk") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					getSelectCustomerWindow().show(target);
				}
			};
			add(selectCustomerLink);
			
			DropDownChoice<String> orderstatusDropDown = new DropDownChoice<String>(OrderSchema.Cols.ORDERSTATUS.id(),
					Arrays.asList(OrderStatus.getOrderStatusTextArray()));
			orderstatusDropDown.setRequired(true);
			add(orderstatusDropDown);
			
			DateTextField shipDate = new DateTextField(OrderSchema.Cols.SHIPDATE.id());
			shipDate.add(new DatePicker());
			add(shipDate);
			
			TextField<String> termsTxt = new TextField<String>(OrderSchema.Cols.TERMS.id());
			add(termsTxt);
			
			AjaxButton saveButton = new AjaxButton("saveBtn") {
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(EditOrderForm.this.get("feedback"));
				}
			};
			add(saveButton);
			
			AjaxLink<Void> resetBtn = new AjaxLink<Void>("resetBtn") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					orderDO.resetData();
					target.add(EditOrderForm.this);
				}
			};
			add(resetBtn);
			
			AjaxLink<Void> bckToSearch = new AjaxLink<Void>("btnBckToSearch") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					setResponsePage(SearchOrderPage.class);
				}
			};
			add(bckToSearch);
			
			AjaxLink<Void> btnNext = new AjaxLink<Void>("nextBtn") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					target.appendJavaScript("$('#tabs').tabs('option', 'active', 2 );");
				}
			};
			add(btnNext);
		}
		
	}
	
	@Override
	protected OrderTab getCurrentActiveTab() {
		return OrderTab.Edit;
	}
	
	protected ModalWindow getSelectCustomerWindow() {
		
		final ModalWindow selectCustomerWindow = (ModalWindow) get("selectCustomerWindow");
		
		if (selectCustomerPanel == null) {
			selectCustomerPanel = new SelectCustomerPanel(selectCustomerWindow.getContentId()) {
				
				@Override
				protected void onDataSelected(Customer customer, AjaxRequestTarget target) {
					EditOrderForm orderForm = (EditOrderForm) EditOrderPage.this.get("editOrderForm");
					
					orderForm.getModelObject().setCustnum(customer.getCustnum());
					orderForm.getModelObject().setCustname(customer.getName());
					orderForm.get("customerLabel").setDefaultModelObject(customer.getName());
					
					selectCustomerWindow.close(target);
					target.add(orderForm.get("customerLabel"));
				}
				
				@Override
				protected void onCancelSelectData(AjaxRequestTarget target) {
					selectCustomerWindow.close(target);
				}
			};
			
			selectCustomerWindow.setContent(selectCustomerPanel);
		} else {
			selectCustomerPanel.reloadPanel();
		}
		
		return selectCustomerWindow;
	}
	
	protected class EditOrderLineForm extends Form<OrderLine> {

		public EditOrderLineForm(String id, IModel<OrderLine> model ) {
			super(id, model);
			
			initFormComponents();
		}
		
		protected void initFormComponents() {
			
			setOutputMarkupId(true);
			
			FeedbackPanel feedback = new FeedbackPanel("feedback"); 
			feedback.setOutputMarkupId(true);
			add(feedback);
			
			TextField<BigDecimal> priceTxt = new TextField<BigDecimal>(OrderLineSchema.Cols.PRICE.id());
			priceTxt.setRequired(true);
			priceTxt.add(RangeValidator.<BigDecimal>minimum(new BigDecimal(0.01d)));
			add(priceTxt);
			
			TextField<Integer> discountTxt = new TextField<Integer>(OrderLineSchema.Cols.DISCOUNT.id());
			discountTxt.add(RangeValidator.<Integer>range(1, 100));
			add(discountTxt);
			
			TextField<Integer> quantityTxt = new TextField<Integer>(OrderLineSchema.Cols.QTY.id());
			quantityTxt.setRequired(true);
			quantityTxt.add(RangeValidator.<Integer>minimum(1));
			add(quantityTxt);
			
			DropDownChoice<String> orderLineStatusDropDown = new DropDownChoice<String>(OrderLineSchema.Cols.ORDERLINESTATUS.id(),
					Arrays.asList(OrderStatus.getOrderStatusTextArray()));
			orderLineStatusDropDown.setRequired(true);
			add(orderLineStatusDropDown);
			
                        Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
			IDataObject<Item> itemDO = new ItemDataObject(application.getMystrobeConfig(), application.getAppName());
			
			String [] itemVisibleColumns = new String [] {ItemSchema.Cols.ITEMNAME.id(), ItemSchema.Cols.CATEGORY1.id(),
					ItemSchema.Cols.CATEGORY1.id(), ItemSchema.Cols.PRICE.id(), ItemSchema.Cols.WEIGHT.id()};
			IDynamicFormConfig<Item> itemDisplay = new DynamicFormConfig<Item>(new ItemSchema(), itemVisibleColumns, true );
			itemDisplay.setColumnProperty(ItemSchema.Cols.CATEGORY1, Property.Label, 
					new StringResourceModel("LBL_CATEGORY", null, EditOrderPage.this));
			itemDisplay.setColumnProperty(ItemSchema.Cols.CATEGORY2, Property.Label, 
					new StringResourceModel("LBL_TYPE", null, EditOrderPage.this));
			
			CompoundPropertyModel<OrderLine> model = (CompoundPropertyModel<OrderLine>)getModel(); 
			
			LookupFormComponent<Integer, Item> itemNumLookup = new LookupFormComponent<Integer, Item>("itemnum", 
					model.<Integer>bind(OrderLineSchema.Cols.ITEMNUM.id()), ItemSchema.Cols.ITEMNAME.id(),
					ItemSchema.Cols.ITEMNUM.id(), itemDO, itemDisplay, getModelObject().getItemname() != null ?
							getModelObject().getItemname().toString() : ""){
				@Override
				protected void onModelChanged() {
					super.onModelChanged();
					if (!changingModel) {
						displayText = EditOrderLineForm.this.getModelObject().getItemname() != null ?
								EditOrderLineForm.this.getModelObject().getItemname() : "";
					}
				};
				
				@Override
				protected void onNewDataSelected(
						Item selectedItem) {
					EditOrderLineForm.this.getModelObject().setItemname(selectedItem.getItemname());
				}
			};
			itemNumLookup.setRequired(true);
			add(itemNumLookup);
			
			AjaxButton saveButton = new AjaxButton("saveBtn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);

					OrderLine line = EditOrderLineForm.this.getModelObject();
					
					if (line.getLinenum() == null) {
						nextLineNumber++;
						line.setLinenum(nextLineNumber);
						orderLinesList.add(line);
					}
					
					line.setExtendedprice(line.getPrice().multiply(BigDecimal.valueOf(line.getQty())));
					if (line.getDiscount() != null) {
						BigDecimal discount = line.getExtendedprice().multiply(BigDecimal.valueOf(line.getDiscount())).divide(HUNDRED, 6, BigDecimal.ROUND_HALF_UP);
						line.setExtendedprice(line.getExtendedprice().subtract(discount).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					
					target.add(listContainer1);
					target.appendJavaScript("initSelectableItems();");
					target.add(EditOrderPage.this.get("orderLineFormContainer"));
					
					updateState = false;
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					updateState = true;
					target.add(EditOrderPage.this.get("orderLineFormContainer"));
				}
			};
			add(saveButton);
			
			AjaxButton deleteButton = new AjaxButton("deleteBtn") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					
					orderLinesList.remove(currentOrderLine);
					updateState = false;
					
					if (!orderLinesList.isEmpty()) {
						currentOrderLine = orderLinesList.get(0);
					} else {
						currentOrderLine = new OrderLine();
					}
					
					updateEditOrderLineFormModelObject(currentOrderLine, target);
					
					target.add(listContainer1);
					target.appendJavaScript("initSelectableItems();");
					target.add(EditOrderPage.this.get("addOrderLineBtn"));
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(EditOrderLineForm.this.get("feedback"));
				}
				
				@Override
				public boolean isEnabled() {
					return !orderLinesList.isEmpty() && !updateState ;
				}
			};
			add(deleteButton);
			
			AjaxLink<Void> resetBtn = new AjaxLink<Void>("resetBtn") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					copyOrderLine(originalOrderLine, currentOrderLine);
					updateEditOrderLineFormModelObject(currentOrderLine, target);
				}
				
				@Override
				public boolean isEnabled() {
					OrderLine line = EditOrderLineForm.this.getModelObject();
					return updateState && (line.getLinenum() != null);
				}
			};
			add(resetBtn);
			
			AjaxLink<Void> cancelBtn = new AjaxLink<Void>("cancelBtn") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					
					if (currentOrderLine.getLinenum() == null) {
						
						if (!orderLinesList.isEmpty()) {
							currentOrderLine = orderLinesList.get(0);
						} else {
							currentOrderLine = new OrderLine();
						}
						
						copyOrderLine(currentOrderLine, originalOrderLine);
						
					} else {
						currentOrderLine.setDiscount(originalOrderLine.getDiscount());
						currentOrderLine.setPrice(originalOrderLine.getPrice());
						currentOrderLine.setItemnum(originalOrderLine.getItemnum());
						currentOrderLine.setQty(originalOrderLine.getQty());
						currentOrderLine.setOrderlinestatus(originalOrderLine.getOrderlinestatus());
						
						copyOrderLine(originalOrderLine, currentOrderLine);
					}
					
					updateEditOrderLineFormModelObject(currentOrderLine, target);
					
					target.add(listContainer1);
					target.appendJavaScript("initSelectableItems();");
					target.add(EditOrderPage.this.get("addOrderLineBtn"));
					
					updateState = false;
				}
				
				@Override
				public boolean isEnabled() {
					return updateState;
				}
			};
			add(cancelBtn);
			
			AjaxLink<Void> bckToSearch = new AjaxLink<Void>("btnBckToSearch") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					setResponsePage(SearchOrderPage.class);
				}
			};
			add(bckToSearch);
			
			AjaxLink<Void> btnPrevious = new AjaxLink<Void>("previousBtn") {

				@Override 
				public void onClick(AjaxRequestTarget target) {
					target.appendJavaScript("$( '#tabs' ).tabs( 'option', 'active', 1 );");
				}
			};
			add(btnPrevious);
			
			AjaxLink<Void> btnNext = new AjaxLink<Void>("nextBtn") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					target.appendJavaScript("$('#tabs').tabs('option', 'active', 3 );");
					target.add(finishEditOrderPanel);
				}
			};
			add(btnNext);
		}
		
		@Override
		protected void onModelChanged() {
			super.onModelChanged();
			get("itemnum").modelChanged();
		}
	}
	
	protected class OrderLineListView extends ListView<OrderLine> {

		public OrderLineListView(String id, IModel<List<? extends OrderLine>> model) {
			super(id,  model);
		}
		
		@Override
		protected void populateItem(ListItem<OrderLine> listItem) {
			
			if (listItem.getIndex() == 0) {
				totalOrderPrice = BigDecimal.ZERO;
			}
			
			final OrderLine orderLine = listItem.getModelObject();
			
			listItem.add(new Label("lblName", orderLine.getItemname()));
			listItem.add(new Label("lblStatus", orderLine.getOrderlinestatus()));
			listItem.add(new Label("lblPrice", bigDecimalConverter.convertToString(orderLine.getPrice() != null ? 
					orderLine.getPrice() : BigDecimal.ZERO, getSession().getLocale())));
			listItem.add(new Label("lblQty", orderLine.getQty().toString()));
			listItem.add(new Label("lblExtendedPrice", orderLine.getExtendedprice() != null ?
					bigDecimalConverter.convertToString(orderLine.getExtendedprice(), getSession().getLocale()) : ""));
			
			totalOrderPrice = totalOrderPrice.add(orderLine.getExtendedprice() != null ? orderLine.getExtendedprice() : BigDecimal.ZERO);
			
			listItem.add(new AjaxEventBehavior("onclick") {
				
				@Override
				protected void onEvent(AjaxRequestTarget target) {
					
					updateState = false;
					
					currentOrderLine = orderLine;
					copyOrderLine(currentOrderLine, originalOrderLine);
					
					updateEditOrderLineFormModelObject(currentOrderLine, target);
					
					target.add(listContainer1);
					target.appendJavaScript("initSelectableItems();");
				}
				
				@Override
				protected void updateAjaxAttributes(
						AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					attributes.setAllowDefault(true);
				}
			});
			
			if (currentOrderLine != null &&
					currentOrderLine.getLinenum().equals(orderLine.getLinenum())) {
				listItem.add(new AttributeAppender("class", Model.of(" ui-selected")));
			}
		}
	}
	
	protected void copyOrderLine(OrderLine source, OrderLine destination) {
		destination.setDiscount(source.getDiscount());
		destination.setPrice(source.getPrice());
		destination.setItemnum(source.getItemnum());
		destination.setItemname(source.getItemname());
		destination.setQty(source.getQty());
		destination.setOrderlinestatus(source.getOrderlinestatus());
		destination.setLinenum(source.getLinenum());
		destination.setExtendedprice(source.getExtendedprice());
		destination.setRowid(source.getRowId());
		
	}
	
	protected void updateEditOrderLineFormModelObject(OrderLine newModelObject, AjaxRequestTarget target) {
		EditOrderLineForm lineForm = (EditOrderLineForm) EditOrderPage.this.get("orderLineFormContainer").get("editOrderLineForm");
		lineForm.visitFormComponents(new IVisitor<FormComponent<?>, Void>() {

			public void component(FormComponent<?> object,
					IVisit<Void> visit) {
				object.clearInput();
				object.modelChanged();
			}
		});
		lineForm.setDefaultModelObject(newModelObject);
		target.add(EditOrderPage.this.get("orderLineFormContainer"));
	}
	
	@Override
	protected void onEditTab(AjaxRequestTarget target) {
		super.onEditTab(target);
	}
	
	@Override
	protected void onEditLinesTab(AjaxRequestTarget target) {
		target.add(EditOrderPage.this.get("orderLineFormContainer"));
		target.add(listContainer1);
	}
	
	@Override
	protected void onFinishTab(AjaxRequestTarget target) {
		target.add(finishEditOrderPanel);
	}
	
	@Override
	protected void onSearchTab(AjaxRequestTarget target) {
		
	}
}
