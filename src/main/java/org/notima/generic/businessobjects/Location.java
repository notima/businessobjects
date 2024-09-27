package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Location {

	@Id
	@GeneratedValue
	private int		locationId;
	
	private String	reference;
	private String	customerReference;
	private String	name;				// In case name differs from bp.
	private String	co;
	private String	address1;
	private String	address2;
	private String	address3;
	private String	address4;
	// Use either street and house No OR Address1 - Address4.
	private String	street;
	private String	houseNo;
	private String	postal;
	private String	city;
	private String	countryCode;
	private String	email;
	private String	phone;
	
	private List<KeyValue> attributes = new ArrayList<KeyValue>();	
	
	
	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * Reference to which customer this location belongs to 
	 * @return
	 */
	public String getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<KeyValue> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<KeyValue> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Trims all fields for easier comparison
	 */
	public void trimAll() {
		if (address1==null)
			address1 = "";
		if (address2==null)
			address2 = "";
		if (address3==null)
			address3 = "";
		if (address4==null)
			address4 = "";
		if (postal==null)
			postal = "";
		if (city==null)
			city = "";
		if (countryCode=="")
			countryCode = "";
		if (email==null)
			email = "";
		if (phone==null)
			phone = "";
		if (name==null)
			name = "";
		address1 = address1.trim();
		address2 = address2.trim();
		address3 = address3.trim();
		address4 = address4.trim();
		postal = postal.trim();
		city = city.trim();
		countryCode = countryCode.trim().toUpperCase();
		email = email.trim();
		phone = phone.trim();
		name = name.trim();
	}

	/**
	 * Compares two location except attributes.
	 * Email, phone and name are compared as well as the address.
	 * 
	 * @param loc
	 * @return
	 */
	public boolean equalsAll(Location loc) {
		
		if (!equalsAddress(loc)) {
			return false;
		}
		
		if (!email.equalsIgnoreCase(loc.email))
			return false;
		
		if (!phone.equalsIgnoreCase(loc.phone))
			return false;
		
		if (!name.equalsIgnoreCase(loc.name))
			return false;
		
		return true;
	}
	
	/**
	 * Compares two locations. Attributes, email, phone or name are not compared.
	 * 
	 * @param loc
	 * @return
	 */
	public boolean equalsAddress(Location loc) {

		this.trimAll();
		loc.trimAll();
		
		if (!address1.equalsIgnoreCase(loc.address1))
			return false;
		if (!address2.equalsIgnoreCase(loc.address2))
			return false;
		if (!address3.equalsIgnoreCase(loc.address3))
			return false;
		if (!address4.equalsIgnoreCase(loc.address4))
			return false;
		
		if (!postal.equalsIgnoreCase(loc.postal))
			return false;
		
		if (!city.equalsIgnoreCase(loc.city))
			return false;
		
		if (!countryCode.equals(loc.countryCode))
			return false;
		
		return true;
		
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	
	
	
}
