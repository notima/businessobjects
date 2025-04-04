package org.notima.generic.businessobjects.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.PaymentBatchProcessOptions;

class TestPaymentBatchProcessOptions {

	public PaymentBatchProcessOptions options; 
	
	@Test
	void testManualReferences() {
		
		options = new PaymentBatchProcessOptions();
		
		options.addManualReferenceMapFromCommaList("1234=6789,1235=6790");
		
		String destination = options.getManualReferenceFor("1235");
		
		assertEquals("6790", destination);
		
	}

}
