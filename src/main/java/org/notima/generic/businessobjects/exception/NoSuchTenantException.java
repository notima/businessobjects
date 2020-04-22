package org.notima.generic.businessobjects.exception;

public class NoSuchTenantException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1718266649120190973L;

	public NoSuchTenantException() {
		super();
	}
	
	public NoSuchTenantException(String message) {
		super(message);
	}
	
}
