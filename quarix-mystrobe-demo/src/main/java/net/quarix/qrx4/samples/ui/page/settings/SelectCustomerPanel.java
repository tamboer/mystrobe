package net.quarix.qrx4.samples.ui.page.settings;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.navigation.DataTablePagesNavigationPanel;
import net.mystrobe.client.dynamic.table.view.SimpleDataTableViewPanel;
import net.quarix.qrx4j.samples.Qrx4jSampleApplication;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.quarix.qrx4j.samples.data.beans.meta.CustomerSchema;
import net.quarix.qrx4j.samples.data.dao.CustomerDataObject;

import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class SelectCustomerPanel extends Panel {

	protected IDataObject<Customer> customerDO;
	
	protected SimpleDataTableViewPanel<Customer> customerTable;

	protected DataTablePagesNavigationPanel<Customer> customerNavigator;
	
	public SelectCustomerPanel(String id) {
		super(id);
		
		initPanelComponents();
	}

	protected void initPanelComponents() {
                Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
		customerDO = new CustomerDataObject(application.getMystrobeConfig(), application.getAppName());
		
		String [] visiblecolumns = new String [] { CustomerSchema.Cols.NAME.id(),
				CustomerSchema.Cols.ADDRESS.id(), CustomerSchema.Cols.CITY.id(),
				CustomerSchema.Cols.CONTACT.id(), CustomerSchema.Cols.COUNTRY.id()
		};
		
		IDynamicFormConfig<Customer> config = new DynamicFormConfig<Customer>(new CustomerSchema(), visiblecolumns, true);
		
		customerTable = new SimpleDataTableViewPanel<Customer>("table", config, 10) {
			
			protected void onDataChanged(AjaxRequestTarget target) {
				super.onDataChanged(target);
			}
		};
		add(customerTable);
		
		customerNavigator = new DataTablePagesNavigationPanel<Customer>("navigator", 10, 4) {
			public void onRefreshContent(AjaxRequestTarget target) {
				target.add(customerTable);
			};
		};
		add(customerNavigator);
		
		ComponentLinker.bindDataTableData(customerDO, customerTable);
		ComponentLinker.bindDataTableNavigation(customerNavigator, customerDO);
		
		AjaxLink<Void> selectButton = new AjaxLink<Void>("selectBtn") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				onDataSelected(customerDO.getData(), target);
			}
		};
		add(selectButton);
		
		AjaxLink<Void> cancelButton = new AjaxLink<Void>("cancelBtn") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				onCancelSelectData(target);
				
			}
		};
		add(cancelButton);
		
		customerDO.resetDataBuffer();
	}
	
	public void reloadPanel () {
		customerDO.resetDataBuffer();
	}
	
	protected abstract void onDataSelected(Customer customer, AjaxRequestTarget target);
	
	protected abstract void onCancelSelectData(AjaxRequestTarget target);
}
