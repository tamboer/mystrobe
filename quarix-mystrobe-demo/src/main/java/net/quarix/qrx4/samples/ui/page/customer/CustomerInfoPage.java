package net.quarix.qrx4.samples.ui.page.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.FilterOperator;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig.Property;
import net.mystrobe.client.dynamic.navigation.DataTablePagesNavigationPanel;
import net.mystrobe.client.dynamic.page.ModalWindowUpdateMode;
import net.mystrobe.client.dynamic.panel.DynamicFormDataViewPanel;
import net.mystrobe.client.dynamic.panel.EditRecordModalPanel;
import net.mystrobe.client.dynamic.table.view.DataRowActionsToolbarColumn;
import net.mystrobe.client.dynamic.table.view.DataRowActionsToolbarColumn.DataRecordAction;
import net.mystrobe.client.dynamic.table.view.DataTableColumn;
import net.mystrobe.client.dynamic.table.view.IMyStrobeColumn;
import net.mystrobe.client.dynamic.table.view.SimpleDataTableViewPanel;
import net.mystrobe.client.filter.SearchFilter;
import net.mystrobe.client.impl.FilterParameter;
import net.mystrobe.client.util.StringUtil;
import net.quarix.qrx4.samples.ui.page.order.SearchOrderPage;
import net.quarix.qrx4j.samples.Qrx4jSampleApplication;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.beans.SalesRep;
import net.quarix.qrx4j.samples.data.beans.State;
import net.quarix.qrx4j.samples.data.beans.meta.CustomerSchema;
import net.quarix.qrx4j.samples.data.beans.meta.SalesRepSchema;
import net.quarix.qrx4j.samples.data.beans.meta.StateSchema;
import net.quarix.qrx4j.samples.data.dao.CustomerDataObject;
import net.quarix.qrx4j.samples.data.dao.SalesRepDataObject;
import net.quarix.qrx4j.samples.data.dao.StateDataObject;
import org.apache.wicket.Application;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class CustomerInfoPage extends CustomerBasePage {

    private static final long serialVersionUID = 4187863307454812815L;
    private SimpleDataTableViewPanel<Customer> tableView;
    private DataTablePagesNavigationPanel<Customer> pagesNavigator;
    private DynamicFormDataViewPanel<Customer> custForm;
    private CustomerDataObject customerDo;

    public CustomerInfoPage(final PageParameters parameters) {

        super(parameters);

        Form<Void> modalWindowForm = new Form<Void>("editCustomerForm");
        ModalWindow editCustomerWindow = new ModalWindow("editCustomerWindow");
        editCustomerWindow.setInitialHeight(350);
        editCustomerWindow.setInitialWidth(600);

        editCustomerWindow.setWindowClosedCallback(new WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                refreshPageComponents(target);
                customerDo.cancelCRUDOperation();
            }
        });

        modalWindowForm.add(editCustomerWindow);
        add(modalWindowForm);

        Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
        customerDo = new CustomerDataObject(application.getMystrobeConfig(), application.getAppName()) {
            @Override
            protected Serializable getSearchBean() {
                return getSearchForm().getModelObject();
            }
        };

        initSearchForm();

        //customer table visible columns 
        String[] visibleColumns = new String[]{CustomerSchema.Cols.NAME.id(), CustomerSchema.Cols.CONTACT.id(),
            CustomerSchema.Cols.STATENAME.id(), CustomerSchema.Cols.COUNTRY.id(),
            CustomerSchema.Cols.CITY.id(), CustomerSchema.Cols.COMMENTS.id()};

        IDynamicFormConfig<Customer> custFormConfig = new DynamicFormConfig<Customer>(customerDo.getSchema(), visibleColumns, true);

        List<DataTableColumn<Customer, ?>> additionalColumns = new ArrayList<DataTableColumn<Customer, ?>>(1);
        additionalColumns.add(new DataTableColumn<Customer, Void>(0, getActionsColumn()));

        tableView = new SimpleDataTableViewPanel<Customer>("tableId", custFormConfig, 10, additionalColumns) {
            @Override
            protected void onSortClick(AjaxRequestTarget target) {
                super.onSortClick(target);
                target.add(pagesNavigator);
                target.add(custForm);
            }

            @Override
            protected void onDataChanged(AjaxRequestTarget target) {
                super.onDataChanged(target);
                target.add(custForm);
                target.add(getSearchForm().get("feedback"));
            }
        };

        add(tableView);
        ComponentLinker.bindDataTableData(customerDo, tableView);
        ComponentLinker.bindSort(tableView, customerDo);

        pagesNavigator = new DataTablePagesNavigationPanel<Customer>("navigatorId", 10, 4) {
            private static final long serialVersionUID = -4658560448403518488L;

            @Override
            public void onRefreshContent(AjaxRequestTarget target) {
                super.onRefreshContent(target);
                target.add(tableView);
                target.add(custForm);
                target.add(getSearchForm().get("feedback"));
            }
        };

        add(pagesNavigator);

        ComponentLinker.bindDataTableNavigation(pagesNavigator, customerDo);

        //customer view visible columns
        String[] visibleDetailColumns = new String[]{CustomerSchema.Cols.SALESREPNAME.id(),
            CustomerSchema.Cols.EMAILADDRESS.id(), CustomerSchema.Cols.PHONE.id(),
            CustomerSchema.Cols.POSTALCODE.id(), CustomerSchema.Cols.TERMS.id(),
            CustomerSchema.Cols.FAX.id(), CustomerSchema.Cols.DISCOUNT.id()};

        IDynamicFormConfig<Customer> custDetailsConfig = new DynamicFormConfig<Customer>(customerDo.getSchema(), visibleDetailColumns, true);
        custForm = new DynamicFormDataViewPanel<Customer>("dynamicForm", new Customer(), custDetailsConfig);

        add(custForm);
        ComponentLinker.bindData(customerDo, custForm, null);

        customerDo.fetchFirst();

//    	AjaxLink<Void> customerInfoLink = new AjaxLink<Void>("customerInfo"){
//
//			@Override
//			public void onClick(AjaxRequestTarg et target) {
//				target.appendJavaScript("javascript:newPopup('CustomerPageInfo.html');");
//			}
//    		
//    	}; 
//    	add(customerInfoLink);
    }

    private IMyStrobeColumn<Customer, Void> getActionsColumn() {

        DataRecordAction[] actions = new DataRecordAction[]{DataRecordAction.Edit, DataRecordAction.Delete, DataRecordAction.Go};

        return new DataRowActionsToolbarColumn<Customer>("", actions, customerDo) {
            @Override
            protected void onEditRecordClicked(AjaxRequestTarget target,
                    IModel<Customer> model) {

                super.onEditRecordClicked(target, model);

                target.add(tableView);
                target.add(custForm);

                ModalWindow editCustomerWindow = getEditCustomerWindow(ModalWindowUpdateMode.Edit);
                editCustomerWindow.show(target);
            }

            @Override
            protected void onDeleteRecordClicked(AjaxRequestTarget target,
                    IModel<Customer> model) {

                super.onDeleteRecordClicked(target, model);
                customerDo.moveToRow(model.getObject().getRowId());
                try {
                    customerDo.deleteData();
                } catch (WicketDSBLException e) {
                    error(e.getBLErorMessage().getMessage());
                    target.add(getSearchForm().get("feedback"));
                }

                target.add(tableView);
                target.add(pagesNavigator);
            }

            @Override
            protected void onGoRecordClicked(AjaxRequestTarget target,
                    IModel<Customer> model) {
                super.onGoRecordClicked(target, model);

                PageParameters parameters = new PageParameters();
                parameters.add("cust_name", model.getObject().getName());
                parameters.add("cust_num", model.getObject().getCustnum());
                setResponsePage(new SearchOrderPage(parameters));
            }

            @Override
            public void populateItem(Item<ICellPopulator<Customer>> cellItem,
                    String componentId, IModel<Customer> rowModel) {
                super.populateItem(cellItem, componentId, rowModel);

                cellItem.add(new AttributeAppender("width", Model.of("90px")));
            }
        };
    }

    protected void refreshPageComponents(AjaxRequestTarget target) {
        target.add(tableView);
        target.add(pagesNavigator);
        target.add(custForm);
        target.add(getSearchForm());
    }

    private ModalWindow getEditCustomerWindow(ModalWindowUpdateMode updateMode) {

        ModalWindow editCustomerWindow = (ModalWindow) get("editCustomerForm").get("editCustomerWindow");

        //edit customer columns
        String[] visibleColumns = new String[]{CustomerSchema.Cols.NAME.id(), CustomerSchema.Cols.CONTACT.id(),
            CustomerSchema.Cols.STATE.id(), CustomerSchema.Cols.COUNTRY.id(),
            CustomerSchema.Cols.CITY.id(), CustomerSchema.Cols.EMAILADDRESS.id(),
            CustomerSchema.Cols.SALESREP.id(), CustomerSchema.Cols.PHONE.id(),
            CustomerSchema.Cols.POSTALCODE.id(), CustomerSchema.Cols.TERMS.id(),
            CustomerSchema.Cols.FAX.id(), CustomerSchema.Cols.COMMENTS.id()};
        IDynamicFormConfig<Customer> editPanelConfig = new DynamicFormConfig<Customer>(new CustomerSchema(), visibleColumns, true);

        //configure sales rep. as a lookup component 
        Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
        SalesRepDataObject salesRepDO = new SalesRepDataObject(application.getMystrobeConfig(), application.getAppName());
        String[] salesRepVisibleColumns = new String[]{SalesRepSchema.Cols.REPNAME.id(), SalesRepSchema.Cols.SALESREP.id(),
            SalesRepSchema.Cols.REGION.id()};
        IDynamicFormConfig<SalesRep> salesRepDisplay = new DynamicFormConfig<SalesRep>(new SalesRepSchema(), salesRepVisibleColumns, true);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.SALESREP, Property.SelectableFieldValue, Boolean.TRUE);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.SALESREP, Property.LinkedColumnName, SalesRepSchema.Cols.SALESREP.id());
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.SALESREP, Property.LinkedDataObject, salesRepDO);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.SALESREP, Property.SelectRecordTableConfig, salesRepDisplay);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.SALESREP, Property.VisibleColumnName, SalesRepSchema.Cols.REPNAME.id());
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.SALESREP, Property.Required, true);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.SALESREP, Property.DescriptionColumn, CustomerSchema.Cols.SALESREPNAME.id());

        //configure state as a lookup component
        StateDataObject stateDO = new StateDataObject(application.getMystrobeConfig(), application.getAppName());
        String[] stateVisibleColumns = new String[]{StateSchema.Cols.STATENAME.id(), StateSchema.Cols.STATECODE.id(),
            StateSchema.Cols.REGION.id()};
        IDynamicFormConfig<State> stateDisplay = new DynamicFormConfig<State>(new StateSchema(), stateVisibleColumns, true);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.STATE, Property.SelectableFieldValue, Boolean.TRUE);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.STATE, Property.LinkedColumnName, StateSchema.Cols.STATECODE.id());
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.STATE, Property.LinkedDataObject, stateDO);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.STATE, Property.SelectRecordTableConfig, stateDisplay);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.STATE, Property.VisibleColumnName, StateSchema.Cols.STATENAME.id());
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.STATE, Property.DescriptionColumn, CustomerSchema.Cols.STATENAME.id());

        //set required fields
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.NAME, Property.Required, true);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.CONTACT, Property.Required, true);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.CITY, Property.Required, true);
        editPanelConfig.setColumnProperty(CustomerSchema.Cols.PHONE, Property.Required, true);

        EditRecordModalPanel<Customer> editCustomerPanel = new EditRecordModalPanel<Customer>(editCustomerWindow.getContentId(),
                customerDo, editCustomerWindow, editPanelConfig, updateMode, null) {
            @Override
            public IModel<String> getTitleModel() {
                return Model.of("Edit Customer");
            }

            @Override
            protected void onSaveSuccess(AjaxRequestTarget target, ModalWindowUpdateMode updateMode) {
                if (ModalWindowUpdateMode.Add.equals(updateMode)) {
                    //display new created customer
                    customerDo.clearFilters();
                    customerDo.addFilter(new FilterParameter(CustomerSchema.Cols.CUSTNUM, FilterOperator.EQ, customerDo.getData().getCustnum()));
                    customerDo.resetDataBuffer();

                    CustomerSearchBean searchBean = getSearchForm().getModelObject();
                    searchBean.setName(null);
                    searchBean.setCountry(null);
                }
            }
        };

        editCustomerWindow.setContent(editCustomerPanel);

        return editCustomerWindow;
    }

    protected void initSearchForm() {

        Form<CustomerSearchBean> searchForm = new Form<CustomerSearchBean>("searchForm",
                new CompoundPropertyModel<CustomerSearchBean>(new CustomerSearchBean()));
        searchForm.setOutputMarkupId(true);

        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        searchForm.add(feedback);

        TextField<String> customerNameTextField = new TextField<String>("name");
        searchForm.add(customerNameTextField);

        TextField<String> countryTextField = new TextField<String>("country");
        searchForm.add(countryTextField);

        AjaxButton searchButton = new AjaxButton("searchButton") {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit();

                customerDo.clearFilters();

                CustomerSearchBean searchBean = getSearchForm().getModelObject();

                if (!StringUtil.isNullOrEmpty(searchBean.getName())) {
                    customerDo.addFilter(new FilterParameter(CustomerSchema.Cols.NAME, FilterOperator.MATCHES, "*" + searchBean.getName() + "*"));
                }

                if (!StringUtil.isNullOrEmpty(searchBean.getCountry())) {
                    customerDo.addFilter(new FilterParameter(CustomerSchema.Cols.COUNTRY, FilterOperator.MATCHES, "*" + searchBean.getCountry() + "*"));
                }

                customerDo.resetDataBuffer();

                refreshPageComponents(target);
            }

            @Override
            public void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError();
            }
        };

        AjaxButton resetButton = new AjaxButton("resetButton") {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit();

                customerDo.clearFilters();

                CustomerSearchBean searchBean = getSearchForm().getModelObject();
                searchBean.setName(null);
                searchBean.setCountry(null);

                customerDo.resetDataBuffer();

                refreshPageComponents(target);
            }
        };

        AjaxLink<Void> addButton = new AjaxLink<Void>("addButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {

                Customer customer = new Customer();
                customerDo.createData(customer);

                ModalWindow editCustomerWindow = getEditCustomerWindow(ModalWindowUpdateMode.Add);
                editCustomerWindow.show(target);
            }
        };

        searchForm.add(searchButton);
        searchForm.add(resetButton);
        searchForm.add(addButton);

        add(searchForm);
    }

    protected Form<CustomerSearchBean> getSearchForm() {
        return (Form<CustomerSearchBean>) get("searchForm");
    }

    public class CustomerSearchBean implements Serializable {

        @SearchFilter(name = "name")
        public String name;
        @SearchFilter(name = "country")
        public String country;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    @Override
    protected CustomerTab getCurrentActiveTab() {
        return CustomerTab.Search;
    }
}
