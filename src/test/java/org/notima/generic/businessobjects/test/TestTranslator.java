package org.notima.generic.businessobjects.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.util.Translator;

public class TestTranslator {

	public String TEST_WORD = "chartOfAccounts";
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTranslator() {
		
		String translated = Translator.getTranslation(TEST_WORD, "sv");
		System.out.println(translated);
		
		System.out.println(Translator.capitalizeFirstLetter(translated));
		
	}

}
