package org.notima.factoring;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.notima.generic.businessobjects.*;
import org.notima.generic.ifacebusinessobjects.BusinessObjectFactory;
import org.notima.generic.ifacebusinessobjects.OrderInvoiceLine;

/**
 * Class that creates detail rows from ADempiere invoices and orders.
 * 
 * These detail rows are used when sending information to factoring providers.
 * 
 * @author Daniel Tamm
 *
 */
public class DetailRowFactory<C,I,O,P,B> {

	private BusinessObjectFactory<C,I,O,P,B> m_oFactory;
	
	// TODO: Make the rounding charge configurable
	public final static int ROUNDING_CHARGE_ID=1000001; 

	public DetailRowFactory(BusinessObjectFactory<C,I,O,P,B> oFactory) {
		
		m_oFactory = oFactory;
		
	}
	
	/**
	 * Creates an array of DetailRow using a vector of OrderInvoiceLine as source.
	 * 
	 * @param oilines
	 * @param useGrouping			If true, articles are grouped.
	 * @param useRounding			If true rounding rows from the source lines are not discarded
	 * @param extraReservationAmt	If non zero, a rounding row with extra reservation amount is added.
	 * @param checkAmt				Detail rows should sum up to the check amt. If rounding is true
	 * 							    a rounding row will make sure the lines sum to checkAmt.
	 * @param roundingDecimals		Precision to use when rounding. Different factoring providers
	 * 								have different precision.
	 * @return
	 * @throws Exception 
	 */
	protected DetailRow[] createFromOrderInvoiceLines(
							Vector<OrderInvoiceLine> oilines, 
							boolean useGrouping,
							boolean useRounding,
							double extraReservationAmt,
							double checkAmt,
							int roundingDecimals) throws Exception {

		/**
		 * ================================================================================= 
		 * This block sorts the invoice lines into groups. The order line determines if the
		 * invoice lines should be grouped.
		 * =================================================================================
		 */
		
		// Map to store group products
		Map<String, Product<P>> groupProducts = new TreeMap<String,Product<P>>();
		// Map to store the list of group products (ie the products in the group)
		Map<String, Vector<OrderInvoiceLine>> groupLines = new TreeMap<String,Vector<OrderInvoiceLine>>();
		// Convert invoice lines to canonical format

        String group = "";
		Product<P> groupProduct;
		Vector<OrderInvoiceLine> glines;
        for (OrderInvoiceLine line : oilines) {
        	if (useGrouping) {
        		group = line.getGroupKey();
        		if (group!=null && group.trim().length()>0) {
        			groupProduct = groupProducts.get(group);
        			if (groupProduct==null) {
        				// Lookup product
        				groupProduct = m_oFactory.lookupProduct(group);
        				if (groupProduct!=null) {
        					groupProducts.put(group, groupProduct);
        				}
        			}
        			// Try again to see if group product is null
        			if (groupProduct!=null) {
        				// This is a group product
        				 glines = groupLines.get(group);
        				 if (glines==null) {
        					 glines = new Vector<OrderInvoiceLine>();
        					 groupLines.put(group, glines);
        				 }
        				 glines.add(line);
        				 continue;
        			} else {
        				// This is not a group product because the group product could not be found.
        				glines = groupLines.get("");
        				if (glines==null) {
        					glines = new Vector<OrderInvoiceLine>();
        					groupLines.put("", glines);
        				}
        				glines.add(line);
        				continue;
        			}
        		}
        	}
			// This is not a group product (no link to order or no packey on order or no grouping)
			glines = groupLines.get("");
			if (glines==null) {
				glines = new Vector<OrderInvoiceLine>();
				groupLines.put("", glines);
			}
			glines.add(line);
        }

		/**
		 * ================================================================================= 
		 * END OF GROUPING BLOCK
		 * =================================================================================
		 */

        
    	Vector<DetailRow> goodsList = new Vector<DetailRow>();
    	BasicDetailRow dstRow;
        String artno = "";
        String name = "";
    	
    	// Create groups first
    	for(String g : groupLines.keySet()) {
    		// Don't create lines for the default group at this time
    		if (g.trim().length()==0) continue;
    		// Get the product for the group
    		groupProduct = groupProducts.get(g);
    		double qty = 9999999d; // Qty will be set to lowest qty in group
    		artno = groupProduct.getKey();
    		name = groupProduct.getName();
    		// Summarize the price
    		double price = 0;
    		String taxKey = null;
    		glines = groupLines.get(g);
    		for (OrderInvoiceLine il : glines) {
    			qty = Math.min(qty,il.getQtyEntered());
    			if (il.getPriceActual()!=0)
    				price = price + (il.getPriceActual()*il.getQtyEntered());
    			if (taxKey==null) {
    				// Set tax id
    				taxKey = il.getTaxKey();
    			} else {
    				// Make sure it's the same tax id. There can't be different tax id's in a group
    				if (!taxKey.equals(il.getTaxKey())) {
    					throw new Exception("A group/pac must have the same tax");
    				}
    			}
    		}
            Tax mtax = m_oFactory.lookupTax(taxKey);
            double vat = mtax.getRate();
            // Add group product and price
    		dstRow = new BasicDetailRow(
    				glines.get(0).getLineNo()-1, // Use the line number of the first detail in the group subtracted by one.
    				artno, //ArticleNr
    				name, // Article name / Description
    				null, // TODO: Show serial
    				price / qty,    //Divide with qty since multiplied by qty before
    				qty,	   //NrOfUnits
    				null,	   //Unit
    				vat,	   //VatPercent
    				null	   //DiscountPercent (never use this)
    				);   
    		dstRow.setRoundingDecimals(roundingDecimals);
            
    		goodsList.add(dstRow);
            
    		// Add group products, don't show price and indent.
    		for (OrderInvoiceLine il : glines) {

    			dstRow = (BasicDetailRow) convert(il, useRounding);
    			if (dstRow!=null) {
    				// Indent the description/name
    				dstRow.setName("- " + dstRow.getName());
    				// Set the price to zero
    				dstRow.setPricePerUnit(0.0);
    				dstRow.setRoundingDecimals(roundingDecimals);    				
    				goodsList.add(dstRow);
    			}
    			
    		}
 
    	}

		// Create lines for the default group
		glines = groupLines.get("");
		if (glines!=null) {
	        for (OrderInvoiceLine il : glines) {
	        	dstRow = (BasicDetailRow) convert(il, useRounding);
	        	if (dstRow!=null) {
	        		dstRow.setRoundingDecimals(roundingDecimals);	        		
	        		goodsList.add(dstRow);
	        	}
	        }
		}

        double extraRound = 0.0;
        // Calculate current total and round/compensate for any difference.
        if (useRounding) {
	        double rowsTotal = DetailRowUtility.calculateTotalAmountWithTax(goodsList.toArray(new DetailRow[]{}), roundingDecimals);
			if (checkAmt!=rowsTotal ) {
				extraRound = checkAmt - rowsTotal;
			}
        }
        
        // Check for extra reservation amount
		if (extraReservationAmt!=0 || extraRound!=0) {
    		// Add extra reservation amount
			Product<P> charge = m_oFactory.lookupRoundingProduct();
    		BasicDetailRow extraResAmtRow = new BasicDetailRow(
    				99999, //ClientOrderRowNr
    				"", //ArticleNr
    				charge.getName(), // Article name / Description
    				null, // Serial
    				extraReservationAmt + extraRound,    //PricePerUnit
    				1.0,	   //NrOfUnits
    				null,	   //Unit
    				0.0,	   //VatPercent
    				null	   //DiscountPercent (never use this)
    				);
    		extraResAmtRow.setRoundingDecimals(roundingDecimals);
    		goodsList.add(extraResAmtRow);
		}
        
		
        return goodsList.toArray(new DetailRow[]{});
		
	}
	
	/**
	 * Creates detail rows from an ADempiere invoice.
	 * Allocations are automatically deducted.
	 * 
	 * @param invoice
	 * @return
	 * @throws Exception 
	 */
	public DetailRow[] createFromInvoice(Invoice<?> invoice, int roundingDecimals) throws Exception {

        // Check if rounding should be used
        PaymentTerm pt = m_oFactory.lookupPaymentTerm(invoice.getPaymentTermKey());
        // Check if payment term is "factoring".
        boolean useGrouping = pt!=null && pt.isUseGrouping();
        // Check if rounding should be a detail row.
        boolean useRounding = pt!=null && pt.isUseRounding();
		
		InvoiceLine[] ilines = invoice.getLines().toArray(new InvoiceLine[0]);
		Vector<OrderInvoiceLine> oilines = new Vector<OrderInvoiceLine>();
		for (int i=0; i<ilines.length; i++) {
			oilines.add(ilines[i]);
		}
		
		// Check for deductions / allocations on invoice
		Vector<DetailRow> deductions = new Vector<DetailRow>();
        // add deductions
		double ded = invoice.getGrandTotal()-invoice.getOpenAmt();
        if (ded>0)  {
        	DetailRow row;
        	String lang = invoice.getBusinessPartner().getLanguage();
        	String name, artno;
        	if (lang == null) lang = "";
        	if (lang.equals("fi_FI")) {
        		name = "Osasuoritus";
        		artno = "maksu";
        	} else {  	
        		name = "Avgår betalt";
        		artno = "betalt";
        	}
        	row = new BasicDetailRow(artno, name, 1.0, ded);
        	row.setRoundingDecimals(roundingDecimals);
        	deductions.add(row);
        }

        
		DetailRow[] result = createFromOrderInvoiceLines(oilines, useGrouping, useRounding, 0.0, invoice.getOpenAmt(), roundingDecimals);
		// If there are deductions, add them to the result.
		if (deductions.size()>0) {
			DetailRow[] tmp = new DetailRow[result.length+deductions.size()];
			for (int i=0; i<result.length; i++) {
				tmp[i] = result[i];
			}
			int len = result.length;
			for (int i=0; i<deductions.size(); i++) {
				tmp[i+len] = deductions.get(i);
			}
			result = tmp;
		}
		
		return(result);
		
	}

	/**
	 * Creates detail rows from ADempiere order.
	 * The payment term on the order determines how the lines are generated.
	 * 
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	public DetailRow[] createFromOrder(Order<?> order, int roundingDecimals) throws Exception {
		
        // Check payment term
        PaymentTerm pt = m_oFactory.lookupPaymentTerm(order.getPaymentTermKey());
        // Check if rounding should be a detail row.
        boolean useRounding = pt.isUseRounding();
        // Check if grouping should be used
        boolean useGrouping = pt.isUseGrouping();
        // Check if extra reservation amount should be added.
        double extraReservationAmt = pt.getExtraReserveAmt();
		
        OrderLine[] lines = order.getLines().toArray(new OrderLine[0]);

		Vector<OrderInvoiceLine> oilines = new Vector<OrderInvoiceLine>();
		for (int i=0; i<lines.length; i++) {
			oilines.add(lines[i]);
		}

		// Find Adempiere's total amount of the order as a check amount.
		double checkAmt = order.getGrandTotal();
		
		return(createFromOrderInvoiceLines(oilines, useGrouping, useRounding, extraReservationAmt, checkAmt, roundingDecimals));
        
	}

	/**
	 * Converts an invoice line to a detail row
	 * 
	 * If the detail row is rounding and useRounding = false, null is returned.
	 * 
	 * @param il
	 * @return
	 */
	private DetailRow convert(OrderInvoiceLine il, boolean useRounding) throws Exception {
        double qty = il.getQtyEntered(); 
        Product<P> mp = m_oFactory.lookupProduct(il.getProductKey());
        String artno = "";
        String name = "";
        if (mp!=null) {
        	artno = mp.getKey();
        	name = mp.getName();
        } else {
       		name = il.getName();
       		if (name==null || name.trim().length()==0) 
       			name = il.getDescription();
       		artno = il.getProductKey();
        }
        if (name == null)
        	name = "";
        
        double price = il.getPriceActual();
        double vat = 0.0;        
        if (il.getTaxKey()!=null) {
	        Tax mtax = m_oFactory.lookupTax(il.getTaxKey());
	        vat = mtax.getRate();
        } else {
        	vat = il.getTaxPercent();
        }
        
		BasicDetailRow dstRow = new BasicDetailRow(
				il.getLineNo(), //ClientOrderRowNr
				artno, //ArticleNr
				name, // Article name / Description
				null, // TODO: Show serial
				price,    //PricePerUnit
				qty,	   //NrOfUnits
				null,	   //Unit
				vat,	   //VatPercent
				null	   //DiscountPercent (never use this)
				);         
		
		return(dstRow);
	}
	
}
