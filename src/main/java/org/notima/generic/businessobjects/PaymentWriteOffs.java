package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class PaymentWriteOffs {

	private List<PaymentWriteOff> paymentWriteOff;

	public void add(PaymentWriteOff pwo) {
		if (paymentWriteOff == null) {
			paymentWriteOff = new ArrayList<PaymentWriteOff>();
		}
		paymentWriteOff.add(pwo);
	}
	
	@XmlElement(name="paymentWriteOff")
	public List<PaymentWriteOff> getPaymentWriteOff() {
		return paymentWriteOff;
	}

	public void setPaymentWriteOff(List<PaymentWriteOff> paymentWriteOff) {
		this.paymentWriteOff = paymentWriteOff;
	}
	
	
	
}
