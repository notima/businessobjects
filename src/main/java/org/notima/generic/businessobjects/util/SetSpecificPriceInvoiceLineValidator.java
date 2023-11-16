package org.notima.generic.businessobjects.util;

import org.notima.generic.ifacebusinessobjects.OrderInvoice;
import org.notima.generic.ifacebusinessobjects.OrderInvoiceLine;
import org.notima.generic.ifacebusinessobjects.OrderInvoiceLineValidator;

public class SetSpecificPriceInvoiceLineValidator implements OrderInvoiceLineValidator {

	private double thePricePerUnit;
	private OrderInvoiceLine il;
	
	public SetSpecificPriceInvoiceLineValidator(double thePricePerUnit) {
		this.thePricePerUnit = thePricePerUnit;
	}
	
	@Override
	public void setOrderInvoice(OrderInvoice oi) {
	}

	@Override
	public void setLineToValidate(OrderInvoiceLine line) {
		this.il = line;
	}

	@Override
	public boolean isLineValid() {
		il.setPriceActual(thePricePerUnit);
		return true;
	}

	@Override
	public String getValidationMessage() {
		return "";
	}

	
	
}
