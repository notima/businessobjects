package org.notima.generic.businessobjects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A snapshot of open items (unpaid invoices and unallocated payments) at a
 * specific date, all belonging to the same {@link AccountElement}.
 * <p>
 * Typical use cases are aged-debt reports and ledger listings. Every
 * {@link OpenItem} in the list shares:
 * <ul>
 *   <li>{@link #snapshotDate} — the reporting date the snapshot was taken.</li>
 *   <li>{@link #accountElement} — the AR/AP ledger account the items belong to.</li>
 * </ul>
 */
@XmlRootElement(name = "openItemList")
public class OpenItemList {

	/** The date at which this snapshot was taken. */
	private LocalDate		snapshotDate;

	/**
	 * The ledger account all items in this list belong to.
	 * Typically an AR account ({@link AccountElement.AccountElementType#BalanceAcct})
	 * for sales open items, or an AP account for vendor open items.
	 */
	private AccountElement	accountElement;

	private List<OpenItem>	openItems = new ArrayList<>();

	// ------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------

	public OpenItemList() {}

	public OpenItemList(LocalDate snapshotDate, AccountElement accountElement) {
		this.snapshotDate = snapshotDate;
		this.accountElement = accountElement;
	}

	// ------------------------------------------------------------------
	// List management
	// ------------------------------------------------------------------

	public void addOpenItem(OpenItem item) {
		openItems.add(item);
	}

	/**
	 * Returns the sum of all {@link OpenItem#getAmount()} values.
	 *
	 * @return Total open amount (positive = net receivable, negative = net payable).
	 */
	public double getTotalAmount() {
		double total = 0.0;
		for (OpenItem item : openItems) {
			total += item.getAmount();
		}
		return total;
	}

	// ------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------

	public LocalDate getSnapshotDate() {
		return snapshotDate;
	}

	public void setSnapshotDate(LocalDate snapshotDate) {
		this.snapshotDate = snapshotDate;
	}

	public AccountElement getAccountElement() {
		return accountElement;
	}

	public void setAccountElement(AccountElement accountElement) {
		this.accountElement = accountElement;
	}

	@XmlElement(name = "openItem")
	public List<OpenItem> getOpenItems() {
		return openItems;
	}

	public void setOpenItems(List<OpenItem> openItems) {
		this.openItems = openItems;
	}
}
