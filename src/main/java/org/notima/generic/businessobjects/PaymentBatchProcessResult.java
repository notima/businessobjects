package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

public class PaymentBatchProcessResult {

	public enum ResultCode {
		NOT_PROCESSED,
		OK,
		OK_WITH_WARNING,
		OK_WITH_RETRY_RECORDS,
		FAILED
	}
	
	private boolean	processedWithoutErrors = true;
	private boolean	processedWithoutWarnings = true;
	
	private int	processedPaymentsCount = 0;
	private int matchedPaymentsCount = 0;
	
	private boolean	dryRun = false;
	private boolean	dryRunSuccessful = false;
	
	private ResultCode	resultCode = ResultCode.NOT_PROCESSED;
	
	private StringBuffer	textResult;
	
	private List<PaymentProcessResult>	paymentResults = new ArrayList<PaymentProcessResult>();
	private List<AccountingVoucher>		vouchers = new ArrayList<AccountingVoucher>();
	
	public boolean isProcessedWithoutErrors() {
		return processedWithoutErrors;
	}

	public void addPaymentProcessResult(PaymentProcessResult prp) {
		if (prp==null) return;
		paymentResults.add(prp);
		if (prp.getResultCode().equals(PaymentProcessResult.ResultCode.OK)) {
			matchedPaymentsCount++;
		}
		if (!prp.getResultCode().equals(PaymentProcessResult.ResultCode.NOT_PROCESSED)) {
			processedPaymentsCount++;
		}
		if (prp.getResultCode().equals(PaymentProcessResult.ResultCode.FAILED)) {
			processedWithoutErrors = false;
		}
		
		if (prp.getResultCode().equals(PaymentProcessResult.ResultCode.OK_WITH_RETRY_RECORDS)) {
			processedWithoutErrors = false;
		}
		
		if (prp.getResultCode().equals(PaymentProcessResult.ResultCode.OK_WITH_WARNING)) {
			processedWithoutWarnings = false;
		}
		
	}
	
	public void setProcessedWithoutErrors(boolean processedWithoutErrors) {
		this.processedWithoutErrors = processedWithoutErrors;
	}

	public boolean isProcessedWithoutWarnings() {
		return processedWithoutWarnings;
	}

	public void setProcessedWithoutWarnings(boolean processedWithoutWarnings) {
		this.processedWithoutWarnings = processedWithoutWarnings;
	}

	public int getProcessedPaymentsCount() {
		return processedPaymentsCount;
	}

	public void setProcessedPaymentsCount(int processedPaymentsCount) {
		this.processedPaymentsCount = processedPaymentsCount;
	}

	public int getMatchedPaymentsCount() {
		return matchedPaymentsCount;
	}

	public void setMatchedPaymentsCount(int matchedPaymentsCount) {
		this.matchedPaymentsCount = matchedPaymentsCount;
	}

	public boolean isDryRun() {
		return dryRun;
	}

	public void setDryRun(boolean dryRun) {
		this.dryRun = dryRun;
	}

	public boolean isDryRunSuccessful() {
		return dryRunSuccessful;
	}

	public void setDryRunSuccessful(boolean dryRunSuccessful) {
		this.dryRunSuccessful = dryRunSuccessful;
	}

	public ResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	} 
	
	public StringBuffer getTextResult() {
		return textResult;
	}

	public void setTextResult(StringBuffer textResult) {
		this.textResult = textResult;
	}
	
	/**
	 * Appends a line / message to the text result
	 * 
	 * @param msg		The message to append.
	 */
	public void appendMessage(String msg) {
		if (textResult==null) {
			textResult = new StringBuffer();
		}
		if (textResult.length()>0) {
			textResult.append("\n");
		}
		textResult.append(msg);
	}

	/**
	 * @return	The results of the processed payments.
	 */
	public List<PaymentProcessResult> getPaymentResults() {
		return paymentResults;
	}
	
	/**
	 * Adds a voucher created in the destination system (or, in a dry run, a voucher 
	 * that would have been created).
	 * 
	 * @param voucher	The voucher.
	 */
	public void addVoucher(AccountingVoucher voucher) {
		if (voucher==null) return;
		vouchers.add(voucher);
	}
	
	/**
	 * @return	Vouchers created (or that would have been created in a dry run).
	 */
	public List<AccountingVoucher> getVouchers() {
		return vouchers;
	}
	
}
