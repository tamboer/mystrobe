package net.quarix.qrx4.samples.item;

import java.util.ArrayList;
import java.util.List;

import net.mystrobe.client.dynamic.config.FieldValue;
import net.mystrobe.client.dynamic.config.IFieldValue;

public enum ItemFirstCategory {

	AEROBICS("Aerobics"),
	ARCHERY("Archery"),
	BASEBALL("Baseball"),
	BIKING("Biking"),
	BOATING("Boating"),
	DIVING("Diving"),
	FISHING("Fishing"),
	FOOTBALL("Football"),
	GOLF("Golf"),
	HOCKEY("Hockey"),
	MISCELLANEOUS("Miscellaneous"),
	POLO("Polo"),
	RUNNING("Running"),
	SKATING("Skating"),
	SKIING("Skiing"),
	SOCCER("Soccer"),
	TENNIS("Tenis"),
	VOLLEYBALL("Volleyball");
	
	private static List<IFieldValue<String>> valuesList = null;
	
	static {
		valuesList = new ArrayList<IFieldValue<String>>();
		
		valuesList.add(new FieldValue<String>(AEROBICS.getText(), AEROBICS.getText(), AEROBICS.getText()));
		valuesList.add(new FieldValue<String>(ARCHERY.getText(), ARCHERY.getText(), ARCHERY.getText()));
		valuesList.add(new FieldValue<String>(BASEBALL.getText(), BASEBALL.getText(), BASEBALL.getText()));
		valuesList.add(new FieldValue<String>(BIKING.getText(), BIKING.getText(), BIKING.getText()));
		valuesList.add(new FieldValue<String>(BOATING.getText(), BOATING.getText(), BOATING.getText()));
		valuesList.add(new FieldValue<String>(DIVING.getText(), DIVING.getText(), DIVING.getText()));
		valuesList.add(new FieldValue<String>(FISHING.getText(), FISHING.getText(), FISHING.getText()));
		valuesList.add(new FieldValue<String>(FOOTBALL.getText(), FOOTBALL.getText(), FOOTBALL.getText()));
		valuesList.add(new FieldValue<String>(GOLF.getText(), GOLF.getText(), GOLF.getText()));
		valuesList.add(new FieldValue<String>(HOCKEY.getText(), HOCKEY.getText(), HOCKEY.getText()));
		valuesList.add(new FieldValue<String>(MISCELLANEOUS.getText(), MISCELLANEOUS.getText(), MISCELLANEOUS.getText()));
		valuesList.add(new FieldValue<String>(POLO.getText(), POLO.getText(), POLO.getText()));
		valuesList.add(new FieldValue<String>(RUNNING.getText(), RUNNING.getText(), RUNNING.getText()));
		valuesList.add(new FieldValue<String>(SKATING.getText(), SKATING.getText(), SKATING.getText()));
		valuesList.add(new FieldValue<String>(SKIING.getText(), SKIING.getText(), SKIING.getText()));
		valuesList.add(new FieldValue<String>(SOCCER.getText(), SOCCER.getText(), SOCCER.getText()));
		valuesList.add(new FieldValue<String>(TENNIS.getText(), TENNIS.getText(), TENNIS.getText()));
		valuesList.add(new FieldValue<String>(VOLLEYBALL.getText(), VOLLEYBALL.getText(), VOLLEYBALL.getText()));
	}
	
	private String text;
	
	private ItemFirstCategory(String text) {
		this.text= text;
	}
	
	public String getText() {
		return text;
	}
	
	public static List<IFieldValue<String>> toFieldValueList() {
		return (valuesList);
	}
	
}
