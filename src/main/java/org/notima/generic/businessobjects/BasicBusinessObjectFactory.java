package org.notima.generic.businessobjects;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.notima.generic.ifacebusinessobjects.BusinessObjectFactory;

/*
 *    Copyright 2020 Notima System Integration AB

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */


/**
 * Boiler plate of a business object factory. This abstract class contains a settings mechanism.
 * 
 * @author Daniel Tamm
 * @param <O>
 *
 */
public abstract class BasicBusinessObjectFactory<C,I,O,P,B> implements BusinessObjectFactory<C,I,O,P,B> {

	protected Map<String,String> settingsMap;
	
	/**
	 * Adds a tenant to given business object factory.
	 * Override this method to add real tenants to a business object factory.
	 *  
	 * @param orgNo				The orgNo
	 * @param countryCode		Country Code
	 * @param props				Properties used for initialization.
	 * @return
	 */
	public BusinessPartner<B> addTenant(String orgNo, String countryCode, Properties props) {
		return null;
	}
	
	
	/**
	 * Returns given setting.
	 * 
	 * @param settingKey	The key of the setting.
	 * @return				Null if the settings doesn't exist. Otherwise a non-null value.
	 */
	public String getSetting(String settingKey) {
		
		if (settingsMap==null)
			return null;
		
		String result = settingsMap.get(settingKey);
		
		return result;
		
	}

	/**
	 * Writes a given setting.
	 * 
	 * @param settingKey	The setting key
	 * @param value			If null and the setting exist, it's remove.
	 */
	public void setSetting(String settingKey, String value) {
		
		if (settingsMap==null) {
			settingsMap = new TreeMap<String, String>();
		}
		
		if (value!=null) {
			settingsMap.put(settingKey, value);
		} else {
			if (settingsMap.get(settingKey)!=null) {
				settingsMap.remove(settingKey);
			}
		}
		
	}

	/**
	 * Appends settings from a settings map
	 * 
	 * @param	settings	An existing settings map. This is copied to the internal settings map.
	 */
	public void appendSettings(Map<String, String> settings) {
		
		if (settings==null) return;
		
		if (settingsMap==null) {
			settingsMap = new TreeMap<String, String>();
		}
		
		for (String s : settings.keySet()) {
			settingsMap.put(s, settings.get(s));
		}
		
	}

	@Override
	public List<AccountingVoucher> writeVouchers(List<AccountingVoucher> vouchers) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
