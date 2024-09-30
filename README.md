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

	feature:install camel-gson

	install mvn:jakarta.activation/jakarta.activation-api/2.1.3
	install mvn:jakarta.persistence/jakarta.persistence-api/3.2.0
	install mvn:jakarta.xml.bind/jakarta.xml.bind-api/3.0.2
	

	install -s mvn:org.notima/notima-util/0.0.7
	install -s mvn:org.notima.generic/businessobjects/1.0.1
	
