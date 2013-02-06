package net.quarix.qrx4.samples.ui.page.settings;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class StatesPage extends SettingsBasePage {

	public StatesPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected SettingsTab getCurrentActiveTab() {
		return SettingsTab.States;
	}

}
