package org.notima.generic.businessobjects;

import java.time.LocalDateTime;

import org.notima.util.NumberUtils;

public class CurrencyExchangeRate {

	private Currency toCurrency;
	private Currency fromCurrency;
	
	private LocalDateTime	validFrom;
	private LocalDateTime	validUntil;
	
	private double	 exchangeRate;

	public String toString() {
		
		StringBuffer buf = new StringBuffer();
		buf.append(getCurrencySymbol(fromCurrency));
		buf.append("=>");
		buf.append(getCurrencySymbol(toCurrency));
		buf.append("@");
		buf.append(NumberUtils.roundToPrecision(exchangeRate, toCurrency.getExchangePrecision()));

		return buf.toString();
	}
	
	private String getCurrencySymbol(Currency currency) {
		if (currency == null || currency.getCurrencyISO()==null) return "?";
		return currency.getCurrencyISO().toUpperCase();
	}
	
	public Currency getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(Currency toCurrency) {
		this.toCurrency = toCurrency;
	}

	public Currency getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(Currency fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public LocalDateTime getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDateTime validFrom) {
		this.validFrom = validFrom;
	}

	public LocalDateTime getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(LocalDateTime validUntil) {
		this.validUntil = validUntil;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	
	
}
