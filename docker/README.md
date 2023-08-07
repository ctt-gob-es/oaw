# Deployment Instructions

In this section, you will find the various instructions to start the application using Docker Compose.


## Deployment on Linux

The following instructions have been performed on Ubuntu 22.04. It's possible that on other distributions or versions of the same, some of these steps might be different or even unnecessary.


### 1. Prerequisites

To perform the following steps, you need to navigate to the `docker` folder and have the following software installed on your system:

* [Apache Maven](https://maven.apache.org/what-is-maven.html) 3.6.3
* [OpenSSL](https://www.openssl.org/) 3.0.2
* [openjdk-8-jdk](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html) (Not compatible with higher versions)
* [Docker](https://docs.docker.com/get-started/overview/) 24.0.5
* [Docker Compose](https://docs.docker.com/compose/) 2.20.2

*Note: The specified versions are those with which the dockerized version has been developed. It might work with other versions except for openjdk-8-jdk.

### 2. Generating Nginx Certificates

In order for our Nginx container to function, it's necessary to add a key and certificate to a specific folder within the Docker volume.

You can generate the key using the following command:

```bash
openssl genpkey -algorithm RSA -out ../motor-js/nginx/certs/server.key
```

Once generated, we will use it to create a self-signed certificate:

```bash
openssl req -new -key ../motor-js/nginx/certs/server.key -x509 -out ../motor-js/nginx/certs/server.crt
```

Finally, you will be prompted to enter some details for the certificate generation. You can invent these details since this certificate will only be used as a test for our local deployment.

*Note: The default validity period for a certificate is 30 days.

### 3. Database Selection (Optional)

This step is not necessary unless you want to change the name or URL to which the database points.

The selection of the database is done within the specified profile. For deployment using Docker, the profile used is `docker`. You can find this profile in the path `portal/profiles/docker`, and the relevant file is `context.xml`.

Inside this file, you can find the `url` of the database that will be used.

```xml
<Context path="/oaw" reloadable="true">
	<Resource auth="Container" driverClassName="com.mysql.cj.jdbc.Driver"
	type="javax.sql.DataSource" name="jdbc/oaw" url="jdbc:mysql://mysql:3306/OAW"
	maxActive="100" maxIdle="10" maxWait="-1"
	validationQuery="SELECT 1 as dbcp_connection_test" removeAbandoned="true"
	testOnBorrow="true" timeBetweenEvictionRunsMillis="60000"
	testWhileIdle="true" defaultTransactionIsolation="READ_UNCOMMITTED"
	username="root" password="root" />
</Context>
```

Please note that this step is optional and should only be performed if you intend to change the database configuration.

### 4. Generating the War File

Ensure that you have JDK version 8 installed beforehand, as higher versions are not compatible.

```bash
sudo apt install openjdk-8-jdk
```

Once installed, in the `/oaw/` folder within the root directory of the project, perform the following test:

```bash
mvn compile
```

If you encounter any failures, check the contents of the `$JAVA_HOME` environment variable. If it is empty or pointing to a different JDK version, copy and paste the following command in your terminal:

```bash
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
```

If you still encounter the same error, ensure that you have `jdk` installed and not just `jre`. You can have both versions, but you definitely need `jdk`.

Please note that this step involves generating the "war" file for your application.
Other commands that might be helpful are:

```bash
# Selecting a specific version of Java from those installed on the system
sudo update-alternatives --config java

# Environment variable for the PATH (requires the previous export)
export PATH=$PATH:$HOME/bin:$JAVA_HOME/bin
```

Once you've verified that the compilation is successful, generate the war file with the following command in the same directory mentioned earlier:

```bash
mvn clean install -P docker -Dmaven.test.skip=true
```

**Note:** `-P` refers to the specified profile, which in this case is `docker` since that's the one we're interested in. This profile contains various parameters, including the URL of the database that we will use once the `war` file is generated.

Finally, you will find the generated `war` file in the `portal/target` folder with the name `oaw.war`. **Do not change the name or move the `war` file from this location.**

### 5. Startup

Once the `war` file is generated, navigate to the `docker` folder located in the root directory and start the containers using `docker compose`.

```bash
docker compose up -d --build
```

The database volume is located in the `/docker/volumes/mysql` folder. Remember that if this folder is present in the directory, MySQL will internally load the existing database and will not generate a new one from the SQL scripts.

To reset the database, simply delete the `/docker/volumes/mysql` folder.

**Note:** Do not confuse this folder with `docker/mysql`. The latter contains the necessary SQL scripts to generate the initial volume.

### 6. Checks

Tomcat is running at [http://localhost:18081](http://localhost:18081/)

If all the steps have been executed correctly, you should find the deployed application at [http://localhost:18081/oaw](http://localhost:18081/oaw)