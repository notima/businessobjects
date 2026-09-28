package org.notima.generic.businessobjects.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.CurrencyAccountMap;
import org.notima.generic.businessobjects.PaymentBatchChannelOptions;
import org.notima.generic.businessobjects.UnknownCurrencyAccountException;

public class TestCurrencyAccountMap {

	private PaymentBatchChannelOptions optionsWith(String inTransit, String defaultCurrency) {
		PaymentBatchChannelOptions opts = new PaymentBatchChannelOptions();
		opts.setDefaultCurrency(defaultCurrency);
		opts.setGeneralLedgerInTransitAccount(inTransit);
		return opts;
	}

	@Test
	public void singleAccountIsUsedForAllCurrencies() {
		PaymentBatchChannelOptions opts = optionsWith("1991", "SEK");
		assertEquals("1991", opts.getGeneralLedgerInTransitAccount());
		assertEquals("1991", opts.getGeneralLedgerInTransitAccount("SEK"));
		assertEquals("1991", opts.getGeneralLedgerInTransitAccount("EUR"));
		assertEquals("1991", opts.getGeneralLedgerInTransitAccount("NOK"));
	}
	
	@Test
	public void multipleCurrencies() {
		PaymentBatchChannelOptions opts = optionsWith("1991,{1992:EUR},{1993:USD}", "SEK");
		assertEquals("1991", opts.getGeneralLedgerInTransitAccount());
		assertEquals("1991", opts.getGeneralLedgerInTransitAccount("SEK"));
		assertEquals("1992", opts.getGeneralLedgerInTransitAccount("eur"));
		assertEquals("1993", opts.getGeneralLedgerInTransitAccount("USD"));
		assertThrows(UnknownCurrencyAccountException.class, () -> opts.getGeneralLedgerInTransitAccount("NOK"));
		assertEquals("1991,{1992:EUR},{1993:USD}", opts.getGeneralLedgerInTransitAccountDefinition());
	}

	@Test
	public void unsetPropertyGivesNull() {
		PaymentBatchChannelOptions opts = optionsWith(null, "SEK");
		assertNull(opts.getGeneralLedgerInTransitAccount());
		assertNull(opts.getGeneralLedgerInTransitAccount("EUR"));
		assertNull(opts.getGeneralLedgerFeeAccount("EUR"));
	}

	@Test
	public void whitespaceIsAccepted() {
		PaymentBatchChannelOptions opts = optionsWith("1991, { 1992 : EUR }", "SEK");
		assertEquals("1991", opts.getGeneralLedgerInTransitAccount());
		assertEquals("1992", opts.getGeneralLedgerInTransitAccount("EUR"));
	}

	@Test
	public void explicitDefaultCurrencyEntry() {
		PaymentBatchChannelOptions opts = optionsWith("{1992:EUR}", "EUR");
		assertEquals("1992", opts.getGeneralLedgerInTransitAccount());
		assertEquals("1992", opts.getGeneralLedgerInTransitAccount("EUR"));
	}

	@Test
	public void noDefaultCurrencySet() {
		PaymentBatchChannelOptions opts = optionsWith("1991", null);
		assertEquals("1991", opts.getGeneralLedgerInTransitAccount());
		assertEquals("1991", opts.getGeneralLedgerInTransitAccount("EUR"));
	}
	
	@Test
	public void unlistedCurrencyFailsWhenCurrenciesAreSpecified() {
		PaymentBatchChannelOptions opts = optionsWith("1991,{1992:EUR}", "SEK");
		UnknownCurrencyAccountException e = assertThrows(UnknownCurrencyAccountException.class, 
				() -> opts.getGeneralLedgerInTransitAccount("NOK"));
		assertEquals("NOK", e.getCurrency());
		assertEquals("generalLedgerInTransitAccount", e.getOptionName());
	}
	
	@Test
	public void malformedDefinitionsAreRejected() {
		PaymentBatchChannelOptions opts = new PaymentBatchChannelOptions();
		for (String bad : new String[] { "1991,{1992}", "1991,1992", "1991,{1992:EUR},{1993:eur}", "1991,", "1991,{1992:EUR", "{:EUR}" }) {
			assertThrows(IllegalArgumentException.class, () -> opts.setGeneralLedgerInTransitAccount(bad), bad);
		}
	}

	@Test
	public void parseEmpty() {
		assertTrue(CurrencyAccountMap.parse(null).isEmpty());
		assertTrue(CurrencyAccountMap.parse("  ").isEmpty());
	}

}
