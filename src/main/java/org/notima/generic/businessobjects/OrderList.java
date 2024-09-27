package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderList")
public class OrderList {

	private List<Order<?>> orderList = new ArrayList<Order<?>>();

	@XmlElement(name = "order")
	public List<Order<?>> getOrderList() {
		return orderList;
	}

	
	public void setOrderList(List<Order<?>> orderList) {
		this.orderList = orderList;
	}
	
}
