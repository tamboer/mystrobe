package net.quarix.qrx4.samples.ui.page.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mystrobe.client.FilterOperator;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.impl.FilterParameter;
import net.quarix.qrx4j.samples.AppConnector;
import net.quarix.qrx4j.samples.data.beans.SalesRep;
import net.quarix.qrx4j.samples.data.beans.meta.SalesRepSchema;
import net.quarix.qrx4j.samples.data.dao.SalesRepDataObject;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;

public class SalesRepresentativeAutocompleteTextField extends AutoCompleteTextField<String>{

	protected IDataObject<SalesRep> salesRepDO;
	
	public SalesRepresentativeAutocompleteTextField(String id) {
		super(id);
	}

	@Override
	protected Iterator<String> getChoices(String input) {
		
		if (salesRepDO == null) {
			salesRepDO = new SalesRepDataObject();
			salesRepDO.setAppConnector(AppConnector.getInstance());
			salesRepDO.getSchema().setBatchSize(0);
		}
		
		salesRepDO.clearFilters();
		salesRepDO.addFilter(new FilterParameter(SalesRepSchema.Cols.REPNAME, FilterOperator.MATCHES, input + "*"));
		salesRepDO.resetDataBuffer();
		
		List<String> choices = new ArrayList<String>(salesRepDO.getDataBuffer().size());
		for (SalesRep salesRep : salesRepDO.getDataBuffer()) {
			choices.add(salesRep.getRepname() + " " + salesRep.getSalesrep());  
		}
		
		return choices.iterator();
	}

	@Override
	protected void convertInput() {
		super.convertInput();
		String input = getInput();
		setConvertedInput(input.substring(input.lastIndexOf(" ") + 1));
	}
	
}
