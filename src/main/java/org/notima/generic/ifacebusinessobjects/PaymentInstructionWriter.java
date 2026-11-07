package org.notima.generic.ifacebusinessobjects;

import java.io.OutputStream;
import java.util.List;

import org.notima.generic.businessobjects.BusinessPartner;
import org.notima.generic.businessobjects.PaymentBatch;

/**
 * Writes payment instructions for a bank: the payments a company asks its bank to make, ie an
 * ISO 20022 pain.001 file of payments to vendors.
 *
 * <p>The payments are those of a {@link PaymentBatch} on the company's bank account
 * ({@link PaymentBatch#getBankAccount()}, its IBAN and BIC): payments to vendors
 * ({@code customerPayment} false) with their amount, currency, requested execution date
 * ({@link org.notima.generic.businessobjects.Payment#getPaymentDate()}), payee
 * ({@link org.notima.generic.businessobjects.Payment#getBusinessPartner()} with its remit-to
 * account: bankgiro, plusgiro or IBAN) and reference (an OCR number, or else the invoice
 * number). Each payment's {@link org.notima.generic.businessobjects.Payment#getPaymentId() id}
 * is its end-to-end id, which the bank reports back.</p>
 */
public interface PaymentInstructionWriter {

	String getSystemName();

	/** @return	The format written, ie "ISO 20022 pain.001.001.03". */
	String getFormatName();

	/** @return	Extensions of the files written, ie {"xml"}. */
	String[] getFileExtensions();

	/**
	 * @param batch				The payments, on the company's bank account.
	 * @param initiatingParty	The company: its name, and its tax id or the customer id agreed
	 * 							with the bank ({@link BusinessPartner#getTaxId()}).
	 * @return	What would stop the batch from being written, in English; empty if nothing.
	 */
	List<String> validate(PaymentBatch batch, BusinessPartner<?> initiatingParty);

	/**
	 * Writes the payment instructions of {@code batch} to {@code out}.
	 *
	 * @throws Exception	If the batch can't be written, ie because {@link #validate} finds problems.
	 */
	void writePaymentInstructions(PaymentBatch batch, BusinessPartner<?> initiatingParty, OutputStream out) throws Exception;

}
