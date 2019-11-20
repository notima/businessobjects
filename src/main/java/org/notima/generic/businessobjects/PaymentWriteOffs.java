package org.notima.generic.businessobjects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PaymentWriteOffs {

	private List<PaymentWriteOff> paymentWriteOff;

	@XmlElement(name="paymentWriteOff")
	public List<PaymentWriteOff> getPaymentWriteOff() {
		return paymentWriteOff;
	}

	public void setPaymentWriteOff(List<PaymentWriteOff> paymentWriteOff) {
		this.paymentWriteOff = paymentWriteOff;
	}
	
	
	
}
