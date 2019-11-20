package org.notima.generic.businessobjects.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class TestSettings {

	public static final String[] ORDER_EXAMPLE_XMLS = 
			new String[] {
				"OrderExample.xml",
				"OrderExample2.xml"};
	
	public static File getOrderExampleFile(int idx) throws FileNotFoundException {
		
		URL url = TestSettings.class.getClassLoader().getResource(ORDER_EXAMPLE_XMLS[idx]);
		if (url==null) {
			throw new FileNotFoundException("Can't find in classpath: " + ORDER_EXAMPLE_XMLS[idx]);
		}

		return new File(url.getFile());
		
	}
	
}
