package org.notima.generic.businessobjects;

import java.time.LocalDate;

/**
 * Describes a setting
 * 
 */
public class Setting {

	private String 	settingsKey;
	private String	name;
	private String  i18nameKey;
	private String	description;
	private String	i18descriptionKey;
	private String	stringValue;
	private LocalDate	dateValue;
	private boolean	file;
	private boolean directory;
	
	public String getSettingsKey() {
		return settingsKey;
	}
	public void setSettingsKey(String settingsKey) {
		this.settingsKey = settingsKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getI18nameKey() {
		return i18nameKey;
	}
	public void setI18nameKey(String i18nameKey) {
		this.i18nameKey = i18nameKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getI18descriptionKey() {
		return i18descriptionKey;
	}
	public void setI18descriptionKey(String i18descriptionKey) {
		this.i18descriptionKey = i18descriptionKey;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public LocalDate getDateValue() {
		return dateValue;
	}
	public void setDateValue(LocalDate dateValue) {
		this.dateValue = dateValue;
	}
	public boolean isFile() {
		return file;
	}
	public void setFile(boolean file) {
		this.file = file;
	}
	public boolean isDirectory() {
		return directory;
	}
	public void setDirectory(boolean directory) {
		this.directory = directory;
	}
	
	
	
}
