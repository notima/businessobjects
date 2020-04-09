package org.notima.generic.businessobjects.util;

import java.util.Map;
import java.util.TreeMap;

import org.notima.generic.businessobjects.BasicBusinessObjectConverter;
import org.notima.generic.businessobjects.Order;
import org.notima.generic.businessobjects.OrderLine;
import org.notima.generic.businessobjects.OrderList;
import org.notima.generic.businessobjects.TaxSummary;
import org.notima.generic.ifacebusinessobjects.OrderInvoiceLine;

/**
 * Calculates revenue based on orders / invoices
 * 
 * @author Daniel Tamm
 */
public class RevenueCalculator {

	private OrderList	orderList;
	private String		currency;
	private Map<String,TaxSummary>	revenueMap;
	
	public RevenueCalculator(OrderList ol) {
		orderList = ol;
	}
	
	public Map<String, TaxSummary> getRevenueMap() {
		return revenueMap;
	}

	public void updateReveueMap() throws Exception {
		
		revenueMap = new TreeMap<String, TaxSummary>();
		
		if (orderList==null || orderList.getOrderList()==null) return;
		
		OrderLine oline;
		for (Order<?> o : orderList.getOrderList()) {
			
			if (o.getOrderInvoiceLines()==null)
				continue;
			
			if (currency==null) {
				currency = o.getCurrency();
			} else {
				if (o.getCurrency()!=null && !currency.equals(o.getCurrency())) {
					throw new Exception("Multicurrency revenue maps are not yet supported.");
				}
			}
			
			for (OrderInvoiceLine oiline : o.getOrderInvoiceLines()) {
				oline = (OrderLine)oiline;
				addOrderLine(oline);
			}
			
		}
		
	}

	/**
	 * Return the tax key to use for this orderline
	 * 
	 * @param oline
	 * @return	The tax key
	 */
	private String getTaxKey(OrderLine oline) {
		
		if (oline.getTaxKey()!=null) {
			return oline.getTaxKey();
		}

		return Double.toString(oline.getTaxPercent()); 
		
	}
	
	private void addOrderLine(OrderLine oline) {
		
		if (oline==null) return;
		String key = getTaxKey(oline);
		TaxSummary s = revenueMap.get(key);
		if (s==null) {
			s = new TaxSummary();
			s.setKey(key);
			revenueMap.put(key, s);
		}
		oline.calculateLineTotalIncTax(BasicBusinessObjectConverter.DEFAULT_ROUNDING_DECIMALS);
		double taxAmount = oline.getTaxAmount();
		double taxBase = oline.getLineNet();
		s.add(taxBase, taxAmount);
		
	}
	
	
}
