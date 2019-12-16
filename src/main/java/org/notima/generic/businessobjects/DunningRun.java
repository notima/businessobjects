package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "DunningRun")
public class DunningRun <B,I> {
	private int nextLineNo = 10;
	private int lineNoIncrement = 10;
	
	private String letterNoPrefix;
	private String ocrNoPrefix;

	private Collection<DunningEntry<?,?>> entries = new ArrayList<DunningEntry<?,?>>();
	

	public DunningRun(){
		
	};
	
	public void addDunningEntry(DunningEntry<?,?> de) {
		if (de.getLineNo()==0) {
			de.setLineNo(nextLineNo);
			nextLineNo+=lineNoIncrement;
		}
		de.calculateValues();
		entries.add(de);
	}
	
	private void setPrefixValues(DunningEntry<?,?> de, String lp, String op){
		if(op != null){
			de.setOcrNoPrefix(op);
			de.calculateOcrNo();
		}
		if(lp != null){
			de.setLetterNoPrefix(lp);
			de.calculateLetterNo();
		}
	}
	
	public Collection<DunningEntry<?,?>> getEntries() {
		return entries;
	}

	public void setEntries(Collection<DunningEntry<?,?>> entries) {
		this.entries = entries;
	}
	
	public String getLetterNoPrefix() {
		return letterNoPrefix;
	}

	public void setLetterNoPrefix(String letterNoPrefix) {
		this.letterNoPrefix = letterNoPrefix;
		for(DunningEntry<?,?> de : entries){
			setPrefixValues(de, letterNoPrefix, null);
		}
	}

	public String getOcrNoPrefix() {
		return ocrNoPrefix;
	}

	public void setOcrNoPrefix(String ocrNoPrefix) {
		this.ocrNoPrefix = ocrNoPrefix;
		for(DunningEntry<?,?> de : entries){
			setPrefixValues(de, null, ocrNoPrefix);
		}
	}
}
