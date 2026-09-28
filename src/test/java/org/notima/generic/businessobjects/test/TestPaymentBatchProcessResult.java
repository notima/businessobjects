package org.notima.generic.businessobjects.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.AccountingVoucher;
import org.notima.generic.businessobjects.PaymentBatchProcessResult;
import org.notima.generic.businessobjects.PaymentProcessResult;
import org.notima.generic.businessobjects.PaymentProcessResult.ResultCode;

public class TestPaymentBatchProcessResult {

	@Test
	public void keepsPaymentResultsAndCounts() {
		PaymentBatchProcessResult result = new PaymentBatchProcessResult();
		PaymentProcessResult ok = new PaymentProcessResult(ResultCode.OK);
		PaymentProcessResult failed = new PaymentProcessResult(ResultCode.FAILED);
		PaymentProcessResult notProcessed = new PaymentProcessResult(ResultCode.NOT_PROCESSED);
		result.addPaymentProcessResult(ok);
		result.addPaymentProcessResult(failed);
		result.addPaymentProcessResult(notProcessed);
		result.addPaymentProcessResult(null);

		assertEquals(3, result.getPaymentResults().size());
		assertSame(ok, result.getPaymentResults().get(0));
		assertEquals(1, result.getMatchedPaymentsCount());
		assertEquals(2, result.getProcessedPaymentsCount());
		assertFalse(result.isProcessedWithoutErrors());
	}

	@Test
	public void keepsVouchers() {
		PaymentBatchProcessResult result = new PaymentBatchProcessResult();
		assertTrue(result.getVouchers().isEmpty());
		AccountingVoucher av = new AccountingVoucher();
		result.addVoucher(av);
		result.addVoucher(null);
		assertEquals(1, result.getVouchers().size());
		assertSame(av, result.getVouchers().get(0));
	}

	@Test
	public void notesAreAppended() {
		PaymentProcessResult r = new PaymentProcessResult(ResultCode.OK);
		r.appendNote("first");
		r.appendNote(null);
		r.appendNote("second");
		assertEquals("first; second", r.getTextResult().toString());
	}

}
