package org.notima.generic.businessobjects.util;

import org.notima.generic.ifacebusinessobjects.OrderInvoice;
import org.notima.generic.ifacebusinessobjects.OrderInvoiceLine;
import org.notima.generic.ifacebusinessobjects.OrderInvoiceLineValidator;

public class SetSpecificPriceInvoiceLineValidator implements OrderInvoiceLineValidator {

	private double thePricePerUnit;
	private boolean adjustTax;
	private double taxPercent;
	private boolean priceIncludesTax;
	private OrderInvoiceLine il;
	private OrderInvoice oi;

	/**
	 * Updates prices
	 * 
	 * @param thePricePerUnit		The price per unit
	 * @param priceIncludesTax		If the price per unit is including tax or excluding tax.
	 * @param adjustTax				If the tax should be considered / adjusted.
	 * @param taxPercent			The tax percent that should be used.
	 */
	public SetSpecificPriceInvoiceLineValidator(double thePricePerUnit, boolean priceIncludesTax, boolean adjustTax, double taxPercent) {
		this.thePricePerUnit = thePricePerUnit;
		this.adjustTax = adjustTax;
		this.taxPercent = taxPercent;
		this.priceIncludesTax = priceIncludesTax;
	}
	
	public SetSpecificPriceInvoiceLineValidator(double thePricePerUnit) {
		this.thePricePerUnit = thePricePerUnit;
		this.adjustTax = false;
	}
	
	
	@Override
	public void setOrderInvoice(OrderInvoice oi) {
		this.oi = oi;
	}

	@Override
	public void setLineToValidate(OrderInvoiceLine line) {
		this.il = line;
	}

	@Override
	public boolean isLineValid() {
		il.setPriceActual(thePricePerUnit);
		if (adjustTax) {
			il.setTaxPercent(taxPercent);
			il.setPricesIncludeVAT(priceIncludesTax);
		}
		// TODO: Adjust rounding.
		il.calculateLineTotalIncTax(2);
		return true;
	}

	@Override
	public String getValidationMessage() {
		return "";
	}

	
	
}
