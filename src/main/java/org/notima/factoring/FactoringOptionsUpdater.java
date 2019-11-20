package org.notima.factoring;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import org.notima.generic.businessobjects.Order;

/**
 * Interface to get Factoring Options.
 * 
 * A Factoring provider wishing to provide dynamic factoring options should have a class implementing
 * this interface.
 * 
 * @author Daniel Tamm
 *
 */
public interface FactoringOptionsUpdater {

	/**
	 * Return available factoring options for a specific order.
	 * 
	 * @param ctx
	 * @param order
	 * @param trxName
	 * @return
	 * @throws Exception
	 */
	public Map<String,FactoringOption> getFactoringOptions(Properties ctx, Order<?> order, BigDecimal amount, String trxName) throws Exception;
	
}
