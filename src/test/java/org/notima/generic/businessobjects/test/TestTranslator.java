package org.notima.generic.businessobjects.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.notima.generic.businessobjects.util.Translator;
import org.notima.generic.ifacebusinessobjects.LanguageTranslator;

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
		
		LanguageTranslator tr = new Translator();
		
		String translated = tr.getTranslation(TEST_WORD, "sve");
		assertEquals("Testar översätta kontoplan", translated, "kontoplan");
		System.out.println(translated);
		
		System.out.println(Translator.capitalizeFirstLetter(translated));
		
	}

}
