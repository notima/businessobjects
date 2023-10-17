package org.notima.generic.businessobjects;

/**
 * Holder class for reporting an invoice operation.
 * 
 * @author Daniel Tamm
 *
 */
public class InvoiceOperationResult {

	private boolean successful;

	private int		updateCount = 0;
	private int		createCount = 0;
	
	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	public int getCreateCount() {
		return createCount;
	}

	public void setCreateCount(int createCount) {
		this.createCount = createCount;
	}
	
}
