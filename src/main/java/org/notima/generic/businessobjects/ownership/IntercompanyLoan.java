package org.notima.generic.businessobjects.ownership;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A loan between two parties as recorded by one of them.
 *
 * <p>The same loan is normally recorded twice: as a debt by the borrower and as a claim by
 * the lender. {@link #getRecordedBy()} tells which side a record comes from, and
 * {@link LoanVerifier} pairs the two records and compares them. The records are paired on
 * {@link #getAgreementId()} when both sides have one.</p>
 */
public class IntercompanyLoan {

	/** The loan is outstanding. */
	public static final String STATUS_ACTIVE = "ACTIVE";

	private String		loanId;
	private String		agreementId;
	private EntityRef	lender;
	private EntityRef	borrower;
	private EntityRef	recordedBy;
	private LocalDate	startDate;
	private LocalDate	closedDate;
	private String		currency;
	private BigDecimal	principal;
	private BigDecimal	outstanding;
	private LocalDate	asOf;
	private String		status;
	private String		description;
	private String		source;

	public IntercompanyLoan() {}

	/** @return	True if the lender recorded this loan (as a claim). */
	public boolean isRecordedByLender() {
		return recordedBy != null && recordedBy.matches(lender);
	}

	/** @return	The party that did not record this loan: the borrower if recorded by the lender. */
	public EntityRef getCounterparty() {
		return isRecordedByLender() ? borrower : lender;
	}

	/** @return	The loan's id in the record keeper's system. */
	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	/**
	 * @return	An id shared by both parties' records of the loan, e.g. a UUID set when one
	 * 			party's record was created from the other's; may be null.
	 */
	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	public EntityRef getLender() {
		return lender;
	}

	public void setLender(EntityRef lender) {
		this.lender = lender;
	}

	public EntityRef getBorrower() {
		return borrower;
	}

	public void setBorrower(EntityRef borrower) {
		this.borrower = borrower;
	}

	/** @return	The party whose records this loan was read from: the lender or the borrower. */
	public EntityRef getRecordedBy() {
		return recordedBy;
	}

	public void setRecordedBy(EntityRef recordedBy) {
		this.recordedBy = recordedBy;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/** @return	When the loan was repaid, written off or cancelled; null while active. */
	public LocalDate getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(LocalDate closedDate) {
		this.closedDate = closedDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/** @return	The principal at the start of the loan. */
	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	/** @return	The outstanding principal at {@link #getAsOf()}; may be null if unknown. */
	public BigDecimal getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(BigDecimal outstanding) {
		this.outstanding = outstanding;
	}

	public LocalDate getAsOf() {
		return asOf;
	}

	public void setAsOf(LocalDate asOf) {
		this.asOf = asOf;
	}

	/** @return	The loan's status, e.g. {@link #STATUS_ACTIVE}, "REPAID" or "WRITTEN_OFF". */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/** @return	Where the loan was read from, e.g. the path of the share register file. */
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return lender + " -> " + borrower + ": " + principal + " " + currency
				+ " (recorded by " + recordedBy + ")";
	}

}
