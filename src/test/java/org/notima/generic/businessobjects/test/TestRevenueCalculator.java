package org.notima.generic.businessobjects.test;

import java.io.File;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.OrderList;
import org.notima.generic.businessobjects.TaxSummary;
import org.notima.generic.businessobjects.util.RevenueCalculator;

public class TestRevenueCalculator {

	private File orderExampleFile;
	
	@Before
	public void setUp() throws Exception {
		orderExampleFile = TestSettings.getOrderExampleFile(2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		OrderList orderList = JAXB.unmarshal(orderExampleFile, OrderList.class);

		RevenueCalculator rc = new RevenueCalculator(orderList);
		try {
			rc.updateReveueMap();
			Map<String, TaxSummary> revenueMap = rc.getRevenueMap();
	
			for (TaxSummary ts : revenueMap.values()) { 
				if (!ts.isEmpty())
					System.out.println(ts.getKey() + " : " + ts.getTaxBase() + " : " + ts.getTaxAmount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
