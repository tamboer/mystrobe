package net.quarix.qrx4.samples.ui;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig;
import net.mystrobe.client.dynamic.navigation.DataTablePagesNavigationPanel;
import net.mystrobe.client.dynamic.table.view.SimpleDataTableViewPanel;
import net.quarix.qrx4j.samples.AppConnector;
import net.quarix.qrx4j.samples.BasePage;
import net.quarix.qrx4j.samples.HeaderLink;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.beans.meta.CustomerSchema;
import net.quarix.qrx4j.samples.data.dao.CustomerDataObject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class DataTable extends BasePage {

	private static final long serialVersionUID = 1L;
	
	private SimpleDataTableViewPanel<Customer> tableView;
	
	private DataTablePagesNavigationPanel<Customer> pagesNavigator;
	
	private CustomerDataObject customerDo;
	
    public DataTable(final PageParameters parameters) {
    	
    	customerDo = new CustomerDataObject();
    	customerDo.setAppConnector(AppConnector.getInstance());
    	
    	IDynamicFormConfig<Customer> custFormConfig = new DynamicFormConfig<Customer>(
    			customerDo.getSchema(), false);
    	
    	custFormConfig.setColumnProperty(CustomerSchema.Cols.ADDRESS2, IDynamicFormFieldConfig.Property.Visible, Boolean.FALSE);
    	custFormConfig.setColumnProperty(CustomerSchema.Cols.CUSTNUM, IDynamicFormFieldConfig.Property.Visible, Boolean.FALSE);
    	custFormConfig.setColumnProperty(CustomerSchema.Cols.SALESREP, IDynamicFormFieldConfig.Property.Visible, Boolean.FALSE);
    	custFormConfig.setColumnProperty(CustomerSchema.Cols.TERMS, IDynamicFormFieldConfig.Property.Visible, Boolean.FALSE);
    	custFormConfig.setColumnProperty(CustomerSchema.Cols.BALANCE, IDynamicFormFieldConfig.Property.Visible, Boolean.FALSE);
    	custFormConfig.setColumnProperty(CustomerSchema.Cols.CREDITLIMIT, IDynamicFormFieldConfig.Property.Visible, Boolean.FALSE);
    	custFormConfig.setColumnProperty(CustomerSchema.Cols.PHONE, IDynamicFormFieldConfig.Property.Visible, Boolean.FALSE);
    	custFormConfig.setColumnProperty(CustomerSchema.Cols.DISCOUNT, IDynamicFormFieldConfig.Property.Visible, Boolean.FALSE);
    	custFormConfig.setColumnProperty(CustomerSchema.Cols.POSTALCODE, IDynamicFormFieldConfig.Property.Visible, Boolean.FALSE);
    	
    	tableView = new SimpleDataTableViewPanel<Customer>("tableId",
    			custFormConfig, 10){

				private static final long serialVersionUID = 8220696030963450633L;

				@Override
				protected void onSortClick(AjaxRequestTarget target) {
					super.onSortClick(target);
					target.add(pagesNavigator);
				}
    	};
    	add(tableView);
    	ComponentLinker.bindDataTableData(customerDo, tableView);
    	ComponentLinker.bindSort(tableView, customerDo);
    	
    	pagesNavigator = new DataTablePagesNavigationPanel<Customer>("navigatorId", 
    			10, 6) {

				private static final long serialVersionUID = -4658560448403518488L;

				@Override
				public void onRefreshContent(AjaxRequestTarget target) {
					super.onRefreshContent(target);
					target.add(tableView);
				}
    	};
    	
    	add(pagesNavigator);
    	ComponentLinker.bindDataTableNavigation(pagesNavigator, customerDo);
    	
    	customerDo.resetDataTableNavigation(pagesNavigator, 10);
    }

	@Override
	protected HeaderLink getHeaderActiveLink() {
		// TODO Auto-generated method stub
		return null;
	}
}