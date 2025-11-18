package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Holder class for reporting an invoice operation.
 * 
 * @author Daniel Tamm
 *
 */
public class OrderInvoiceOperationResult {

	private boolean successful;

	private int		updateCount = 0;
	private int		createCount = 0;
	
	private InvoiceList		affectedInvoices;
	private OrderList		affectedOrders;
	
	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	public int getCreateCount() {
		return createCount;
	}

	public void setCreateCount(int createCount) {
		this.createCount = createCount;
	}
	
	public void incrementUpdated() {
		updateCount++;
	}
	
	public void incrementCreated() {
		createCount++;
	}

	public InvoiceList getAffectedInvoices() {
		return affectedInvoices;
	}

	public void setAffectedInvoices(InvoiceList affectedInvoices) {
		this.affectedInvoices = affectedInvoices;
	}

	private void initAffectedInvoices() {
		affectedInvoices = new InvoiceList();
		List<Invoice<?>> invoiceList = new ArrayList<Invoice<?>>();
		affectedInvoices.setInvoiceList(invoiceList);
	}
	
	public void addAffectedInvoice(Invoice<?> inv) {
		if (affectedInvoices==null) {
			initAffectedInvoices();
		}
		affectedInvoices.getInvoiceList().add(inv);
	}
	
	public OrderList getAffectedOrders() {
		return affectedOrders;
	}

	public void setAffectedOrders(OrderList affectedOrders) {
		this.affectedOrders = affectedOrders;
	}
	
	/**
	 * Removes any references to native formats.
	 */
	public void canonize() {
		
		if (affectedInvoices!=null) {
			List<Invoice<?>> invoiceList = affectedInvoices.getInvoiceList();
			if (invoiceList!=null) {
				for (Invoice<?> ii : invoiceList) {
					ii.setNativeInvoice(null);
				}
			}
		}
		
		if (affectedOrders!=null) {
			List<Order<?>> orderList = affectedOrders.getOrderList();
			if (orderList!=null) {
				for (Order<?> oo : orderList) {
					oo.setNativeOrder(null);
				}
			}
		}
		
	}
	
}
