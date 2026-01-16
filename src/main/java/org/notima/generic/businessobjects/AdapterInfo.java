package org.notima.generic.businessobjects;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Class that contains data about the specific adapter and where it's connected to
 * 
 */
public class AdapterInfo {

	private String 	systemName;
	private String	systemUrl;
	private String	displayName;
	
	private boolean	online;
	private boolean local;

	private Map<String, Setting> settingsMap = new TreeMap<String,Setting>();	
	private Set<String> requiredSettings = new TreeSet<String>();
	
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemUrl() {
		return systemUrl;
	}

	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	/**
	 * Gets required settings for this business object factory
	 */
	public Set<String> getRequiredSettings() {
		return requiredSettings;
	}
	
	/**
	 * Returns given setting.
	 * 
	 * @param settingKey	The key of the setting.
	 * @return				Null if the settings doesn't exist. Otherwise a non-null value.
	 */
	public Setting getSetting(String settingKey) {
		return settingsMap.get(settingKey);
	}
	
	/**
	 * Writes a given setting.
	 * 
	 * @param settingKey	The setting key
	 * @param value			If null and the setting exist, it's remove.
	 */
	public void setSetting(String settingKey, Setting value) {
		settingsMap.put(settingKey, value);
	}
	
	/**
	 * Appends settings from a settings map
	 * 
	 * @param	settings	An existing settings map. This is copied to the internal settings map.
	 */
	public void appendSettings(Map<String, Setting> settings) {
		if (settings!=null) {
			for (String s : settings.keySet()) {
				settingsMap.put(s, settings.get(s));
			}
		}
	}
	
	
}
