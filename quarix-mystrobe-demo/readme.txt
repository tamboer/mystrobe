To quickly compile and deploy the sample application you must use:
1. java 1.6 or higher
2. maven build and compile tool
3. mystrobe framework - version specified in pom file of the demo application version

Make sure you correctly configure the server address and application name in web.xml file.
..
<context-param>
	<param-name>ApplicationServerURL</param-name>
	<param-value>AppServer://server:5162/QRXEX</param-value> 
</context-param>
    
<context-param>
	<param-name>AppName</param-name>
	<param-value>qrxexmpl</param-value>
</context-param> 
..

To compile and start application use maven and jetty plugin commands:
mvn clean
mvn jetty:run

A second option is to download the war archive from the MyStroBe site downloads(http://code.google.com/p/mystrobe/downloads/list) section.
You can deploy the war in other web containers not just jetty. 

 
 