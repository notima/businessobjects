package org.notima.generic.businessobjects.test;

import static org.junit.Assert.*;

import java.io.File;

import jakarta.xml.bind.JAXB;

import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.test.samples.BPartnerTestFactory;

public class TestBusinessPartner {

	private BusinessPartner<?> bp0, bp1;
	
	@Before
	public void setUp() throws Exception {
		
		bp0 = BPartnerTestFactory.buildTestBusinessPartnerNameOnly();
		
		TestSettings testSettings = new TestSettings();
		bp1 = testSettings.getBusinessPartnerExample(0);
		
	}

	@Test
	public void testHasName() {
		
		assertTrue(bp0.hasName());
		assertTrue(bp1.hasName());
		
	}
	
	@Test
	public void testHasContacts() {
		
		assertFalse(bp0.hasContacts());
		
	}

	@Test
	public void testIsAnonymous() {
		
		assertFalse(bp0.isAnonymous());
		
	}
	
	@Test
	public void testSaveToXml() {
		
		try {
			
			File saveFile = new File("BpSavedFile.xml");
			JAXB.marshal(bp0, saveFile);
			saveFile.delete();
			
		} catch (Exception ee) {
			fail(ee.getMessage());
		}
		
	}
	

}
