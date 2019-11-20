package org.notima.factoring;

import org.notima.generic.businessobjects.*;
import java.math.BigDecimal;

public class BasicPaymentRequest implements PaymentRequest {

	private BusinessPartner<?>		bPartner;
	private Invoice<?>		invoice;
	private Order<?>			order;
	private BigDecimal		amount;
	private String			currencyIso;
	private java.util.Date	requestDate;
	private String			reference;
	
	public BusinessPartner<?> getbPartner() {
		return bPartner;
	}
	
	public void setbPartner(BusinessPartner<?> bPartner) {
		this.bPartner = bPartner;
	}
	
	public Invoice<?> getInvoice() {
		return invoice;
	}
	
	public void setInvoice(Invoice<?> invoice) {
		this.invoice = invoice;
	}
	
	public Order<?> getOrder() {
		return order;
	}
	
	public void setOrder(Order<?> order) {
		this.order = order;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getCurrencyIso() {
		return currencyIso;
	}
	
	public void setCurrencyIso(String currencyIso) {
		this.currencyIso = currencyIso;
	}
	
	public java.util.Date getRequestDate() {
		return requestDate;
	}
	
	public void setRequestDate(java.util.Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
}
