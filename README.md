# businessobjects
A common generic format used to transform business objects (ie invoices, customers etc) between different formats.

## Use

Releases are found in Maven-Central repository and can easliy be used by adding a dependency to you pom.xml.

    <dependency>
        <groupId>org.notima.generic</groupId>
        <artifactId>businessobjects</artifactId>
        <version>0.1.16</version>
    </dependency>

Or in karaf

	install -s mvn:javax.persistence/javax.persistence-api/2.2
	install -s mvn:org.notima.bg/bankgiro-api
	install -s mvn:org.notima.generic/businessobjects
	
