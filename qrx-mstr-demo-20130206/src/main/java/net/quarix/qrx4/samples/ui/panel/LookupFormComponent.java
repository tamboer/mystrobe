package net.quarix.qrx4.samples.ui.panel;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.page.SelectRecordModalPanel;
import net.mystrobe.client.util.DataBeanUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

public class LookupFormComponent<T, S extends IDataBean> extends FormComponentPanel<T> {

	//text column name
	private String textColumnName;
	
	//id column name of 
	private String idColumnName;
	
	protected String displayText;
	
	private SelectRecordModalPanel<S> selectRecordPanel;
	
	private IDataObject<S> lookupDO;
	
	private IDynamicFormConfig<S> lookupTableConfig;
	
	protected AjaxLink<T> lookupLink;
	
	protected boolean changingModel = false;
	
	public LookupFormComponent(String id, IModel<T> model, String textColumnName,
				String idColumnName, IDataObject<S> lookupDataObject, IDynamicFormConfig<S> lookupTableConfig, String initDisplayText) {
		
		super(id, model);
		
		this.displayText = initDisplayText;
		this.idColumnName = idColumnName;
		this.textColumnName = textColumnName;
		this.lookupDO = lookupDataObject;
		this.lookupTableConfig = lookupTableConfig;
		
		initComponent();
	}

	
	protected void initComponent() {
		
		ModalWindow selectRecordWindow = new ModalWindow("selectRecordWindow");
		selectRecordWindow.setInitialWidth(600);
		selectRecordWindow.setInitialHeight(800);
		selectRecordWindow.setTitle(Model.of("Select Record"));
		add(selectRecordWindow);
		
		selectRecordWindow.setWindowClosedCallback(new WindowClosedCallback() {
			public void onClose(AjaxRequestTarget target) {
				if (selectRecordPanel.isSelected()) {
					T selectedId = (T) DataBeanUtil.getFieldValue(selectRecordPanel.getSelectedData(),
							idColumnName, null) ;
					
					changingModel = true;
					
					getModel().setObject(selectedId);
					lookupLink.setModelObject(selectedId);
					
					changingModel = false;
					
					displayText = DataBeanUtil.getFieldValue(selectRecordPanel.getSelectedData(),
							textColumnName, null).toString();
				
					target.add(LookupFormComponent.this.get("text"));
					
					onNewDataSelected((S)selectRecordPanel.getSelectedData());
				}
			}
		});
		
		
		Label text = new Label("text", new LoadableDetachableModel<String>() {
			@Override
			protected String load() {
				return (LookupFormComponent.this.getModelObject() != null &&  displayText != null) ? displayText : "" ;
			}
		});
		text.setOutputMarkupId(true);
		add(text);
		
		lookupLink = new AjaxLink<T>("lookUpLinkId", getModel()) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				getSelectRecordWindow().show(target);
			}
		};
		add(lookupLink);
	}
	
	private ModalWindow getSelectRecordWindow() {
		ModalWindow selectRecordWindow = (ModalWindow) get("selectRecordWindow");
		
		if ( selectRecordPanel == null) {
			selectRecordPanel = new SelectRecordModalPanel<S>(selectRecordWindow.getContentId(),  
					selectRecordWindow, lookupDO, lookupTableConfig); 
			selectRecordWindow.setContent(selectRecordPanel);
			
			try {
				selectRecordPanel.initializePageComponents();
			} catch (WicketDSBLException e) {
				//e.printStackTrace();
			}
		}
		
		lookupDO.resetDataBuffer();
		
		return selectRecordWindow;
	}
	
	@Override
	protected void onModelChanging() {
		// TODO Auto-generated method stub
		super.onModelChanging();
	}
	
	@Override
	protected void convertInput() {
		if (lookupLink.getModelObject() != null) {
			T input = lookupLink.getModelObject();
			setConvertedInput(input);
		} else {
			setModelObject(null);
		}
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if (getModelObject() == null) {
			get("text").setDefaultModelObject(null);
		} 
	}
	
	protected void onNewDataSelected(S selectedData) {
		
	}
}
