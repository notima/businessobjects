package org.notima.generic.businessobjects;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payment")
public class Payment<P> {

	private String	businessPartnerKey;
	private BusinessPartner<?>	businessPartner;
	private String	currency;
	private String	orderNo;
	private String	clientOrderNo;
	private String	invoiceNo;
	private boolean	customerPayment;
	private Double	amount;
	private String	accountNo;
	private String	comment;
	private java.util.Date paymentDate;
	private Double	acctAmount;
	private String	acctCurrency;
	private Location	location;
	private transient P nativePayment;
	
	private PaymentWriteOffs paymentWriteOffs;
	
	public String getBusinessPartnerKey() {
		return businessPartnerKey;
	}
	public void setBusinessPartnerKey(String businessPartnerKey) {
		this.businessPartnerKey = businessPartnerKey;
	}
	public BusinessPartner<?> getBusinessPartner() {
		return businessPartner;
	}
	public void setBusinessPartner(BusinessPartner<?> businessPartner) {
		this.businessPartner = businessPartner;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getClientOrderNo() {
		return clientOrderNo;
	}
	public void setClientOrderNo(String clientOrderNo) {
		this.clientOrderNo = clientOrderNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public boolean isCustomerPayment() {
		return customerPayment;
	}
	public void setCustomerPayment(boolean customerPayment) {
		this.customerPayment = customerPayment;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public java.util.Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(java.util.Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Double getAcctAmount() {
		return acctAmount;
	}
	public void setAcctAmount(Double acctAmount) {
		this.acctAmount = acctAmount;
	}
	public String getAcctCurrency() {
		return acctCurrency;
	}
	public void setAcctCurrency(String acctCurrency) {
		this.acctCurrency = acctCurrency;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public PaymentWriteOffs getPaymentWriteOffs() {
		return paymentWriteOffs;
	}
	public void setPaymentWriteOffs(PaymentWriteOffs paymentWriteOffs) {
		this.paymentWriteOffs = paymentWriteOffs;
	}
	public P getNativePayment() {
		return nativePayment;
	}
	public void setNativePayment(P nativePayment) {
		this.nativePayment = nativePayment;
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		if (amount!=null) {
			result.append("Amt: " + amount);
		}
		if (currency!=null) {
			result.append(" " + currency);
		}
		if (paymentDate!=null) {
			if (result.length()>0) 
				result.append(", ");

			result.append("Date: " + paymentDate);
		}
		if (orderNo!=null) {
			if (result.length()>0) 
				result.append(", ");

			result.append("OrderNo: " + orderNo);
		}
		if (invoiceNo!=null) {
			if (result.length()>0) 
				result.append(", ");

			result.append("InvoiceNo: " + invoiceNo);
		}
		if (accountNo!=null) {
			if (result.length()>0) 
				result.append(", ");

			result.append("AcctNo: " + accountNo);
		}
		if (businessPartnerKey!=null) {
			if (result.length()>0) 
				result.append(", ");

			result.append("bpKey: " + businessPartnerKey);
		}
		if (businessPartner!=null) {
			if (result.length()>0) 
				result.append(", ");

			result.append("bp: " + businessPartner.toString());
		}
		result.insert(0, "{");
		result.append("}");

		return result.toString();
	}
	
}
