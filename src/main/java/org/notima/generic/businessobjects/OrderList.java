package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderList")
public class OrderList {

	private List<Order<?>> orderList = new ArrayList<Order<?>>();

	private BusinessPartner<?> creditor;
	
	@XmlElement(name = "order")
	public List<Order<?>> getOrderList() {
		return orderList;
	}

	
	public void setOrderList(List<Order<?>> orderList) {
		this.orderList = orderList;
	}


	public BusinessPartner<?> getCreditor() {
		return creditor;
	}


	public void setCreditor(BusinessPartner<?> creditor) {
		this.creditor = creditor;
	}
	
	
	
}
