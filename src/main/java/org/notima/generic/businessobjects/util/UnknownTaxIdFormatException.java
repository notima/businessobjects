package org.notima.generic.businessobjects.util;

/**
 * Thrown if the format identifier is unknown.
 * 
 * @author Daniel Tamm
 *
 */
public class UnknownTaxIdFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9101032631801818427L;

	public UnknownTaxIdFormatException(String srcFormat) {
		super(srcFormat);
	}
	
}
