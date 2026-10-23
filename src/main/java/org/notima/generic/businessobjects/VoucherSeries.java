package org.notima.generic.businessobjects;

/**
 * Defines one voucher series within an accounting period — e.g. series "A"
 * with the last voucher number used.
 *
 * <p>Voucher series are used to number vouchers sequentially within a period.
 * When a new period is created, copying the series definitions (with their
 * last numbers) from a prior period lets users continue the numbering sequence.</p>
 */
public class VoucherSeries {

	/** Series identifier, e.g. {@code "A"}, {@code "LV"}, {@code "KV"}. */
	private String id;
	/** Optional human-readable name, e.g. {@code "Leverantörsfakturor"}. */
	private String name;
	/** Last voucher number used in this series. {@code 0} if unused. */
	private int lastNo;

	public VoucherSeries() {}

	public VoucherSeries(String id, int lastNo) {
		this.id     = id;
		this.lastNo = lastNo;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLastNo() {
		return lastNo;
	}
	public void setLastNo(int lastNo) {
		this.lastNo = lastNo;
	}

	@Override
	public String toString() {
		return id != null ? id + " (last: " + lastNo + ")" : "";
	}
}
