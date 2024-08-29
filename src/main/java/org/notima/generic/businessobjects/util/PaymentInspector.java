package org.notima.generic.businessobjects.util;

import org.notima.generic.businessobjects.Payment;

public class PaymentInspector {

	private Payment<?> payment;
	
	public PaymentInspector(Payment<?> pmt) {
		payment = pmt;
	}
	
	public String getDetailedShortDescription() {
		
		StringBuffer buf = new StringBuffer();

		if (payment!=null) {
			
			if (payment.getBusinessPartnerKey()!=null && payment.getBusinessPartnerKey().trim().length()>0) {
				buf.append(payment.getBusinessPartnerKey() + " ");
			}
			buf.append(payment.getPayerName() + " ");

			if (payment.hasOrderNo()) {
				buf.append("Order# " + payment.getOrderNo() + " ");
			}
			
			if (payment.hasClientOrderNo() && !payment.getClientOrderNo().equalsIgnoreCase(payment.getOrderNo())) {
				buf.append("ClientOrder# " + payment.getClientOrderNo() + " ");
			}
			
		}
		
		return buf.toString().trim();
		
	}
	
}
