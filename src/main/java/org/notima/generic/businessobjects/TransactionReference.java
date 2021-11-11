package org.notima.generic.businessobjects;

import java.time.LocalDate;

public class TransactionReference extends AttributeObject {

	private String 	invoiceNo;
	private String	orderNo;
	private LocalDate	orderDate;
	private LocalDate	invoiceDate;
	private LocalDate	transactionDate;
	private LocalDate	shipDate;
	private String	transactionId;
	private String	customerNo;
	private String	project;
	private String	transactionGroup;
	private String	transactionType;
	private String	customReference;
	private String	customReferenceName;
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
	public LocalDate getShipDate() {
		return shipDate;
	}
	public void setShipDate(LocalDate shipDate) {
		this.shipDate = shipDate;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getTransactionGroup() {
		return transactionGroup;
	}
	public void setTransactionGroup(String transactionGroup) {
		this.transactionGroup = transactionGroup;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCustomReference() {
		return customReference;
	}
	public void setCustomReference(String customReference) {
		this.customReference = customReference;
	}
	public String getCustomReferenceName() {
		return customReferenceName;
	}
	public void setCustomReferenceName(String customReferenceName) {
		this.customReferenceName = customReferenceName;
	}
	
	
}
