# OAW
Rastreador Observatorio de Accesibilidad Web

This repository contains 3 applications:

* OAW: Java Web Application.
* Motor JS: An implementation of https://github.com/prerender/prerender to render web pages and return code.
* WCAG EM Tool: An fork of https://github.com/w3c/wcag-em-report-tool with capabiluty to exports as ODS format.

## OAW (Observatorio de Accesibilidad Web)

The application code is distributed in several maven projects:

* common: library with common functions
* crawler: web crawler
* intavcore: analyzer core code
* oaw: "father" project to generate all the libraries and dependencies
* portal: web project of the accessibility observatory

To compile the application, we will use maven (version 3.0.0 or higher). It will be necessary to download a number of dependencies from the central repositories so it needs to be properly frozen. It may be necessary to configure the proxy or a mirror:

* https://maven.apache.org/guides/mini/guide-proxies.html
* https://maven.apache.org/guides/mini/guide-mirror-settings.html

To do this, inside the oaw directory we will execute the following command so that it builds us the complete project:

>	mvn clean install -P development -DskipTests

If everything goes well, a war will be generated in the portal/target folder which will be the one we should display this war in the webapps folder of the tomcat server. 

### Requeriments

Currently OAW is deployment under this configuration:

* Java 1.8.0_202 
* Apache Tomcat 7
* MySQL 5

This is a Maven project that requieres version 3.2.3 or high.


### Instalation

You can find full documentacion (in Spanish) at https://administracionelectronica.gob.es/ctt/oaw/descargas in __Rastreador OAW WCAG 2.1__ section. At this moment, the last documentation avalaible can be downloaded [here](https://administracionelectronica.gob.es/ctt/resources/Soluciones/2431/Descargas/Liberacion-codigo-OAW---MAETD-v5-0-4.zip?idIniciativa=2431&idElemento=19053)

#### MySQL Database

To fresh install execute the scripts locates in folder /portal/scripts from version 4.0.0 to higher version.

#### Tomcat

Create a context configuration like this in *server.xml*:
```xml
<Context path="/oaw" reloadable="true">
    <Resource auth="Container" driverClassName="com.mysql.jdbc.Driver" type="javax.sql.DataSource" name="jdbc/oaw" url="jdbc:mysql://<server>:<port>/<schema>"
    maxActive="100"  maxIdle="10"  maxWait="-1" validationQuery="SELECT 1 as dbcp_connection_test"
    removeAbandoned="true" testOnBorrow="true"
    timeBetweenEvictionRunsMillis="60000" testWhileIdle="true"                                         
    defaultTransactionIsolation="READ_UNCOMMITTED" username="<username>" password="<password>"/>
</Context>
```
Note to change *url*, *port*, *user* and *password* values. In folder *profiles* exists an example of this configuracion. Adapt to your environment.


#### Profiles

There are several parameters that are configurable by environment, as well as configuration files that depend on the environment. In the current project there are two default compilation profiles: development and integration. 

In the oaw project's pom.xml is reflected the configuration for each profile, being possible to create new ones or take advantage of the existing ones. There are also profiles in the portal project folder. 

__It is necessary to review and adapt the configuration of the profiles if necessary.__

#### External properties

In the file /portal/profiles/<profile>/propertiesmanager.properties a series of properties files and their location are indicated. You should configure the files paths according to the information of this file.

* context.xml: Database connection parameters
* mail.properties: Mailing parameters
* basic.service.properties: Parameters of the mail sent by the diagnostic service
* check.descriptions.properties: Explanatory texts for problem solving included in the reports
* check.patterns.properties: Regural expressions and validation patterns


#### Unsatisfied dependencies  in Maven Central

Some of the links are not available in Maven's central repository. They can be downloaded at the following links:1

* javax.jms:jms:jar:1.1: http://www.java2s.com/Code/Jar/j/Downloadjavaxjms11jar.htm
* javax.transaction:jta:jar:1.0.1B: http://www.java2s.com/Code/Jar/j/Downloadjta101bjar.htm

They need to be installed manually: https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html 

## Motor JS

Into folder motor-js contains this tool has 3 packages:

* proxy: entrypoint of tool. Listen for petitions http/s.
* nginx: recivies proxy petitions and handle http and https to renderer.
* renderer: executes https://github.com/prerender/prerender ths listen to http/s requests, renderer the page and return result html

This project is configuring to execute as docker solution



## Templates

From version 5.6.0 new versions of templates are available that must be incorporated into the application. 

These templates can be found in the following path -> /portal/scripts/5.6.0

* hallazgos.odt -> Must be saved as "hallazgos" in the system
* generica_accesible_v1.odt
* segmentos_accesible_v1.odt
* complejidades_accesible_v1.odt
* evolucion_segmentos_accesible_v1.odt
