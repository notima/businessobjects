package org.notima.generic.businessobjects;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@Entity
@XmlRootElement(name = "PriceList")
@XmlSeeAlso(PriceListLine.class)
public class PriceList {

	@Id
	@GeneratedValue
	private Integer		id;
	
	@ManyToOne
	private BusinessPartner<?>		seller;
	@ManyToOne
	private BusinessPartner<?>		buyer;
	@OneToMany
	private List<PriceListLine> priceListLines;
	private String				name;
	private java.util.Date		listDate;
	private String				currency;
	private String 				pricelistReference;
	private int					pricePrecision;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BusinessPartner<?> getSeller() {
		return seller;
	}
	public void setSeller(BusinessPartner<?> seller) {
		this.seller = seller;
	}
	public BusinessPartner<?> getBuyer() {
		return buyer;
	}
	public void setBuyer(BusinessPartner<?> buyer) {
		this.buyer = buyer;
	}
	public List<PriceListLine> getPriceListLines() {
		return priceListLines;
	}
	public void setPriceListLines(List<PriceListLine> priceListLines) {
		this.priceListLines = priceListLines;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public java.util.Date getListDate() {
		return listDate;
	}
	public void setListDate(java.sql.Date listDate) {
		this.listDate = listDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public void addPriceListLine(PriceListLine line) {
		if (priceListLines==null)
			priceListLines = new ArrayList<PriceListLine>();
		priceListLines.add(line);
	}
	
	public String getPricelistReference() {
		return pricelistReference;
	}
	public void setPricelistReference(String pricelistReference) {
		this.pricelistReference = pricelistReference;
	}
	
	/**
	 * The number of decimals used / regarded on this pricelist
	 * 
	 * @return
	 */
	public int getPricePrecision() {
		return pricePrecision;
	}
	public void setPricePrecision(int pricePrecision) {
		this.pricePrecision = pricePrecision;
	}
	
	
	
}
