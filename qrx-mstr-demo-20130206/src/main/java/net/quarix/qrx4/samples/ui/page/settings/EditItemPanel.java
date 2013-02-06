package net.quarix.qrx4.samples.ui.page.settings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mystrobe.client.IDataObject;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.FieldValueOptionsRenderer;
import net.mystrobe.client.dynamic.config.IFieldValue;
import net.mystrobe.client.dynamic.page.ModalWindowUpdateMode;
import net.quarix.qrx4.samples.item.ItemFirstCategory;
import net.quarix.qrx4.samples.item.ItemSecondCategory;
import net.quarix.qrx4j.samples.data.beans.Item;
import net.quarix.qrx4j.samples.data.beans.meta.ItemSchema;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

public abstract class EditItemPanel extends Panel {

	protected IDataObject<Item> itemDO;
	
	protected ModalWindowUpdateMode updateMopde; 
	
	public EditItemPanel(String id, IDataObject<Item> itemDO, ModalWindowUpdateMode updateMode) {
		super(id);
		this.itemDO = itemDO;
		this.updateMopde = updateMode;
		
		add(new EditItemForm("editForm", new CompoundPropertyModel<Item>(itemDO.getData())));
	}
	
	public void updateFormModel(ModalWindowUpdateMode updateMode) {
		this.updateMopde = updateMode;
		
		EditItemForm form = getEditForm();
		form.visitFormComponents(new IVisitor<FormComponent<?>, Void>() {
            public void component(FormComponent<?> formComponent, IVisit<Void> visit) {
                formComponent.clearInput();
            }
        });
		form.setDefaultModelObject(itemDO.getData());
	}
	
	protected EditItemForm getEditForm() {
		return (EditItemForm) get("editForm");
	}

	protected class EditItemForm extends Form<Item>{

		public EditItemForm(String id, IModel<Item> model) {
			super(id, model);
			
			initFormComponnets();
		}
		
		protected void initFormComponnets() {
			
			FeedbackPanel feedback = new FeedbackPanel("feedback");
			feedback.setOutputMarkupId(true);
			add(feedback);
			
			//name
			RequiredTextField<String> nameTxt = new RequiredTextField<String>(ItemSchema.Cols.ITEMNAME.id());
			add(nameTxt);
			
			//category 1 drop down
			Collection<IFieldValue<String>> cat1OptionsList = ItemFirstCategory.toFieldValueList();
			Map<String, IFieldValue<String>> cat1OptionsMap = new HashMap<String, IFieldValue<String>>(cat1OptionsList.size());
			for (IFieldValue<String> fieldValue : cat1OptionsList) {
				cat1OptionsMap.put(fieldValue.getValue(), fieldValue);
			}	
			DropDownChoice<String> dropDownCat1 = new DropDownChoice<String>(ItemSchema.Cols.CATEGORY1.id(), 
					new ArrayList<String>(cat1OptionsMap.keySet()), new FieldValueOptionsRenderer<String>(cat1OptionsMap));
			dropDownCat1.setRequired(true);
			add(dropDownCat1);
			
			//category description
			TextArea<String> catDescTxt = new TextArea<String>(ItemSchema.Cols.CATDESCRIPTION.id());
			add( catDescTxt);
			
			//category 2 drop down
			Collection<IFieldValue<String>> cat2OptionsList = ItemSecondCategory.toFieldValueList();
			Map<String, IFieldValue<String>> cat2OptionsMap = new HashMap<String, IFieldValue<String>>(cat2OptionsList.size());
			for (IFieldValue<String> fieldValue : cat2OptionsList) {
				cat2OptionsMap.put(fieldValue.getValue(), fieldValue);
			}	
			
			DropDownChoice<String> dropDownCat2 = new DropDownChoice<String>(ItemSchema.Cols.CATEGORY2.id(), 
					new ArrayList<String>(cat2OptionsMap.keySet()), new FieldValueOptionsRenderer<String>(cat2OptionsMap));
			dropDownCat2.setRequired(true);
			add(dropDownCat2);
			
			//price
			RequiredTextField<BigDecimal> priceTxt = new RequiredTextField<BigDecimal>(ItemSchema.Cols.PRICE.id());
			add(priceTxt);
			
			//weight
			RequiredTextField<BigDecimal> weightTxt = new RequiredTextField<BigDecimal>(ItemSchema.Cols.WEIGHT.id());
			add(weightTxt);
			
			AjaxButton saveButton = new AjaxButton("saveBtn") {
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					
					try {
						itemDO.updateData();
						afterEdit(target, true);
					} catch (WicketDSBLException e) {
						
						error(e.getBLErorMessage().getMessage());
						onError(target, form);
					}
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.add(EditItemForm.this.get("feedback"));
				}
			};
			add(saveButton);
			
			AjaxLink<Void> cancelButton = new AjaxLink<Void>("cancelBtn") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					afterEdit(target, false);
				}
			};
			add(cancelButton);
			
			AjaxLink<Void> resetBtn = new AjaxLink<Void>("resetBtn") {

				@Override
				public void onClick(AjaxRequestTarget target) {
					itemDO.resetData();
					EditItemForm.this.visitFormComponents(new IVisitor<FormComponent<?>, Void>() {

						public void component(FormComponent<?> object,
								IVisit<Void> visit) {
							object.clearInput();
						}
					});
					target.add(EditItemForm.this);
				}
				
				@Override
				public boolean isVisible() {
					return !ModalWindowUpdateMode.Add.equals(updateMopde);
				}
			};
			add(resetBtn);
			
		}
	}
	
	public ModalWindowUpdateMode getUpdateMode() {
		return updateMopde;
	}
	
	protected abstract void afterEdit(AjaxRequestTarget target, boolean dataSaved);
}
