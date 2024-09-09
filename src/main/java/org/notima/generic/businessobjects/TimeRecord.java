package org.notima.generic.businessobjects;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Time record
 * @author dantam
 *
 */
@XmlRootElement(name = "TimeRecord")
public class TimeRecord {

	private String	recordId;
	private String	name;
	private String	location;
	private String	reference;
	private Date	startTime;
	private Date	endTime;
	private double	hours;
	private String 	externalReference;
	private String	comment;
	
	private BusinessPartner<?> customer;
	private Person	actor;
	
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public String getExternalReference() {
		return externalReference;
	}
	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public BusinessPartner<?> getCustomer() {
		return customer;
	}
	public void setCustomer(BusinessPartner<?> customer) {
		this.customer = customer;
	}
	public Person getActor() {
		return actor;
	}
	public void setActor(Person actor) {
		this.actor = actor;
	}
	
	
	
	
}
