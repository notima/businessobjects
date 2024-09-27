# businessobjects
A common generic format used to transform business objects (ie invoices, customers etc) between different formats.

## Use

Releases are found in Maven-Central repository and can easliy be used by adding a dependency to you pom.xml.

    <dependency>
        <groupId>org.notima.generic</groupId>
        <artifactId>businessobjects</artifactId>
        <version>1.0.1</version>
    </dependency>

Or in karaf 4.4.6

	repo-add mvn:org.apache.camel.karaf/apache-camel/4.7.0/xml/features

	feature:install camel-jaxb
	feature:install camel-jpa
	feature:install camel-gson

	install -s mvn:org.notima/notima-util/0.0.7
	install -s mvn:org.notima.generic/businessobjects/1.0.1
	
