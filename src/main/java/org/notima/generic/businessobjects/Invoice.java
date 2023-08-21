package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.notima.generic.ifacebusinessobjects.FactoringReservation;
import org.notima.generic.ifacebusinessobjects.OrderInvoice;
import org.notima.generic.ifacebusinessobjects.OrderInvoiceLine;
import org.notima.generic.ifacebusinessobjects.OrderInvoiceLineValidator;

@Entity
@XmlRootElement(name = "invoice")
public class Invoice<I> implements OrderInvoice {

	@Id
	@GeneratedValue
	private Integer	id;
	
	private double	lineNet;
	private int		lineNo;


	private String invoiceKey;
	private String paymentTermKey;
	private String paymentRule;
	private String descriptionKey;
	
	@OneToMany
	private List<InvoiceLine> lines = new Vector<InvoiceLine>();
	private double netTotal;
	private double grandTotal;
	private double vatTotal;
	private double openAmt;
	@ManyToOne
	private BusinessPartner<?> bpartner;
	private String orderKey;
	@ManyToOne
	private Location billLocation;
	private java.util.Date invoiceDate;
	private java.util.Date dueDate;
	private java.util.Date deliveryDate;
	private java.util.Date contractStartDate;
	private java.util.Date contractEndDate;
	@ManyToOne
	private Person billPerson;
	@ManyToOne
	private Location shipLocation;
	@ManyToOne
	private BusinessPartner<?> billBpartner;
	@ManyToOne
	private BusinessPartner<?>	sender;
	private String currency;
	private String ourCustomerNo;
	private String poDocumentNo;
	private String	externalReference1;
	private String	externalReference2;
	private int	   roundingDecimals = 2;
	private int	   nextLineNo = 10;
	private int	   lineNoIncrement = 10;
	@ManyToOne
	private FactoringReservation factoringReservation;
	private String	warehouseId;
	private String	pricelistId;
	@ManyToOne
	private Person  ourReference;
	@OneToMany
	private List<KeyValue> attributes = new ArrayList<KeyValue>();
	private String status;
	
	private String comment;
	
	private Map<String,BankAccountDetail>	bankAccountDetails = new TreeMap<String, BankAccountDetail>();
	
	private String	ocr;
	private boolean showPricesIncludingVAT = false;
	
	private transient I nativeInvoice;
	
	private transient OrderInvoiceLineValidator	lineValidator;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double round(double roundMe) {
		double multiplicator = Math.pow(10, (double)roundingDecimals);
		double result = Math.round(roundMe*multiplicator) / multiplicator;
		return result;
	}
	
	public boolean isShowPricesIncludingVAT() {
		return showPricesIncludingVAT;
	}

	public void setShowPricesIncludingVAT(boolean showPricesIncludingVAT) {
		this.showPricesIncludingVAT = showPricesIncludingVAT;
	}

	public double getLineNet() {
		return round(lineNet);
	}

	public void setLineNet(double lineNet) {
		this.lineNet = lineNet;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	
	public String getDescriptionKey() {
		return descriptionKey;
	}
	
	public void setDescriptionKey(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}
	
	public String getPaymentTermKey() {
		return paymentTermKey;
	}
	
	public List<InvoiceLine> getLines() {
		return lines;
	}
	
	public double getGrandTotal() {
		return round(grandTotal);
	}
	
	public double getOpenAmt() {
		return round(openAmt);
	}
	
	public BusinessPartner<?> getBusinessPartner() {
		return bpartner;
	}

	public void setBusinessPartner(BusinessPartner<?> bpartner) {
		this.bpartner = bpartner;
	}
	
	public void addInvoiceLine(InvoiceLine il) {
		if (il.getLineNo()==0) {
			il.setLineNo(nextLineNo);
			nextLineNo+=lineNoIncrement;
		}
		lines.add(il);
	}
	
	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

	public void setPaymentTermKey(String paymentTermKey) {
		this.paymentTermKey = paymentTermKey;
	}
	
	public String getPaymentRule() {
		return paymentRule;
	}

	public void setPaymentRule(String paymentRule) {
		this.paymentRule = paymentRule;
	}

	public void setLines(List<InvoiceLine> lines) {
		this.lines = lines;
		calculateGrandTotal();
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public void setOpenAmt(double openAmt) {
		this.openAmt = openAmt;
	}

	public String getInvoiceKey() {
		return invoiceKey;
	}

	public void setInvoiceKey(String invoiceKey) {
		this.invoiceKey = invoiceKey;
	}

	public Location getBillLocation() {
		return billLocation;
	}

	public void setBillLocation(Location billLocation) {
		this.billLocation = billLocation;
	}

	@Override
	public String getDocumentKey() {
		return(invoiceKey);
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}
	
	@Override
	public Person getBillPerson() {
		return billPerson;
	}

	@Override
	public Location getShipLocation() {
		return shipLocation;
	}

	@Override
	public BusinessPartner<?> getBillBpartner() {
		return billBpartner;
	}

	@Override
	public Date getDocumentDate() {
		return getInvoiceDate();
	}

	@Override
	public String getCurrency() {
		return currency;
	}

	public void setInvoiceDate(java.util.Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public void setBillPerson(Person billPerson) {
		this.billPerson = billPerson;
	}

	public void setShipLocation(Location shipLocation) {
		this.shipLocation = shipLocation;
	}

	public void setBillBpartner(BusinessPartner<?> billBpartner) {
		this.billBpartner = billBpartner;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@XmlTransient
	public FactoringReservation getFactoringReservation() {
		return factoringReservation;
	}

	public void setFactoringReservation(FactoringReservation factoringReservation) {
		this.factoringReservation = factoringReservation;
	}

	@Override
	public void setDocumentKey(String key) {
		setInvoiceKey(key);
		
	}

	@Override
	public void setDocumentDate(Date date) {
		setInvoiceDate(date);
	}

	@Override
	public Date getDueDate() {
		return this.dueDate;
	}

	public java.util.Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(java.util.Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@Override
	public double getFreightAmount() {
		return 0;
	}

	@Override
	public String getShipmentNo() {
		return null;
	}

	@Override
	public boolean isCreditNote() {
		return this.grandTotal<0.0;
	}

	@Override
	public String getOurCustomerNo() {
		return this.ourCustomerNo;
	}

	@Override
	public boolean isInvoice() {
		return true;
	}

	@Override
	public boolean isOrder() {
		return false;
	}

	public void setDueDate(java.util.Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setOurCustomerNo(String ourCustomerNo) {
		this.ourCustomerNo = ourCustomerNo;
	}

	public String getPoDocumentNo() {
		return poDocumentNo;
	}

	public void setPoDocumentNo(String poDocumentNo) {
		this.poDocumentNo = poDocumentNo;
	}

	public double getVatTotal() {
		return round(vatTotal);
	}

	public void setVatTotal(double vatTotal) {
		this.vatTotal = vatTotal;
	}
	
	/**
	 * Calculates grand total by adding the line total of all lines.
	 * Sets the grandTotal attribute.
	 * @return
	 */
	public double calculateGrandTotal() {
		// Sum up all lines
		double total = 0.0;
		vatTotal = 0.0;
		netTotal = 0.0;
		
		for (InvoiceLine line : lines) {
			total += line.calculateLineTotalIncTax(roundingDecimals);
			vatTotal += line.getTaxAmount();
			netTotal += line.getLineNet();
		}
		
		total = InvoiceLine.round(total, roundingDecimals);
		vatTotal = InvoiceLine.round(vatTotal, roundingDecimals);
		netTotal = InvoiceLine.round(netTotal, roundingDecimals);
		grandTotal = total;
		return total;
	}

	public int getRoundingDecimals() {
		return roundingDecimals;
	}

	public void setRoundingDecimals(int roundingDecimals) {
		this.roundingDecimals = roundingDecimals;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getPricelistId() {
		return pricelistId;
	}

	public void setPricelistId(String pricelistId) {
		this.pricelistId = pricelistId;
	}

	/**
	 * Total of all lines excluding VAT
	 * @return
	 */
	public double getNetTotal() {
		return round(netTotal);
	}

	public void setNetTotal(double netTotal) {
		this.netTotal = netTotal;
	}

	public BusinessPartner<?> getSender() {
		return sender;
	}

	public void setSender(BusinessPartner<?> sender) {
		this.sender = sender;
	}

	public Person getOurReference() {
		return ourReference;
	}

	public void setOurReference(Person ourReference) {
		this.ourReference = ourReference;
	}
	
	public String getExternalReference1() {
		return externalReference1;
	}

	public void setExternalReference1(String externalReference1) {
		this.externalReference1 = externalReference1;
	}

	public String getExternalReference2() {
		return externalReference2;
	}

	public void setExternalReference2(String externalReference2) {
		this.externalReference2 = externalReference2;
	}

	@Override
	public List<OrderInvoiceLine> getOrderInvoiceLines() {
		if (lines!=null) {
			List<OrderInvoiceLine> oiLines = new ArrayList<OrderInvoiceLine>();
			oiLines.addAll(lines);
			return (oiLines);
		} else {
			return null;
		}
	}

	public List<KeyValue> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<KeyValue> attributes) {
		this.attributes = attributes;
	}

	public Map<String, BankAccountDetail> getBankAccountDetails() {
		return bankAccountDetails;
	}

	public void setBankAccountDetails(
			Map<String, BankAccountDetail> bankAccountDetails) {
		this.bankAccountDetails = bankAccountDetails;
	}
	
	public BankAccountDetail getBankAccountWithType(String type) {
		return bankAccountDetails.get(type);
	}

	public String getOcr() {
		return ocr;
	}

	public void setOcr(String ocr) {
		this.ocr = ocr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public java.util.Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(java.util.Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public java.util.Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(java.util.Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public I getNativeInvoice() {
		return nativeInvoice;
	}

	public void setNativeInvoice(I src) {
		this.nativeInvoice = src;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return comment;
	}

	@Override
	public void setOrderInvoiceLineValidator(OrderInvoiceLineValidator validator) {
		lineValidator = validator;		
	}

	@Override
	@XmlTransient
	public List<OrderInvoiceLine> getInvalidLines() {
		List<OrderInvoiceLine> invalidLines = new ArrayList<OrderInvoiceLine>();
		if (lineValidator==null || lines==null)
			return invalidLines;

		for (InvoiceLine il : lines) {
			lineValidator.setLineToValidate(il);
			if (!lineValidator.isLineValid()) {
				invalidLines.add(il);
			}
		}
		
		return invalidLines;
	}
	
	@Override
	@XmlTransient
	public OrderInvoiceLineValidator getOrderInvoiceLineValidator() {
		return lineValidator;
	}
	
}
