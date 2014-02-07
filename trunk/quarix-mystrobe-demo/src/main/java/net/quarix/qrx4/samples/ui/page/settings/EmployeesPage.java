package net.quarix.qrx4.samples.ui.page.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.dynamic.config.DynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig.FieldType;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig.Property;
import net.mystrobe.client.dynamic.navigation.CRUDAjaxOperationsPanel;
import net.mystrobe.client.dynamic.navigation.CRUDOperationsPanel.CRUDButton;
import net.mystrobe.client.dynamic.navigation.DataTableNavigationPanel;
import net.mystrobe.client.dynamic.panel.DynamicFormDataViewPanel;
import net.mystrobe.client.dynamic.table.view.DataTableColumn;
import net.mystrobe.client.dynamic.table.view.IMyStrobeColumn;
import net.mystrobe.client.dynamic.table.view.MyStrobeColumn;
import net.mystrobe.client.dynamic.table.view.SimpleDataTableViewPanel;
import net.quarix.qrx4j.samples.Qrx4jSampleApplication;
import net.quarix.qrx4j.samples.data.beans.Department;
import net.quarix.qrx4j.samples.data.beans.Employee;
import net.quarix.qrx4j.samples.data.beans.State;
import net.quarix.qrx4j.samples.data.beans.meta.DepartmentSchema;
import net.quarix.qrx4j.samples.data.beans.meta.EmployeeSchema;
import net.quarix.qrx4j.samples.data.beans.meta.StateSchema;
import net.quarix.qrx4j.samples.data.dao.DepartmentDataObject;
import net.quarix.qrx4j.samples.data.dao.EmployeeDataObject;
import net.quarix.qrx4j.samples.data.dao.StateDataObject;
import net.quarix.qrx4j.samples.settings.Position;
import org.apache.wicket.Application;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class EmployeesPage extends SettingsBasePage {

	private DynamicFormDataViewPanel<Employee> employeeView;
	
	private CRUDAjaxOperationsPanel<Employee> operationsPanel;
	
	private SimpleDataTableViewPanel<Employee> employeeTable;
	
	private DataTableNavigationPanel<Employee> employeeNavigator;

	private IDataObject<Employee> employeeDO;
	
	
	public EmployeesPage(PageParameters parameters) {
		super(parameters);
		initComponents();
	}
	
	private void initComponents(){
		Qrx4jSampleApplication application = (Qrx4jSampleApplication)Application.get();
		employeeDO = new EmployeeDataObject(application.getMystrobeConfig(), application.getAppName());
		
		Form<Employee> employeeForm = new Form<Employee>("employeeForm");
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		employeeForm.add(feedback);
		
		String [] employeeViewVisibleColumns = new String [] {
				EmployeeSchema.Cols.FIRSTNAME.id(), EmployeeSchema.Cols.LASTNAME.id(),
				EmployeeSchema.Cols.ADDRESS.id(), EmployeeSchema.Cols.ADDRESS2.id(),
				EmployeeSchema.Cols.POSITION.id(), EmployeeSchema.Cols.DEPTCODE.id(),
				EmployeeSchema.Cols.HOMEPHONE.id(), EmployeeSchema.Cols.WORKPHONE.id(),
				EmployeeSchema.Cols.STATE.id(), EmployeeSchema.Cols.POSTALCODE.id(),
				EmployeeSchema.Cols.STARTDATE.id(),
		};
		
		IDynamicFormConfig<Employee> employeeViewConfig = new DynamicFormConfig<Employee>(new EmployeeSchema(), employeeViewVisibleColumns, true);
		
		DepartmentDataObject departmentDO = new DepartmentDataObject(application.getMystrobeConfig(), application.getAppName());
		
		String [] departmentVisibleColumns = new String [] {DepartmentSchema.Cols.DEPTNAME.id(), DepartmentSchema.Cols.DEPTCODE.id()};
		IDynamicFormConfig<Department> departmentDisplay = new DynamicFormConfig<Department>(new DepartmentSchema(), departmentVisibleColumns, true );
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.DEPTCODE, Property.SelectableFieldValue, Boolean.TRUE);
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.DEPTCODE, Property.LinkedColumnName, DepartmentSchema.Cols.DEPTCODE.id());
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.DEPTCODE, Property.LinkedDataObject, departmentDO);
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.DEPTCODE, Property.SelectRecordTableConfig, departmentDisplay);
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.DEPTCODE, Property.VisibleColumnName, DepartmentSchema.Cols.DEPTNAME.id());
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.DEPTCODE, Property.DescriptionColumn, EmployeeSchema.Cols.DEPTNAME.id());
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.DEPTCODE, Property.Required, true);
		
		StateDataObject stateDO = new StateDataObject(application.getMystrobeConfig(), application.getAppName());
		String [] stateVisibleColumns = new String [] {StateSchema.Cols.STATENAME.id(), StateSchema.Cols.STATECODE.id(),
				StateSchema.Cols.REGION.id()};
		IDynamicFormConfig<State> stateDisplay = new DynamicFormConfig<State>(new StateSchema(), stateVisibleColumns, true );
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.STATE, Property.SelectableFieldValue, Boolean.TRUE);
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.STATE, Property.LinkedColumnName, StateSchema.Cols.STATECODE.id());
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.STATE, Property.LinkedDataObject, stateDO);
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.STATE, Property.SelectRecordTableConfig, stateDisplay);
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.STATE, Property.VisibleColumnName, StateSchema.Cols.STATENAME.id());
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.STATE, Property.DescriptionColumn, EmployeeSchema.Cols.STATENAME.id());
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.STATE, Property.Required, true);
		
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.POSITION, Property.Type, FieldType.DropDown);
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.POSITION, Property.ValuesList, Position.toFieldValueList());
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.POSITION, Property.Required, true);
		
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.FIRSTNAME, Property.Required, true);
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.LASTNAME, Property.Required, true);
		employeeViewConfig.setColumnProperty(EmployeeSchema.Cols.STARTDATE, Property.Required, true);
		
		employeeView = new DynamicFormDataViewPanel<Employee>("employeeView", new Employee(), employeeViewConfig);
		
		employeeForm.add(employeeView);
		
		operationsPanel = new CRUDAjaxOperationsPanel<Employee>("crudOperations", employeeForm, new HashSet<CRUDButton>(
				Arrays.<CRUDButton>asList(new CRUDButton [] {CRUDButton.Add, CRUDButton.Edit, CRUDButton.Save, CRUDButton.Cancel, CRUDButton.Delete, CRUDButton.Reset}))){
			
			@Override
			protected void onAdd(AjaxRequestTarget target) {
				super.onAdd(target);
				target.add(operationsPanel);
				target.add(employeeView);
			}
			
			@Override
			protected void onDelete(AjaxRequestTarget target) {
				super.onDelete(target);
				target.add(employeeView);
				target.add(employeeTable);
				target.add(employeeNavigator);
				target.add(operationsPanel);
				target.add(EmployeesPage.this.get("employeeForm").get("feedback"));
			}
			
			@Override
			protected void onCancel(AjaxRequestTarget target) {
				super.onCancel(target);
				target.add(operationsPanel);
				target.add(employeeView);
				target.add(EmployeesPage.this.get("employeeForm").get("feedback"));
			}
			
			@Override
			protected void onEdit(AjaxRequestTarget target) {
				super.onCancel(target);
				target.add(operationsPanel);
				target.add(employeeView);
			}
			
			@Override
			protected void onSave(AjaxRequestTarget target) {
				super.onCancel(target);
				target.add(operationsPanel);
				target.add(employeeView);
				target.add(employeeTable);
				target.add(employeeNavigator);
				target.add(EmployeesPage.this.get("employeeForm").get("feedback"));
			}
			
			@Override
			protected void onReset(AjaxRequestTarget target) {
				super.onCancel(target);
				target.add(EmployeesPage.this.get("employeeForm").get("feedback"));
				target.add(employeeView);
				target.add(EmployeesPage.this.get("employeeForm").get("feedback"));
			}
			
			@Override
			protected void onSaveError(AjaxRequestTarget target, Form<?> form) {
				super.onCancel(target);
				target.add(employeeView);
				target.add(EmployeesPage.this.get("employeeForm").get("feedback"));
				
			}
		};
		employeeForm.add(operationsPanel);
		add(employeeForm);
		
		
		String [] employeeTableVisibleColumns = new String [] { EmployeeSchema.Cols.BIRTHDATE.id(),
				EmployeeSchema.Cols.ADDRESS.id(), 
				EmployeeSchema.Cols.POSITION.id(),
				EmployeeSchema.Cols.HOMEPHONE.id(),
				EmployeeSchema.Cols.STARTDATE.id(),
		};
		IDynamicFormConfig<Employee> tableConfig = new DynamicFormConfig<Employee>(new EmployeeSchema(), employeeTableVisibleColumns, true);
		List<DataTableColumn<Employee, ?>> additionalColumns = new ArrayList<DataTableColumn<Employee, ?>>(2);
		additionalColumns.add(new DataTableColumn<Employee, Void>(0, getNameColumn()));
		
		employeeTable = new SimpleDataTableViewPanel<Employee>("employeeTable", tableConfig, 10, additionalColumns){
			@Override
			protected void onDataChanged(AjaxRequestTarget target) {
				super.onDataChanged(target);
				target.add(employeeView);
			}
		};
		add(employeeTable);
		
		employeeNavigator = new DataTableNavigationPanel<Employee>("employeeNavigator", 10);
		add(employeeNavigator);
	
		ComponentLinker.bindData(employeeDO, employeeView, null);
		ComponentLinker.bindDataTableData(employeeDO, employeeTable);
		ComponentLinker.bindDataTableNavigation(employeeNavigator, employeeDO);
		
		ComponentLinker.bindUpdate(employeeView, employeeDO);
		ComponentLinker.bindUpdateUI(operationsPanel, employeeView);
		ComponentLinker.bindState(employeeDO, operationsPanel);
		ComponentLinker.bindState(employeeView, operationsPanel);
		
		employeeDO.resetDataBuffer();
	}
	

	@Override
	protected SettingsTab getCurrentActiveTab() {
		return SettingsTab.Employees;
	}

	private IMyStrobeColumn<Employee, Void> getNameColumn () {
		
		return new MyStrobeColumn<Employee, Void>() {

			public Component getHeader(String componentId) {
				return new Label(componentId, new StringResourceModel("LBL_NAME", null, EmployeesPage.this));
			}

			public Void getSortProperty() {
				return null;
			}

			public boolean isSortable() {
				return false;
			}

			public void populateItem(Item<ICellPopulator<Employee>> cellItem,
					String componentId, IModel<Employee> rowModel) {
				
				StringBuilder name = new StringBuilder(rowModel.getObject().getFirstname());
				name.append(" ").append(rowModel.getObject().getLastname());
				
				cellItem.add(new Label(componentId, Model.of(name.toString())));
				
			}

			public void detach() {
				// TODO Auto-generated method stub
				
			}
		};
	}
}
