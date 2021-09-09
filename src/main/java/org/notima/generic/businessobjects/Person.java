package org.notima.generic.businessobjects;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private int    personId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String phone2;
	private String name;
	private String passwd;
	private List<KeyValue>	attributes;
	
	public Person() {}
	
	public Person(String name) {
		this.name = name;
	}
	
	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	
	public String getName() {
		if (name==null || name.trim().length()==0) {
			StringBuffer n = new StringBuffer();
			if (firstName!=null && firstName.trim().length()>0) {
				n.append(firstName);
			}
			if (lastName!=null && lastName.trim().length()>0) {
				if (n.length()>0) n.append(" ");
				n.append(lastName);
			}
			return n.toString();
		} else {
			return this.name;
		}
	}

	public boolean hasName() {
		String tempName = getName(); 
		return tempName!=null && tempName.trim().length()>0;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public List<KeyValue> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<KeyValue> attributes) {
		this.attributes = attributes;
	}
	
}
