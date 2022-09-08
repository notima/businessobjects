package org.notima.generic.businessobjects;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

@Entity
@XmlRootElement(name = "order")
public class Order<O> implements OrderInvoice {

	@Id
	@GeneratedValue
	private Integer id;
	
	private String documentKey;
	private String orderKey;
	private String paymentTermKey;
	private String paymentRule;
	private PaymentTerm 	paymentTerm;
	@OneToMany
	private List<OrderLine> lines = new Vector<OrderLine>();
	private double netTotal;
	private double grandTotal;
	private double vatTotal;
	@ManyToOne
	private BusinessPartner<?> bpartner;
	@ManyToOne
	private BusinessPartner<?> billBpartner;
	@ManyToOne
	private BusinessPartner<?> sender;
	@ManyToOne
	private Location billLocation;
	@ManyToOne
	private Location shipLocation;
	@ManyToOne
	private Person	 billPerson;
	private Date	 dateOrdered;
	private Date	 datePromised;
	private Date	 dateDelivered;
	private String	warehouseId;
	private String 	currency;
	private String	ourCustomerNo;
	private String	externalReference1;
	private String	externalReference2;
	private String	yourOrderNumber;
	private boolean	salesOrder;
	private int	   roundingDecimals = 2;
	private String	deliveryRule;
	private String	deliveryViaRule;
	private String	shipper;
	@OneToMany
	private List<KeyValue> attributes = new ArrayList<KeyValue>();
	@ManyToOne
	private FactoringReservation factoringReservation;	
	private OrderStatus	status;
	private String statusComment;
	
	private transient O nativeOrder;
	
	/**
	 * Short summary of order
	 * 
	 * @return
	 */
	public String printOrderInfo() {
		StringBuffer buf = new StringBuffer();
		if (getDocumentKey()!=null) {
			buf.append(getDocumentKey());
		} else if (getOrderKey()!=null) {
			buf.append(getOrderKey());
		}
		if (getBusinessPartner()!=null && getBusinessPartner().getName()!=null) {
			if (buf.length()>0) buf.append(" ");
			buf.append(getBusinessPartner().getName());
		}
		if (buf.length()>0) buf.append(" ");
		buf.append(" with total " + getGrandTotal());
		return buf.toString();
	}
	
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("[orderKey=>"+orderKey + "]," + "[BpIdentityNo=>" + (bpartner!=null ? bpartner.getIdentityNo() : "") + "]");
		buf.append("[grandTotal=>"+grandTotal + (currency!=null?(" " + currency):"") + "]");
		for (OrderLine line : lines) {
			if (buf.length()>0)
				buf.append("\r\n");
			buf.append("  " + line.toString());
		}
		return buf.toString();
	}
	
	public void addOrderLine(OrderLine line) {
		lines.add(line);
	}
	
	public void addAttribute(String attributeName, Object value) {
		attributes.add(new KeyValue(attributeName, value));
	}
	
	public String getPaymentTermKey() {
		return paymentTermKey;
	}
	
	public List<OrderLine> getLines() {
		return lines;
	}
	
	public double getGrandTotal() {
		return grandTotal;
	}
	
	public BusinessPartner<?> getBusinessPartner() {
		return bpartner;
	}

	public BusinessPartner<?> getBpartner() {
		return bpartner;
	}

	public void setBpartner(BusinessPartner<?> bpartner) {
		this.bpartner = bpartner;
	}

	public Location getBillLocation() {
		return billLocation;
	}

	public void setBillLocation(Location billAddress) {
		this.billLocation = billAddress;
	}

	public Location getShipLocation() {
		return shipLocation;
	}

	public void setShipLocation(Location shipAddress) {
		this.shipLocation = shipAddress;
	}

	public void setPaymentTermKey(String paymentTermKey) {
		this.paymentTermKey = paymentTermKey;
	}

	public void setLines(List<OrderLine> lines) {
		this.lines = lines;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Person getBillPerson() {
		return billPerson;
	}

	public void setBillPerson(Person billPerson) {
		this.billPerson = billPerson;
	}

	/**
	 * Returns bill bPartner if set,
	 * otherwise business partner.
	 */
	public BusinessPartner<?> getBillBpartner() {
		if (billBpartner==null)
			return bpartner;
		return billBpartner;
	}

	public void setBillBpartner(BusinessPartner<?> billBpartner) {
		this.billBpartner = billBpartner;
	}

	public Date getDateOrdered() {
		return dateOrdered;
	}

	public void setDateOrdered(Date dateOrdered) {
		this.dateOrdered = dateOrdered;
	}
	
	public Date getDateDelivered() {
		return dateDelivered;
	}

	public void setDateDelivered(Date dateDelivered) {
		this.dateDelivered = dateDelivered;
	}

	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String getDocumentKey() {
		return documentKey;
	}

	@Override
	public Date getDocumentDate() {
		return(getDateOrdered());
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
		documentKey = key;
	}

	@Override
	public void setDocumentDate(Date date) {
		setDateOrdered(date);
	}

	@Override
	public Date getDueDate() {
		return null;
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
		return false;
	}

	@Override
	public String getOurCustomerNo() {
		return this.ourCustomerNo;
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
	public boolean isInvoice() {
		return false;
	}

	@Override
	public boolean isOrder() {
		return true;
	}

	public double getVatTotal() {
		return vatTotal;
	}

	public void setVatTotal(double vatTotal) {
		this.vatTotal = vatTotal;
	}

	public void setOurCustomerNo(String ourCustomerNo) {
		this.ourCustomerNo = ourCustomerNo;
	}

	public boolean isSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(boolean isSalesOrder) {
		this.salesOrder = isSalesOrder;
	}

	public Date getDatePromised() {
		return datePromised;
	}

	public void setDatePromised(Date datePromised) {
		this.datePromised = datePromised;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public BusinessPartner<?> getSender() {
		return sender;
	}

	public void setSender(BusinessPartner<?> sender) {
		this.sender = sender;
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

	public List<KeyValue> getAttributesWithKey(String key) {
		List<KeyValue> result = new ArrayList<KeyValue>();
		if (key==null) return result;
		for (KeyValue attr : attributes) {
			if (key.equals(attr.getKey())) {
				result.add(attr);
			}
		}
		return result;
	}
	
	public Object getFirstAttributeWithKey(String key) {
		if (key==null) return null;
		for (KeyValue attr : attributes) {
			if (key.equals(attr.getKey())) {
				return attr.getObject();
			}
		}
		return null;
	}
	
	public void setAttributes(List<KeyValue> attributes) {
		this.attributes = attributes;
	}
	
	
	/**
	 * This method replaces the oldTaxKey and recalculates the tax lines 
	 * and the total tax of this order.
	 * 
	 * @param oldTaxKey			The old tax key to replace
	 * @param newTaxKey			The new tax key
	 * @param newTaxPercent		The new tax percentage.
	 * @param precision			Rounding precision when recalculating tax.
	 */
	public void replaceTaxKey(String oldTaxKey, String newTaxKey, double newTaxPercent, int precision) {
		
		if (getLines()==null) return;

		double lineTotal;
		
		for (OrderLine ol : lines) {
			
			if ((ol.getTaxKey()==null && oldTaxKey==null) || 
					(ol.getTaxKey()!=null && ol.getTaxKey().equals(oldTaxKey)) ) {
				
				lineTotal = ol.calculateLineTotalIncTax(precision);
				ol.setTaxKey(newTaxKey);
				ol.setTaxPercent(newTaxPercent);
				ol.calculatePriceActualFromLineTotalIncTax(lineTotal, precision);
				
			}
			
		}
		
		calculateGrandTotal();
		
	}
	
	/**
	 * Calculates tax percent by using grand total and vat amount. Distributes tax on tax lines
	 */
	public void calculateTaxPercentFromTotal() {
		
		double taxPercent = vatTotal / (grandTotal-vatTotal);
		
		taxPercent = (double)Math.round(taxPercent * 10000.0);
		taxPercent = taxPercent / 100.0;

		for (OrderLine o : lines) {
			o.setTaxPercent(taxPercent);
		}
		
	}

	public String getDeliveryRule() {
		return deliveryRule;
	}

	public void setDeliveryRule(String deliveryRule) {
		this.deliveryRule = deliveryRule;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getDeliveryViaRule() {
		return deliveryViaRule;
	}

	public void setDeliveryViaRule(String deliveryViaRule) {
		this.deliveryViaRule = deliveryViaRule;
	}

	public String getPaymentRule() {
		return paymentRule;
	}

	public void setPaymentRule(String paymentRule) {
		this.paymentRule = paymentRule;
	}

	public PaymentTerm getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(PaymentTerm paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getStatus() {
		if (status==null) return null;
		return status.toString();
	}

	public void setStatus(String statusString) {
		this.status = OrderStatus.valueOf(statusString);
	}

	@Transient
	public void setStatusEnum(OrderStatus status) {
		this.status = status;
	}
	

	public O getNativeOrder() {
		return nativeOrder;
	}

	public void setNativeOrder(O nativeOrder) {
		this.nativeOrder = nativeOrder;
	}

	/**
	 * Calculates grand total by adding the line total of all lines.
	 * Sets the grandTotal, vatTotal and netTotal attributes.
	 * @return
	 */
	public double calculateGrandTotal() {
		// Sum up all lines
		double total = 0.0;
		vatTotal = 0.0;
		netTotal = 0.0;
		
		if (lines!=null) {
			for (OrderLine line : lines) {
				total += line.calculateLineTotalIncTax(roundingDecimals);
				vatTotal += line.getTaxAmount();
				netTotal += line.getLineNet();
			}
		}
		
		total = InvoiceLine.round(total, roundingDecimals);
		vatTotal = InvoiceLine.round(vatTotal, roundingDecimals);
		netTotal = InvoiceLine.round(netTotal, roundingDecimals);
		grandTotal = total;
		return total;
	}
	
	/**
	 * Suggests a tax distribution of the given amount. Lowest tax rate first.
	 * TODO - This must be more tested when there are multiple tax rates.
	 * 
	 * @param amountIncTax
	 * @return	A list of taxes suggested. The total amounts to amountIncTax.
	 */
	public List<TaxSummary> calculateSuggestedTaxDistribution(double amountIncTax) {

		Map<String, TaxSummary> taxes = calculateTaxSummary(); 		
		
		List<TaxSummary> result = new ArrayList<TaxSummary>();
		TaxSummary ts = null;
		TaxSummary src = null;
		if (taxes==null || taxes.size()==0) {
			ts = new TaxSummary();
			ts.setTaxBase(amountIncTax);
			ts.setTaxAmount(0);
			ts.setKey(Tax.TAX_KEY_UNKNOWN);
			ts.setRate(0);
			result.add(ts);
			return result;
		}
		
		if (taxes.size()==1) {
			src = taxes.get(taxes.keySet().iterator().next());
			ts = new TaxSummary();
			ts.setKey(src.getKey());
			ts.calculateValuesFrom(amountIncTax, src.getRate());
			result.add(ts);
			return result;
		}

		// Sort tax summary according to VAT rate, lowest first.
		List<TaxSummary> taxesArray = new ArrayList<TaxSummary>();
		taxesArray.addAll(taxes.values());
		
		Comparator<TaxSummary> sorter = new Comparator<TaxSummary>() {
			@Override
			public int compare(TaxSummary o1, TaxSummary o2) {
				if (o1.getRate()==o2.getRate()) return 0;
				return (o1.getRate()>o2.getRate()) ? -1 : 1;
			}
		};

		TaxSummary[] sorted = new TaxSummary[taxesArray.size()];
		Arrays.sort(taxesArray.toArray(sorted), sorter);
		
		double totalAdded = 0;
		
		int i=0;
		while (totalAdded < amountIncTax && i<sorted.length) {
			src = sorted[i++];
			if (totalAdded + src.calculateTotal() <= amountIncTax) {
				result.add(src.clone());
				totalAdded += src.calculateTotal();
			} else {
				ts = new TaxSummary();
				ts.calculateValuesFrom(src.calculateTotal() - totalAdded, src.getRate());
				result.add(ts);
				totalAdded += ts.calculateTotal();
				break;
			}
		}

		// If we still haven't filled up the amount
		if (totalAdded < amountIncTax) {
			ts = new TaxSummary();
			ts.setTaxBase(amountIncTax - totalAdded);
			ts.setTaxAmount(0);
			ts.setKey(Tax.TAX_KEY_UNKNOWN);
			ts.setRate(0);
			result.add(ts);
		}
		
		return result;
		
	}
	
	
	/**
	 * Calculates and returns tax summary for order
	 * 
	 * @return A tax summary for this order
	 */
	public Map<String,TaxSummary> calculateTaxSummary() {

		Map<String,TaxSummary> result = new TreeMap<String,TaxSummary>();
		
		String taxKey = null;
		TaxSummary ts;
		
		for (OrderLine il : (List<OrderLine>)getLines()) {
			if (il.calculateLineTotalIncTax(roundingDecimals)==0) {
				// Don't consider lines amounting to zero.
				continue;
			}
			taxKey = il.getTaxKey();
			if (taxKey==null) {
				taxKey = new Double(il.getTaxPercent()).toString();
			}

			ts = result.get(taxKey);
			if (ts==null) {
				ts = new TaxSummary();
				ts.setKey(taxKey);
				ts.setRate(il.getTaxPercent());
				result.put(taxKey, ts);
			}
			
			// Add numbers
			ts.setTaxBase(ts.getTaxBase() + il.getLineNet());
			ts.setTaxAmount(ts.getTaxAmount() + il.getTaxAmount());
			
		}
		
		// Remove any zero summaries if there are more than one tax key
		Map<String, TaxSummary> withoutZeroes = new TreeMap<String,TaxSummary>();
		
		if (result.size()<2) return result;
		
		for (TaxSummary tts : result.values()) {
			if (tts.getTaxAmount()==0 && tts.getTaxBase()==0) {
				continue;
			}
			withoutZeroes.put(tts.getKey(), tts);
		}
		if (withoutZeroes.size()==0 && result.size()>0) {
			// All are zeroes, return all
			return result;
		}
		
		return withoutZeroes;
		
	}
	
	public String getYourOrderNumber() {
		return yourOrderNumber;
	}

	public void setYourOrderNumber(String yourOrderNumber) {
		this.yourOrderNumber = yourOrderNumber;
	}

	@XmlTransient
	public boolean isValidOrder() {
		return hasValidBusinessPartner() && hasOrderLines();
	}
	
	public boolean hasValidBusinessPartner() {
		return bpartner!=null && (bpartner.hasName() || bpartner.hasTaxId());
	}
	
	public boolean hasOrderLines() {
		return lines!=null && lines.size()>0;
	}
	
	public boolean hasStatus(String status) {
		if (this.status==null && status==null)
			return true;
		
		if (this.status==null) return false;
		
		return this.status.toString().equals(status);
	}


	public String getStatusComment() {
		return statusComment;
	}


	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}

	/**
	 * Appends to status comment if there's already an existing comment. A semicolon is prepended if there's an existing comment.
	 * Sets status comment if no one exists.
	 * @param comment
	 */
	public void appendStatusComment(String comment) {
		if (statusComment==null) {
			statusComment = comment;
			return;
		}
		if (statusComment.trim().length()>0) {
			statusComment += "; " + comment; 
		} else {
			statusComment = comment;
		}
		
	}
	
}
