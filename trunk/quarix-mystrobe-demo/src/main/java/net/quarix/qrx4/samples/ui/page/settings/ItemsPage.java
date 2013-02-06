package net.quarix.qrx4.samples.ui.page.settings;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.CursorStates;
import net.mystrobe.client.FilterOperator;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.IDataTableDataListener;
import net.mystrobe.client.IDataTableDataSource;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.FieldValueOptionsRenderer;
import net.mystrobe.client.dynamic.config.IFieldValue;
import net.mystrobe.client.dynamic.navigation.DataTablePagesNavigationPanel;
import net.mystrobe.client.dynamic.page.ModalWindowUpdateMode;
import net.mystrobe.client.impl.FilterParameter;
import net.quarix.qrx4.samples.item.ItemFirstCategory;
import net.quarix.qrx4.samples.item.ItemSecondCategory;
import net.quarix.qrx4j.samples.AppConnector;
import net.quarix.qrx4j.samples.data.beans.Item;
import net.quarix.qrx4j.samples.data.beans.meta.ItemSchema;
import net.quarix.qrx4j.samples.data.dao.ItemDataObject;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.converter.BigDecimalConverter;

public class ItemsPage extends SettingsBasePage {
	
	protected IDataObject<Item> itemsDO;
	
	//protected SimpleDataTableViewPanel<Item> itemsTable;

	protected DataTablePagesNavigationPanel<Item> itemsNavigator;
	
	protected WebMarkupContainer listContainer;
	
	protected ItemsListView itemsListView;  
	
	protected EditItemPanel editItemPanel;
	
	protected FeedbackPanel feedback;
	
	private boolean dataSaved = false;
	
	protected BigDecimalConverter bigDecimalConverter;
	
	public ItemsPage(PageParameters parameters) {
		super(parameters);
		initPageComponents();
		
		bigDecimalConverter = new BigDecimalConverter();
	}
	
	
	private void initPageComponents() {
		
		itemsDO = new ItemDataObject();
		itemsDO.setAppConnector(AppConnector.getInstance());
		
		Form<Void> modalWindowForm = new Form<Void>("editItemWindowForm");
		ModalWindow editItemWindow = new ModalWindow("editItemModalWindow"); 
		editItemWindow.setTitle(new StringResourceModel("LBL_EDIT_ITEM", null, ItemsPage.this));
		editItemWindow.setInitialHeight(350);
		editItemWindow.setInitialWidth(500);
		editItemWindow.setWindowClosedCallback(new WindowClosedCallback() {
			public void onClose(AjaxRequestTarget target) {
				if (ItemsPage.this.dataSaved){
					
					if (ModalWindowUpdateMode.Add.equals(editItemPanel.getUpdateMode())) {
						itemsDO.clearFilters();
						
						SearchItemBean searchBean = ((SearchItemForm)get("itemSearchForm")).getModelObject();
						searchBean.setCategory1(null);
						searchBean.setCategory2(null);
						searchBean.setName(null);
						searchBean.setPrice(null);
						
						itemsDO.addFilter(new FilterParameter(ItemSchema.Cols.ITEMNUM, FilterOperator.EQ, itemsDO.getData().getItemnum()));
						itemsDO.resetDataBuffer();
						
						target.add(get("itemSearchForm"));
					}
					
					target.add(listContainer);
					target.appendJavaScript("initSelectableItems();");
					target.add(ItemsPage.this.get("editBtn"));
					target.add(ItemsPage.this.get("deleteBtn"));
				} else {
					itemsDO.cancelCRUDOperation();
				}
				ItemsPage.this.dataSaved = false;
			}
		});
		modalWindowForm.add(editItemWindow);
		add(modalWindowForm);
		
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		
		add(new SearchItemForm("itemSearchForm", 
				new CompoundPropertyModel<SearchItemBean>(new SearchItemBean())));
		
		itemsNavigator = new DataTablePagesNavigationPanel<Item>("itemsNavigator", 10, 4) {
			public void onRefreshContent(AjaxRequestTarget target) {
				target.add(listContainer);
				target.appendJavaScript("initSelectableItems();");
				target.add(ItemsPage.this.get("editBtn"));
				target.add(ItemsPage.this.get("deleteBtn"));
				target.add(ItemsPage.this.get("feedback"));
			}
		};
		add(itemsNavigator);
		
		ComponentLinker.bindDataTableNavigation(itemsNavigator, itemsDO);
		
		listContainer = new WebMarkupContainer("listHolder");
		listContainer.setOutputMarkupId(true);
		itemsListView = new ItemsListView("itemsList", Model.ofList(Collections.<Item>emptyList()));
		itemsListView.setOutputMarkupId(true);
		listContainer.add(itemsListView);
		ComponentLinker.bindDataTableData(itemsDO, itemsListView);
		add(listContainer);
		
		WebMarkupContainer noResultId = new WebMarkupContainer("noResultId"){
			@Override
			public boolean isVisible() {
				return itemsDO != null && CursorStates.NoRecordAvailable.equals(itemsDO.getCursorState());
			}
		};
		listContainer.add(noResultId);
		
		AjaxLink<Void> addBtn = new AjaxLink<Void>("addBtn") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				itemsDO.createData();
				ModalWindow editWindow = getEidtItemWindow(ModalWindowUpdateMode.Add);
				editWindow.show(target);
			}
		};
		add(addBtn);
		
		AjaxLink<Void> editBtn = new AjaxLink<Void>("editBtn") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				ModalWindow editWindow = getEidtItemWindow(ModalWindowUpdateMode.Edit);
				editWindow.show(target);
			}
			
			@Override
			public boolean isEnabled() {
				return itemsDO != null && 
						!CursorStates.NoRecordAvailable.equals(itemsDO.getCursorState()); 
			}
		};
		editBtn.setOutputMarkupId(true);
		add(editBtn);
		
		AjaxLink<Void> deleteBtn = new AjaxLink<Void>("deleteBtn") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					itemsDO.deleteData();
					
					target.add(itemsNavigator);
					target.add(ItemsPage.this.get("editBtn"));
					target.add(ItemsPage.this.get("deleteBtn"));
					target.add(listContainer);
					target.appendJavaScript("initSelectableItems();");
					
				} catch (WicketDSBLException e) {
					itemsDO.cancelCRUDOperation();
					error("Can not delete item:" + e.getBLErorMessage().getMessage() );
					
				}
				
				target.add(feedback);
			}
			
			@Override
			public boolean isEnabled() {
				return itemsDO != null && 
						!CursorStates.NoRecordAvailable.equals(itemsDO.getCursorState()); 
			}
		};
		add(deleteBtn);
		
		itemsDO.resetDataBuffer();
		
	}

	@Override
	protected SettingsTab getCurrentActiveTab() {
		return SettingsTab.Items;
	}

	
	protected void searchItems(AjaxRequestTarget target, SearchItemBean searchBean) {
		
		itemsDO.clearFilters();
		
		if (searchBean.getName()!= null ) {
			itemsDO.addFilter(new FilterParameter(ItemSchema.Cols.ITEMNAME, FilterOperator.MATCHES, "*"+searchBean.getName()+"*"));
		}
		
		if (searchBean.getPrice()!= null ) {
			itemsDO.addFilter(new FilterParameter(ItemSchema.Cols.PRICE, FilterOperator.LT, searchBean.getPrice()));
		}
		
		if (searchBean.getCategory1()!= null ) {
			itemsDO.addFilter(new FilterParameter(ItemSchema.Cols.CATEGORY1, FilterOperator.EQ, searchBean.getCategory1()));
		}
		
		if (searchBean.getCategory2()!= null ) {
			itemsDO.addFilter(new FilterParameter(ItemSchema.Cols.CATEGORY2, FilterOperator.EQ, searchBean.getCategory2()));
		}
		
		target.add(itemsNavigator);
		target.add(ItemsPage.this.get("editBtn"));
		target.add(ItemsPage.this.get("deleteBtn"));
		target.add(listContainer);
		target.appendJavaScript("initSelectableItems();");
		target.add(ItemsPage.this.get("feedback"));
		
		itemsDO.resetDataBuffer();
	}
	
	
	protected class SearchItemForm extends Form<SearchItemBean> {

		public SearchItemForm(String id, IModel<SearchItemBean> model ) {
			super(id, model);
			initFormComponents();
		}
		
		protected void initFormComponents() {
			
			setOutputMarkupId(true);
			
			FeedbackPanel feedback = new FeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);
			
			TextField<String> nameTxt = new TextField<String>("name");
			add(nameTxt);
			
			TextField<BigDecimal> priceTxt = new TextField<BigDecimal>("price");
			add(priceTxt);
			
			//category 1 drop down
			Collection<IFieldValue<String>> cat1OptionsList = ItemFirstCategory.toFieldValueList();
			Map<String, IFieldValue<String>> cat1OptionsMap = new HashMap<String, IFieldValue<String>>(cat1OptionsList.size());
			for (IFieldValue<String> fieldValue : cat1OptionsList) {
				cat1OptionsMap.put(fieldValue.getValue(), fieldValue);
			}	
			
			DropDownChoice<String> dropDownCat1 = new DropDownChoice<String>("category1", 
					new ArrayList<String>(cat1OptionsMap.keySet()), new FieldValueOptionsRenderer<String>(cat1OptionsMap));
			add(dropDownCat1);
			
			//category 2 drop down
			Collection<IFieldValue<String>> cat2OptionsList = ItemSecondCategory.toFieldValueList();
			Map<String, IFieldValue<String>> cat2OptionsMap = new HashMap<String, IFieldValue<String>>(cat2OptionsList.size());
			for (IFieldValue<String> fieldValue : cat2OptionsList) {
				cat2OptionsMap.put(fieldValue.getValue(), fieldValue);
			}	
			
			DropDownChoice<String> dropDownCat2 = new DropDownChoice<String>("category2", 
					new ArrayList<String>(cat2OptionsMap.keySet()), new FieldValueOptionsRenderer<String>(cat2OptionsMap));
			add(dropDownCat2);
			
			AjaxButton searchButton = new AjaxButton("searchBtn") {
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					searchItems(target, (SearchItemBean)form.getModelObject());
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
				}
			};
			add(searchButton);
			
			AjaxLink<Void> resetButton = new AjaxLink<Void>("resetBtn") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					SearchItemBean searchBean = new SearchItemBean();
					SearchItemForm.this.setDefaultModelObject(searchBean);
						
					searchItems(target, searchBean);
					target.add(SearchItemForm.this);
					target.add(ItemsPage.this.get("feedback"));
				}
			};
			add(resetButton);
		}
	}
	
	protected class SearchItemBean implements Serializable {
		
		protected String name;
		
		protected BigDecimal price;
		
		protected String category1;
		
		protected String category2;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public String getCategory1() {
			return category1;
		}

		public void setCategory1(String category1) {
			this.category1 = category1;
		}

		public String getCategory2() {
			return category2;
		}

		public void setCategory2(String category2) {
			this.category2 = category2;
		}
	}
	
	protected class ItemsListView extends ListView<Item> implements IDataTableDataListener<Item> {

		protected IDataTableDataSource<Item> dataSource;
		
		public ItemsListView(String id, IModel<List<? extends Item>> model) {
			super(id,  model);
		}

		public void dataAvailable(List<Item> listData, int navigatorSize) {
			if (navigatorSize == 10) {
				setModelObject(listData);
			}
		}

		public void setDataTableDataSource(IDataTableDataSource<Item> source) {
			this.dataSource = source;
			
		}

		public int getTableDataSize() {
			return getViewSize();
		}

		public int getSize() {
			return 10;
		}

		@Override
		protected void populateItem(ListItem<Item> listItem) {
			
			final Item item = listItem.getModelObject();
			
			listItem.add(new Label("lblName", item.getItemname()));
			listItem.add(new Label("lblCategory", item.getCategory1()));
			listItem.add(new Label("lblType", item.getCategory2()));
			listItem.add(new Label("lblPrice", bigDecimalConverter.convertToString(item.getPrice(), getSession().getLocale())));
			listItem.add(new Label("lblWeight", item.getWeight().toString()));
			
			listItem.add(new AjaxEventBehavior("onclick") {
				
				@Override
				protected void onEvent(AjaxRequestTarget target) {
					itemsDO.moveToRow(item.getRowId());
					target.add(ItemsPage.this.get("editBtn"));
					target.add(ItemsPage.this.get("deleteBtn"));
				}
				
				@Override
				protected void updateAjaxAttributes(
						AjaxRequestAttributes attributes) {
					super.updateAjaxAttributes(attributes);
					attributes.setAllowDefault(true);
				}
			});
			
			if (itemsDO.getData().getRowId() != null &&
					itemsDO.getData().getRowId().equals(item.getRowId())) {
				listItem.add(new AttributeAppender("class", Model.of(" ui-selected")));
			}
		}
		
	}
	
	protected ModalWindow getEidtItemWindow(ModalWindowUpdateMode updateMode) {
		
		final ModalWindow editItemWindow = (ModalWindow) get("editItemWindowForm").get("editItemModalWindow");
		
		if (editItemPanel == null) {
			editItemPanel = new EditItemPanel(editItemWindow.getContentId(), itemsDO, updateMode ) {
				
				@Override
				protected void afterEdit(AjaxRequestTarget target, boolean dataSaved) {
					if (dataSaved) {
						ItemsPage.this.dataSaved = dataSaved;
					}
					editItemWindow.close(target);
				}
			};
			editItemWindow.setContent(editItemPanel);
		} else {
			editItemPanel.updateFormModel(updateMode);
		}
		
		return editItemWindow;
	}
	
}
