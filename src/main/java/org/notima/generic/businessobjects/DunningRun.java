package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.xml.bind.annotation.XmlRootElement;

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
	
	public DunningEntry<?,?> getFirstEntryForDebtor(BusinessPartner<?> debtor) {
		
		if (entries==null) return null;
		if (debtor.getIdentityNo()==null) return null;
		
		for (DunningEntry<?,?> entry : entries) {
			if (entry.getDebtor()!=null && entry.getDebtor().getIdentityNo()!=null && entry.getDebtor().getIdentityNo().equals(debtor.getIdentityNo())) {
				return entry;
			}
		}
		return null;
		
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
