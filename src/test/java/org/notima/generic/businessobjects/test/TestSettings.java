package org.notima.generic.businessobjects.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import jakarta.xml.bind.JAXB;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Order;
import org.notima.generic.businessobjects.OrderList;

public class TestSettings {

	public static final String[] ORDER_EXAMPLE_XMLS = 
			new String[] {
				"OrderExample.xml",
				"OrderExample2.xml",
				"OrderListExample.xml",
				"ValidOrderWithoutName.xml"
				};
	
	public static final String[] BP_EXAMPLE_XMLS = 
			new String[] {
					"BpExample.xml",
					"BpExample2.xml"
			};
	

	public Order<?> getOrderExample(int idx) throws FileNotFoundException {
		
		Order<?> order = JAXB.unmarshal(getOrderExampleFile(idx), Order.class);
		return order;
		
	}
	
	public OrderList getOrderListExample(int idx) throws FileNotFoundException {
		
		OrderList orderList = JAXB.unmarshal(getOrderExampleFile(idx), OrderList.class);
		return orderList;
		
	}
	
	public BusinessPartner<?> getBusinessPartnerExample(int idx) throws FileNotFoundException {
		
		BusinessPartner<?> bp = JAXB.unmarshal(getBpExampleFile(idx), BusinessPartner.class);
		return bp;
		
	}
	
	private File getBpExampleFile(int idx) throws FileNotFoundException {
		
		return getExampleFile(BP_EXAMPLE_XMLS[idx]);
		
	}

	private File getOrderExampleFile(int idx) throws FileNotFoundException {
		
		return getExampleFile(ORDER_EXAMPLE_XMLS[idx]);
		
	}
	
	private File getExampleFile(String filename) throws FileNotFoundException {

		URL url = TestSettings.class.getClassLoader().getResource(filename);
		if (url==null) {
			throw new FileNotFoundException("Can't find in classpath: " + filename);
		}

		return new File(url.getFile());
		
	}
	
	
}
