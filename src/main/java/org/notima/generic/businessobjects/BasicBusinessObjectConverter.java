package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.notima.generic.businessobjects.TaxSummary;

import org.notima.generic.ifacebusinessobjects.BusinessObjectConverter;

public class BasicBusinessObjectConverter<O,I> implements BusinessObjectConverter<O,I> {
	
	public static int DEFAULT_ROUNDING_DECIMALS = 2;
	
	/**
	 * Creates a deep copy of the order
	 * 
	 * @param src
	 * @return
	 */
	public Order<O> copyOrder(Order<O> src) {

		Order<O> dst = new Order<O>();
		dst.setBillBpartner(src.getBillBpartner());
		dst.setBillLocation(src.getBillLocation());
		dst.setBillPerson(src.getBillPerson());
		dst.setBpartner(src.getBusinessPartner());
		dst.setCurrency(src.getCurrency());
		dst.setDocumentDate(src.getDocumentDate());
		dst.setGrandTotal(src.getGrandTotal());
		dst.setOrderKey(src.getOrderKey()!=null ? src.getOrderKey() : src.getDocumentKey());
		dst.setShipLocation(src.getShipLocation());
		dst.setVatTotal(src.getVatTotal());

		// Copy lines
		List<OrderLine> olines = new ArrayList<OrderLine>();
		dst.setLines(olines);
		for (OrderLine iline : src.getLines()) {
			olines.add(copyOrderLine(iline));
		}
		dst.calculateGrandTotal();
		
		// Copy attributes
		dst.setAttributes(src.getAttributes());
		
		return dst;
		
	}
	
	@Override
	public Invoice<I> toInvoice(Order<O> src) {

		Invoice<I> dst = new Invoice<I>();
		dst.setBillLocation(src.getBillLocation());
		dst.setBillPerson(src.getBillPerson());
		dst.setBusinessPartner(src.getBusinessPartner());
		dst.setCurrency(src.getCurrency());
		dst.setDocumentDate(src.getDocumentDate());
		dst.setGrandTotal(src.getGrandTotal());
		dst.setOrderKey(src.getDocumentKey());
		dst.setShipLocation(src.getShipLocation());
		dst.setVatTotal(src.getVatTotal());
		dst.setDeliveryDate(src.getDateDelivered());
		dst.setExternalReference1(src.getExternalReference1());
		dst.setExternalReference2(src.getExternalReference2());
		dst.setPaymentRule(src.getPaymentRule());
		dst.setPaymentTermKey(src.getPaymentTermKey());
		
		// Copy lines
		List<InvoiceLine> ilines = new ArrayList<InvoiceLine>();
		dst.setLines(ilines);
		for (OrderLine oline : src.getLines()) {
			ilines.add(toInvoiceLine(oline));
		}
		dst.calculateGrandTotal();
		
		// Copy attributes
		dst.setAttributes(src.getAttributes());
		
		return dst;
		
	}
	
	public InvoiceLine toInvoiceLine(OrderLine src) {
		
		InvoiceLine dst = new InvoiceLine();

		dst.setKey(src.getKey());
		dst.setGroupKey(src.getGroupKey());
		dst.setName(src.getName());
		dst.setDescription(src.getDescription());
		dst.setProductKey(src.getProductKey());
		dst.setProduct(src.getProduct());
		dst.setLineNo(src.getLineNo());
		dst.setPriceActual(src.getPriceActual());
		dst.setQtyEntered(src.getQtyEntered());
		dst.setQtyDelivered(src.getQtyDelivered());
		dst.setTaxPercent(src.getTaxPercent());
		dst.setTaxKey(src.getTaxKey());
		dst.setTaxIncludedInPrice(src.isPricesIncludeVAT());
		dst.setUOM(src.getUOM());

		dst.calculateLineTotalIncTax(2);
		
		return dst;
	}

	@Override
	public O toNativeOrder(Order<?> src) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Invoice<I> fromNativeInvoice(I src) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Order<O> toOrder(Invoice<I> src) {

		Order<O> dst = new Order<O>();
		dst.setBillBpartner(src.getBillBpartner());
		dst.setBillLocation(src.getBillLocation());
		dst.setBillPerson(src.getBillPerson());
		dst.setBpartner(src.getBusinessPartner());
		dst.setCurrency(src.getCurrency());
		dst.setDocumentDate(src.getDocumentDate());
		dst.setGrandTotal(src.getGrandTotal());
		dst.setOrderKey(src.getOrderKey()!=null ? src.getOrderKey() : src.getDocumentKey());
		dst.setShipLocation(src.getShipLocation());
		dst.setVatTotal(src.getVatTotal());

		// Copy lines
		List<OrderLine> olines = new ArrayList<OrderLine>();
		dst.setLines(olines);
		for (InvoiceLine iline : src.getLines()) {
			olines.add(toOrderLine(iline));
		}
		dst.calculateGrandTotal();
		
		// Copy attributes
		dst.setAttributes(src.getAttributes());
		
		return dst;
		
	}
	
	public OrderLine toOrderLine(InvoiceLine src) {
		
		OrderLine dst = new OrderLine();

		dst.setKey(src.getKey());
		dst.setGroupKey(src.getGroupKey());
		dst.setName(src.getName());
		dst.setDescription(src.getDescription());
		dst.setProductKey(src.getProductKey());
		dst.setProduct(src.getProduct());
		dst.setLineNo(src.getLineNo());
		dst.setPriceActual(src.getPriceActual());
		dst.setQtyEntered(src.getQtyEntered());
		dst.setQtyDelivered(src.getQtyDelivered());
		dst.setTaxPercent(src.getTaxPercent());
		dst.setTaxKey(src.getTaxKey());
		dst.setUOM(src.getUOM());
		dst.setLineNet(src.getLineNet());
		dst.setTaxAmount(src.getTaxAmount());
		dst.setLineNet(src.getLineNet());
		dst.setPricesIncludeVAT(src.isPricesIncludeVAT());

		dst.calculateLineTotalIncTax(2);
		
		return dst;
	}

	/**
	 * Deep copy of order line
	 * 
	 * @param src
	 * @return
	 */
	public OrderLine copyOrderLine(OrderLine src) {
		
		OrderLine dst = new OrderLine();

		dst.setKey(src.getKey());
		dst.setGroupKey(src.getGroupKey());
		dst.setName(src.getName());
		dst.setDescription(src.getDescription());
		dst.setProductKey(src.getProductKey());
		dst.setProduct(src.getProduct());
		dst.setLineNo(src.getLineNo());
		dst.setPriceActual(src.getPriceActual());
		dst.setQtyEntered(src.getQtyEntered());
		dst.setQtyDelivered(src.getQtyDelivered());
		dst.setTaxPercent(src.getTaxPercent());
		dst.setTaxKey(src.getTaxKey());
		dst.setUOM(src.getUOM());
		dst.setLineNet(src.getLineNet());
		dst.setTaxAmount(src.getTaxAmount());
		dst.setLineNet(src.getLineNet());
		dst.setPricesIncludeVAT(src.isPricesIncludeVAT());

		dst.calculateLineTotalIncTax(2);
		
		return dst;
	}

	
	/**
	 * Negates the order.
	 * 
	 * @param src
	 * @return
	 */
	public Order<O> negateOrder(Order<O> src) {
		
		// Negate qty on order lines and recalculate grand total
		
		if (src==null) return null;
		if (src.getOrderInvoiceLines()==null) return src;
		
		for (OrderLine ol : (List<OrderLine>)src.getLines()) {
			if (ol.getQtyEntered()!=0)
				ol.setQtyEntered(-ol.getQtyEntered());
			if (ol.getQtyDelivered()!=0)
				ol.setQtyDelivered(-ol.getQtyDelivered());
			ol.calculateLineTotalIncTax(DEFAULT_ROUNDING_DECIMALS);
		}
		
		src.calculateGrandTotal();
		
		return src;
		
	}
	
	/**
	 * Creates an order on given amount from a given source order.
	 * 
	 * If a line is found that matches the amount of the order, that line is copied.
	 * If no line is found, a non specified line is created.
	 * 
	 * @param	src		The source order. This is not changed.
	 * @param   amount	The total amount of the order.
	 */
	public Order<O> createOrderFromAmount(Order<O> src, double amount) throws Exception {
		
		Order<O> dst = copyOrder(src);
		
		OrderLine singleLine = null;
		
		// Check for one single line match
		for (OrderLine ol : (List<OrderLine>)dst.getLines()) {
			
			if ((ol.getLineNet() + ol.getTaxAmount()) == amount) {
				singleLine = ol;
				break;
			}
			
		}
		
		List<OrderLine> newList = new ArrayList<OrderLine>();
		
		if (singleLine!=null) {
			
			newList.add(singleLine);
			
		} else {
			// We need to create arbitrary credit line(s)
			
			double amountRemaining = amount;
			
			// Check how taxes are distributed.
			Map<String, TaxSummary> taxes = (Map<String, TaxSummary>)dst.calculateTaxSummary();

			// Get tax rates
			SortedSet<Double> rates = new TreeSet<Double>();
			// Save in lists based on rates
			Map<Double, List<TaxSummary>> taxMap = new TreeMap<Double, List<TaxSummary>>();;
			List<TaxSummary> tsl;
			
			TaxSummary ts;
			for (String key : taxes.keySet()) {
				ts = taxes.get(key);
				// Add to set
				rates.add(ts.getRate());
				// Add to taxMap
				tsl = taxMap.get(ts.getRate());
				if (tsl==null) {
					tsl = new ArrayList<TaxSummary>();
					taxMap.put(ts.getRate(), tsl);
				}
				tsl.add(ts);
			}
			
			// We wan't to credit the highest tax first
			// Create a list and sort it in reverse
			List<Double> rateList = new ArrayList<Double>();
			for (Iterator<Double> ii = rates.iterator(); ii.hasNext(); ) {
				rateList.add(ii.next());
			}
			Collections.reverse(rateList);
			
			// Create lines crediting on tax rate
			for (Double rate : rateList) {

				if (amountRemaining <= 0)
					break;
				
				tsl = taxMap.get(rate);
				double amountToCredit = 0.0;
				for (TaxSummary t : tsl) {
					
					if (amountRemaining <= (t.getTaxBase() + t.getTaxAmount())) {
						amountToCredit = amountRemaining;
					} else {
						amountToCredit = t.getTaxBase() + t.getTaxAmount();
					}
					
					singleLine = new OrderLine();
					singleLine.setQtyEntered(1);
					singleLine.setPricesIncludeVAT(true);
					singleLine.setPriceActual(amountToCredit);
					singleLine.setTaxPercent(t.getRate());
					singleLine.calculateLineTotalIncTax(DEFAULT_ROUNDING_DECIMALS);
					// TODO: Internationalization
					singleLine.setDescription("Unspecified line");
					newList.add(singleLine);

					amountRemaining -= amountToCredit;
					
					if (amountRemaining <= 0)
						break;
				}
				
			}
			
			// If we've gone through the rates and there's still amount to credit, we credit more than what
			// was originally on the order.
			if (amountRemaining >0) {
				if (rateList.size()>1) {
					throw new Exception("Can't credit more than order value because tax rates are ambiguos");
				}
				// Add a line with remaining amount
				singleLine = new OrderLine();
				singleLine.setQtyEntered(1);
				singleLine.setPricesIncludeVAT(true);
				singleLine.setPriceActual(amountRemaining);
				// If there are no tax rates, set tax rate to 0 otherwise the only tax rate.
				singleLine.setTaxPercent(rateList.isEmpty() ? 0.0 : rateList.get(0));
				singleLine.calculateLineTotalIncTax(DEFAULT_ROUNDING_DECIMALS);
				// TODO: Internationalization
				singleLine.setDescription("Unspecified line");
				newList.add(singleLine);

			}
			
		}

		dst.setLines(newList);
		dst.calculateGrandTotal();
		
		return dst;
		
	}
	
	/**
	 * Creates a credit order on given amount (what is credited is not specified).
	 * 
	 * If a line is found that matches the amount to be credited, that line is credited.
	 * If no line is found, a non specified credit line is created.
	 * 
	 * @param	src		The source order. This is not changed.
	 * @param   amount	Amount to be credited. The amount should be a positive number.
	 */
	public Order<O> createCreditOrderFromAmount(Order<O> src, double amount) throws Exception {

		Order<O> dst = createOrderFromAmount(src, amount);
		dst = negateOrder(dst);
		
		return dst;
		
	}

	@Override
	public I toNativeInvoice(Invoice<I> src) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	

}
