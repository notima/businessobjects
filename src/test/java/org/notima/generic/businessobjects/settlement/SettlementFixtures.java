package org.notima.generic.businessobjects.settlement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.Invoice;
import org.notima.generic.businessobjects.InvoiceLine;
import org.notima.generic.businessobjects.Payment;
import org.notima.util.LocalDateUtils;

/** Builds invoices and payments for the settlement tests. */
final class SettlementFixtures {

	static final LocalDate DATE = LocalDate.of(2026, 10, 1);

	private SettlementFixtures() {}

	static BusinessPartner<Object> partner(String no, String name) {
		BusinessPartner<Object> bp = new BusinessPartner<Object>();
		bp.setIdentityNo(no);
		bp.setName(name);
		return bp;
	}

	/** An invoice of one line per {net, VAT rate} pair, calculated. */
	static Invoice<Object> invoice(String key, boolean sales, double... netAndRate) {
		Invoice<Object> inv = new Invoice<Object>();
		inv.setLines(new ArrayList<InvoiceLine>());
		inv.setInvoiceKey(key);
		inv.setSalesTransaction(sales);
		inv.setCurrency("SEK");
		inv.setInvoiceDate(LocalDateUtils.asDate(DATE));
		inv.setDueDate(LocalDateUtils.asDate(DATE.plusDays(30)));
		inv.setBusinessPartner(partner("1001", sales ? "Kund AB" : "Leverantör AB"));
		for (int i = 0; i < netAndRate.length; i += 2) {
			InvoiceLine il = new InvoiceLine();
			il.setQtyEntered(1);
			il.setPriceActual(netAndRate[i]);
			il.setTaxPercent(netAndRate[i + 1]);
			inv.addInvoiceLine(il);
		}
		inv.calculateGrandTotal();
		return inv;
	}

	static Payment<Object> payment(String id, boolean customer, double amount) {
		Payment<Object> p = new Payment<Object>();
		p.setPaymentId(id);
		p.setCustomerPayment(customer);
		p.setAmount(amount);
		p.setOriginalAmount(amount);
		p.setCurrency("SEK");
		p.setPaymentDate(LocalDateUtils.asDate(DATE.plusDays(10)));
		return p;
	}

	static BigDecimal bd(String s) {
		return new BigDecimal(s);
	}

	static PaymentAllocation completed(PaymentAllocationLine... lines) {
		PaymentAllocation a = new PaymentAllocation(DATE.plusDays(10), "SEK");
		for (PaymentAllocationLine l : lines) a.addLine(l);
		a.setStatus(DocumentStatus.COMPLETED);
		return a;
	}

}
