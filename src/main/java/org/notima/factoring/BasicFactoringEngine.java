package org.notima.factoring;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import org.notima.generic.businessobjects.*;
import org.notima.generic.ifacebusinessobjects.BusinessObjectFactory;
import org.notima.generic.ifacebusinessobjects.FactoringReservation;

/**
 * Basic factoring engine that produces XML invoices.
 * 
 * @author Daniel Tamm
 * @param <C>
 *
 */
public class BasicFactoringEngine<C,I,O,P,B,T> implements FactoringEngine {

	private Properties m_factoringProperties;
	private DetailRowFactory<?, ?, ?, ?, ?, ?> rowFactory;
	public static final int ROUNDING_DECIMALS = 2;
	

	public BasicFactoringEngine(BusinessObjectFactory<C,I,O,P,B,T> oFactory) {
		rowFactory = new DetailRowFactory<C,I,O,P,B,T>(oFactory);
	}
	
	
	@Override
	public double getPossibleFactoringAmount(BusinessPartner<?> customer,
			Currency currency, BusinessPartner<?> factoringCompany)
			throws Exception {
		return 0;
	}
	
	/**
	 * Checks factoring status for a given customer / order
	 * 
	 * @param invoice
	 * @param factoringCompany
	 * @return 	Possible return values:
	 * 			E = Factoring OK, F = Factoring Denied 
	 * 			Z = Error in communication
	 * @throws Exception - If anything in the order is incomplete.
	 */
	@Override
	public FactoringReservation reserveForFactoring(Invoice<?> invoice, BusinessPartner<?> factoringCompany) throws Exception {
		return null;
	}
	
	/**
	 * Check if factoring is OK.
	 * 
	 * @param order
	 * @param factoringCompany
	 * @return 	Possible return values:
	 * 			E = Factoring OK, F = Factoring Denied 
	 * 			Z = Error in communication
	 * @throws Exception - If anything in the order is incomplete.
	 */
	@Override
	public FactoringReservation reserveForFactoring(Order<?> order, BusinessPartner<?> factoringCompany) throws Exception {
        
        DetailRow[] srcRows = rowFactory.createFromOrder(order, ROUNDING_DECIMALS);
        
        // Get total of order
        double amount = DetailRowUtility.calculateTotalAmountWithTax(srcRows, ROUNDING_DECIMALS);

        if (amount==0.0) {
            throw new Exception("The order has no value");
        }
        
		
		// Get invoice address
		@SuppressWarnings("unused")
		Location billLocation;
		BusinessPartner<?> bpartner;
		Person user = order.getBillPerson();
		if (user==null) {
			throw new Exception ("Vänligen ange fakturakontakt");
			// If no bill User ID is defined, use AD_User ID
			//user = (MUser)order.getAD_User();
		}
		bpartner = order.getBpartner();
		
		// Tax id
		String taxId = bpartner.getTaxId();
		// Clean up dashes and spaces.
		taxId = toDigitsOnly(taxId);

		// Invoice address
		billLocation = order.getBillLocation();
		bpartner = order.getBillBpartner();
        
        String phone = user.getPhone(); if (phone == null) phone = "";
        String cell = user.getPhone2(); if (cell == null) cell = "";
        String email = user.getEmail(); if (email == null) email = "";

        Calendar orderDate = Calendar.getInstance();
        orderDate.setTime(order.getDateOrdered());
        

        JAXBContext contextObj = JAXBContext.newInstance(BasicDetailRow.class);
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerObj.setProperty(Marshaller.JAXB_FRAGMENT, true);
        for(int i=0;i<srcRows.length; i++) {
            marshallerObj.marshal((BasicDetailRow)srcRows[i], System.out);  
        }
        
        // BasicAddressMap address = makeAddressFromLocation(bpartner, user, location);
        
        boolean success = true;
        
        if (success) {
            FactoringReservation ref = new BasicFactoringReservation();
            ref.setAmount(amount);
            ref.setOrderNo(order.getOrderKey());
            ref.setCurrency( order.getCurrency() );
            ref.setReservationResponseCode(FactoringReservation.FACTORING_OK);
			return ref;
        }
        
        BasicFactoringReservation failed = new BasicFactoringReservation();
        failed.setReservationResponseCode(FactoringReservation.FACTORING_DENIED);
		return failed;
	}


	@Override
	public FactoringReply sendToFactoring(BusinessPartner<?> factoringCompany,
			Invoice<?> invoice) throws Exception {

        DetailRow[] srcRows = rowFactory.createFromInvoice(invoice, ROUNDING_DECIMALS);

        JAXBContext contextObj = JAXBContext.newInstance(BasicDetailRow.class);
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerObj.setProperty(Marshaller.JAXB_FRAGMENT, true);
        for(int i=0;i<srcRows.length; i++) {
            marshallerObj.marshal((BasicDetailRow)srcRows[i], System.out);  
        }
        
        return(new FactoringReply(true));
	}

	/**
	 * This "hook" is called after sendToFactoring has been called. The purpose of this hook is
	 * if any clean up etc must be done after an invoice has been sent.
	 * 
	 * @param factoringCompany
	 * @param invoice
	 * @param reply
	 * @throws Exception
	 */
	public void afterSendToFactoring(
			BusinessPartner<?> factoringCompany, 
			Invoice<?> invoice, 
			FactoringReply reply) throws Exception {
	}	
	
	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public FactoringReply returnToFactoring(BusinessPartner<?> factoringCompany,
			Invoice<?> invoice) throws Exception {
		return null;
	}

	/**
	 * Remove all non digit characters
	 *
	 * @param cleanUp
	 * @return
	 */
	public static String toDigitsOnly(String cleanUp) {
		if (cleanUp==null) return("");
		StringBuffer buf = new StringBuffer();
		char c;
		for (int i=0; i<cleanUp.length();i++) {
			c = cleanUp.charAt(i);
			if (c>='0' && c<='9') {
				buf.append(c);
			}
		}
		return(buf.toString());
	}
	
	/**
	 * Creates an address that's usable by the factoring engine.
	 * 
	 * @param customer
	 * @param user
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public BasicAddressMap makeAddressFromLocation(BusinessPartner<?> customer, Person user, Location location) throws Exception {

		String countryCode = location.getCountryCode();
		boolean isCompany = false;

		String countryStr = countryCode.toLowerCase();
		
		StringBuffer street = new StringBuffer();
		if (location.getAddress1()!=null && location.getAddress1().trim().length()>0) {
			street.append(location.getAddress1());
		}
		if (location.getAddress2()!=null && location.getAddress2().trim().length()>0) {
			if (street.length()>0) street.append("\n");
			street.append(location.getAddress2());
		}
		if (location.getAddress3()!=null && location.getAddress3().trim().length()>0) {
			if (street.length()>0) street.append("\n");
			street.append(location.getAddress3());
		}
		if (location.getAddress4()!=null && location.getAddress4().trim().length()>0) {
			if (street.length()>0) street.append("\n");
			street.append(location.getAddress4());
		}
		
		String fname = null;
		String lname = null;
		if (!customer.isCompany()) {
			fname = user.getFirstName();
			lname = user.getLastName();
			if (fname==null || fname.trim().length()==0 || lname==null || lname.trim().length()==0) {
				// Split into first and last name
				fname = customer.getName();
				int firstSpace = fname.lastIndexOf(" ");
				if (firstSpace>0 && firstSpace<(fname.length()-1)) {
					lname = fname.substring(firstSpace+1);
					fname = fname.substring(0, firstSpace);
				}
			}
		} else {
			isCompany = true;
			// Legal entity
			fname = customer.getName();
			lname = "";
		}
		
		// Create Address
		Map<String,String> adr = (Map<String,String>)mk_address(fname, lname, street.toString(), location.getPostal(), location.getCity(), countryStr, isCompany);
		
		BasicAddressMap result = new BasicAddressMap(adr);
		
		return(result);
		
	}
	
	
    /*************************************************************************
     * API: mk_address
     *************************************************************************/
    protected Map<String,String> mk_address(String fname, String lname, String street,
            String postno, String city, String country, boolean isCompany) {
        Map<String, String> address = new Hashtable<String, String>();
        address.put("fname", fname);
        address.put("lname", lname);
        address.put("street", street);
        address.put("zip", postno);
        address.put("city", city);
        address.put("country", country);
        if (isCompany) {
        	address.put("isCompany", "true");
        }
        return address;
    }

	@Override
	public FactoringReservation cancelReservation(Order<?> order, FactoringReservation reservation) throws Exception {
		return null;
	}
	
	public void setFactoringProperties(Properties props) {
		m_factoringProperties = props;
	}
	
	public Properties getFactoringProperties() {
		return m_factoringProperties;
	}
	
	
}
