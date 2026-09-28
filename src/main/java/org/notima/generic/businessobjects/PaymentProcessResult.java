package org.notima.generic.businessobjects;

import java.time.LocalDate;

/**
 * The result of processing a payment in the destination system.
 * 
 * The invoice payment fields describe the payment created in the destination system,
 * or, in a dry run, the payment that would have been created.
 */
public class PaymentProcessResult {

	public enum ResultCode {
		NOT_PROCESSED,
		OK,
		OK_WITH_WARNING,
		OK_WITH_RETRY_RECORDS,
		FAILED
	}

	private ResultCode	resultCode = ResultCode.NOT_PROCESSED;
	
	private Payment<?>		resultingPayment;
	
	private StringBuffer	textResult;

	private Exception		exception;
	
	private String			sourceReference;
	private String			invoiceNo;
	private LocalDate		paymentDate;
	private double			amount;
	private String			currency;
	private Double			acctAmount;
	private String			modeOfPayment;
	private double			writeOffAmount;
	private String			destinationPaymentId;
	private AccountingVoucher	voucher;
	
	public PaymentProcessResult() {};
	
	public PaymentProcessResult(ResultCode code) {
		resultCode = code;
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

	public void setTextResultFromException() {
		if (exception!=null) {
			textResult = new StringBuffer(exception.getMessage());
		}
	}
	
	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Payment<?> getResultingPayment() {
		return resultingPayment;
	}

	public void setResultingPayment(Payment<?> resultingPayment) {
		this.resultingPayment = resultingPayment;
	}

	/**
	 * Appends a note to the text result.
	 * 
	 * @param note	The note to append.
	 */
	public void appendNote(String note) {
		if (note==null) return;
		if (textResult==null) {
			textResult = new StringBuffer();
		}
		if (textResult.length()>0) {
			textResult.append("; ");
		}
		textResult.append(note);
	}

	/**
	 * @return	The source's reference to the payment (ie order reference).
	 */
	public String getSourceReference() {
		return sourceReference;
	}

	public void setSourceReference(String sourceReference) {
		this.sourceReference = sourceReference;
	}

	/**
	 * @return	The invoice that is (or would be) paid.
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * @return	The payment amount in the payment's currency.
	 */
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return	The amount in accounting currency if converted, otherwise null.
	 */
	public Double getAcctAmount() {
		return acctAmount;
	}

	public void setAcctAmount(Double acctAmount) {
		this.acctAmount = acctAmount;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public double getWriteOffAmount() {
		return writeOffAmount;
	}

	public void setWriteOffAmount(double writeOffAmount) {
		this.writeOffAmount = writeOffAmount;
	}

	/**
	 * @return	The id of the payment created in the destination system. Null in a dry run.
	 */
	public String getDestinationPaymentId() {
		return destinationPaymentId;
	}

	public void setDestinationPaymentId(String destinationPaymentId) {
		this.destinationPaymentId = destinationPaymentId;
	}

	/**
	 * @return	A voucher created together with the payment (ie a prepayment voucher), or
	 * 			in a dry run, a voucher that would have been created. Can be null.
	 */
	public AccountingVoucher getVoucher() {
		return voucher;
	}

	public void setVoucher(AccountingVoucher voucher) {
		this.voucher = voucher;
	}
	
}
