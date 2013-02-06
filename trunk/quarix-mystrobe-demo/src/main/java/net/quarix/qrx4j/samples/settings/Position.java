package net.quarix.qrx4j.samples.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.mystrobe.client.dynamic.config.FieldValue;
import net.mystrobe.client.dynamic.config.IFieldValue;

public enum Position {

	ADMIN_ASSISTANT("Admin Assistant"),
	ADMIN_REP("Admin Rep"),
	CONSULTANT("Consultant"),
	MARKETING_MANAGER("Marketing Manager"),
	PROGRAMMER("Programmer"),
	TRAINER("Trainer"),
	SALES_MANAGER("Sales Manager"),
	SENIOR_TRAINER("Senior Trainer"),
	SENIOR_PROGRAMMER("Senior Programmer"),
	SENIOR_ADMIN_REP("Senior Admin Rep"),
	SYSTEMS_PROGRAMMER("Systems Programmer");
	
	private static List<IFieldValue<String>> valuesList = null;
	
	static {
		valuesList = new ArrayList<IFieldValue<String>>();
		
		valuesList.add(new FieldValue<String>(ADMIN_ASSISTANT.getText(), ADMIN_ASSISTANT.getText(), ADMIN_ASSISTANT.getText()));
		valuesList.add(new FieldValue<String>(ADMIN_REP.getText(), ADMIN_REP.getText(), ADMIN_REP.getText()));
		valuesList.add(new FieldValue<String>(CONSULTANT.getText(), CONSULTANT.getText(), CONSULTANT.getText()));
		valuesList.add(new FieldValue<String>(MARKETING_MANAGER.getText(), MARKETING_MANAGER.getText(), MARKETING_MANAGER.getText()));
		valuesList.add(new FieldValue<String>(PROGRAMMER.getText(), PROGRAMMER.getText(), PROGRAMMER.getText()));
		valuesList.add(new FieldValue<String>(TRAINER.getText(), TRAINER.getText(), TRAINER.getText()));
		valuesList.add(new FieldValue<String>(SALES_MANAGER.getText(), SALES_MANAGER.getText(), SALES_MANAGER.getText()));
		valuesList.add(new FieldValue<String>(SENIOR_TRAINER.getText(), SENIOR_TRAINER.getText(), SENIOR_TRAINER.getText()));
		valuesList.add(new FieldValue<String>(SENIOR_PROGRAMMER.getText(), SENIOR_PROGRAMMER.getText(), SENIOR_PROGRAMMER.getText()));
		valuesList.add(new FieldValue<String>(SENIOR_ADMIN_REP.getText(), SENIOR_ADMIN_REP.getText(), SENIOR_ADMIN_REP.getText()));
		valuesList.add(new FieldValue<String>(SYSTEMS_PROGRAMMER.getText(), SYSTEMS_PROGRAMMER.getText(), SYSTEMS_PROGRAMMER.getText()));
	}
	
	private String text;
	
	private Position(String text) {
		this.text= text;
	}
	
	public String getText() {
		return text;
	}
	
	public static List<IFieldValue<String>> toFieldValueList() {
		return (valuesList);
	}
	
}
