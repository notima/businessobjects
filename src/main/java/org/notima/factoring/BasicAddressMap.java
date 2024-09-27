package org.notima.factoring;

import java.util.Map;

public class BasicAddressMap extends AbstractAddress {

	private Map<String,String>	m_adrMap; 
	
	public BasicAddressMap(Map<String, String> adr) {
		m_adrMap = adr;
	}
	
	
	public Map<String,String> getAddressMap() {
		return(m_adrMap);
	}
}
