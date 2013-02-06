package net.quarix.qrx4.samples.item;

import java.util.ArrayList;
import java.util.List;

import net.mystrobe.client.dynamic.config.FieldValue;
import net.mystrobe.client.dynamic.config.IFieldValue;

public enum ItemSecondCategory {
	
	CLOTHING("Clothing"),
	EQUIPMENT("Equipment"),
	FOOTWEAR("Footwear");
	
	private static List<IFieldValue<String>> valuesList = null;
	
	static {
		valuesList = new ArrayList<IFieldValue<String>>();
		valuesList.add(new FieldValue<String>(CLOTHING.getText(), CLOTHING.getText(), CLOTHING.getText()));
		valuesList.add(new FieldValue<String>(EQUIPMENT.getText(), EQUIPMENT.getText(), EQUIPMENT.getText()));
		valuesList.add(new FieldValue<String>(FOOTWEAR.getText(), FOOTWEAR.getText(), FOOTWEAR.getText()));
	}
	
	private String text;
	
	private ItemSecondCategory(String text) {
		this.text= text;
	}
	
	public String getText() {
		return text;
	}
	
	public static List<IFieldValue<String>> toFieldValueList() {
		return (valuesList);
	}
}
