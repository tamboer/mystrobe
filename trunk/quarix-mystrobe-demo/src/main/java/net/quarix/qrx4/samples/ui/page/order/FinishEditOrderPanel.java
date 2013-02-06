package net.quarix.qrx4.samples.ui.page.order;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import net.mystrobe.client.IDataObject;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.quarix.qrx4j.samples.data.beans.Order;
import net.quarix.qrx4j.samples.data.beans.OrderLine;
import net.quarix.qrx4j.samples.data.beans.meta.OrderSchema;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.convert.converter.BigDecimalConverter;

public abstract class FinishEditOrderPanel extends Panel {
	
	protected IDataObject<Order> orderDO;

	protected WebMarkupContainer listContainer;
	
	protected OrderLineListView orderLinesListView;
	
	protected List<OrderLine> orderLinesList;
	
	protected SimpleDateFormat simpleDateFormat = (SimpleDateFormat)DateFormat.getDateInstance(DateFormat.SHORT);
	
	protected BigDecimalConverter bigDecimalConverter;
	
	protected BigDecimal totalOrderPrice = BigDecimal.ZERO;

	public FinishEditOrderPanel(String id, IDataObject<Order> orderDO, List<OrderLine> orderLinesList) {
		super(id);
		setOutputMarkupId(true);
		bigDecimalConverter = new BigDecimalConverter();
		this.orderDO = orderDO;
		this.orderLinesList = orderLinesList;
		initPanelComponents();
	}

	protected void initPanelComponents() {
	
//		Label instructionsLabel = new Label(OrderSchema.Cols.INSTRUCTIONS.id(), new LoadableDetachableModel<String>() {
//
//			@Override
//			protected String load() {
//				return orderDO.getData().getInstructions();
//			}
//		});
//		add(instructionsLabel);
		
		Label carrierLabel = new Label(OrderSchema.Cols.CARRIER.id(), new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return orderDO.getData().getCarrier();
			}
		});
		add(carrierLabel);
		
		Label promiseDateLabel = new Label(OrderSchema.Cols.PROMISEDATE.id(), new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return orderDO.getData().getPromisedate() != null ? 
						simpleDateFormat.format(orderDO.getData().getPromisedate()) : "";
			}
		});
		add(promiseDateLabel);
		
		Label salesRepLabel = new Label(OrderSchema.Cols.SALESREP.id(), new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return orderDO.getData().getSalesrepname();
			}
		});
		add(salesRepLabel);
		
		Label customerLabel = new Label("customerLabel", new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return orderDO.getData().getCustname();
			}
		});
		add(customerLabel);
		
		Label orderStatusLabel = new Label(OrderSchema.Cols.ORDERSTATUS.id(), new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return orderDO.getData().getOrderstatus();
			}
		});
		add(orderStatusLabel);

		Label shipDateLabel = new Label(OrderSchema.Cols.SHIPDATE.id(), new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return orderDO.getData().getShipdate() != null ? 
						simpleDateFormat.format(orderDO.getData().getShipdate()) : "";
			}
		});
		add(shipDateLabel);
		
		Label termsLabel = new Label(OrderSchema.Cols.TERMS.id(), new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return orderDO.getData().getTerms();
			}
		});
		add(termsLabel);
		
		listContainer = new WebMarkupContainer("listHolder");
		listContainer.setOutputMarkupId(true);
		orderLinesListView = new OrderLineListView("itemsList", Model.ofList(orderLinesList));
		orderLinesListView.setOutputMarkupId(true);
		listContainer.add(orderLinesListView);
		listContainer.add(new Label("totalOrderPrice", new LoadableDetachableModel<String>() {

					@Override
					protected String load() {
						String totPrice = bigDecimalConverter.convertToString(totalOrderPrice, getSession().getLocale());
						StringResourceModel resourceModel = new StringResourceModel("LBL_TOTAL_PRICE_PARAM", FinishEditOrderPanel.this, 
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
			});
		add(listContainer);
		
		WebMarkupContainer noResultId = new WebMarkupContainer("noResultId"){
			@Override
			public boolean isVisible() {
				return orderLinesList == null || orderLinesList.isEmpty() ;
			}
		};
		listContainer.add(noResultId);
		
		Form<Void> buttonsForm = new Form<Void>("buttonsForm");
		
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		buttonsForm.add(feedback);
		
		buttonsForm.add(new AjaxButton("btnSaveChanges"){
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				
				try {
					
					onSaveChanges();
					setResponsePage(SearchOrderPage.class);
					 
				} catch (WicketDSBLException e) {
					
					error(e.getBLErorMessage().getMessage());
					onError(target, form);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e);
				}
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				add(get("buttonsForm").get("feedback"));
			}
		}); 
		
		AjaxLink<Void> btnPrevious = new AjaxLink<Void>("previousBtn") {

			@Override 
			public void onClick(AjaxRequestTarget target) {
				target.appendJavaScript("$( '#tabs' ).tabs( 'option', 'active', 2 );");
			}
		};
		buttonsForm.add(btnPrevious);
		
		AjaxLink<Void> bckToSearch = new AjaxLink<Void>("btnBckToSearch") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(SearchOrderPage.class);
			}
		};
		buttonsForm.add(bckToSearch);
		add(buttonsForm);
	}
	
	protected abstract void onSaveChanges() throws Exception;
	
	protected class OrderLineListView extends ListView<OrderLine> {

		public OrderLineListView(String id, IModel<List<? extends OrderLine>> model) {
			super(id,  model);
		}
		@Override
		protected void populateItem(ListItem<OrderLine> listItem) {
			
			final OrderLine orderLine = listItem.getModelObject();
			
			if (listItem.getIndex() == 0) {
				totalOrderPrice = BigDecimal.ZERO;
			}
			
			listItem.add(new Label("lblName", orderLine.getItemname().toString()));
			listItem.add(new Label("lblStatus", orderLine.getOrderlinestatus()));
			listItem.add(new Label("lblPrice", bigDecimalConverter.convertToString(orderLine.getPrice() != null ? 
					orderLine.getPrice() : BigDecimal.ZERO, getSession().getLocale())));
			listItem.add(new Label("lblQty", orderLine.getQty().toString()));
			listItem.add(new Label("lblExtendedPrice", orderLine.getExtendedprice() != null ?
					bigDecimalConverter.convertToString(orderLine.getExtendedprice(), getSession().getLocale()) : ""));
			
			totalOrderPrice = totalOrderPrice.add(orderLine.getExtendedprice() != null ? orderLine.getExtendedprice() : BigDecimal.ZERO);
		}
	}
}
