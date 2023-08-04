# Instrucciones de despliegue

En este apartado se recogen las diferentes instrucciones para poder arrancar la aplicación a través de Docker Compose.


## Despligue en Linux

Las siguientes intrucciones se han realizado en Ubuntu 22.04, es posible que en otras distribuciones o versiones de la misma algunos de estos pasos sean diferentes o incluso no sean necesarios.


### 1. Requisitos previos

Para realizar los siguientes pasos es necesario tener instalado en el equipo el siguiente software:
- [Apache Maven](https://maven.apache.org/what-is-maven.html) 3.6.3
- [OpenSSL](https://www.openssl.org/) 3.0.2
- [openjdk-8-jdk](https://www.oracle.com/es/java/technologies/javase/javase8-archive-downloads.html) (No compatible con versiones superiores)
- [Docker](https://docs.docker.com/get-started/overview/) 24.0.5
- [Docker compose](https://docs.docker.com/compose/) 2.20.2

*Nota: Las versiones indicadas son con las que se ha desarrollado la versión dockerizada. Puede funcionar con otras versiones exceptuando el caso de openjdk-8-jdk.

### 2. Generación de certificados Nginx

Para que nuestro contenedor de Nginx funcione es necesario agregarle una clave y certificado en una carpeta epecífica a través de un volumen de Docker.

Puedes generar la clave con el siguiente comando:

```bash
openssl genpkey -algorithm RSA -out ./docker/nginx/certs/server.key >> /dev/null 2>&1
```

Una vez generada la utilizaremos para crear el certificado autofirmado:

```bash
openssl req -new -key ./docker/nginx/certs/server.key -x509 -out ./docker/nginx/certs/server.crt
```

Finalmente se te pedirán unos datos a rellenar para la generación del certificado. 
Puedes inventarte estos datos, ya que este certificado solo lo usaremos como prueba para realizar nuestro despligue local.

*Nota: La validez por defecto de un certificado son 30 días.

### 3. Selección de la Base de datos (Opcional)

Este paso no es necesario, a no ser que quieras cambiar el nombre o la url a la que apunta en busca de la base de datos.

La selección de la base de datos se realiza en el perfil indicado.
Para el despliegue en Docker el perfil utilizado es `docker` , este perfil lo podemos encontrar en la ruta `portal/profiles/docker` y el archivo que nos interesa es `context.xml`.

Dentro de este archivo podemos encontrar la `url` de la base de datos que vamos a utilizar.

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

### 4. Generación del War

Previamente asegurate de tener instalada la versión 8 de JDK, dado que no sirve con versiones superiores.

```bash
sudo apt install openjdk-8-jdk
```

Una vez instalado, en la carpeta `/oaw/` del directorio raíz del proyecto, realiza la siguiente prueba.

```bash
mvn compile
```

En caso de fallo comprueba el contenido de la variable de entorno `$JAVA_HOME`, en caso de que no tenga contenido o que esté apuntando a otra versión del JDK, copia y pega el siguiente comando en tu terminal.

```bash 
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
```

Si sigues teniendo el mismo error compueba que realmente tengas instalado `jdk` y no `jre`.
Puedes tener ambas versiones, pero necesitas si o si `jdk`.

Otros comandos que te pueden servir son:
```bash
# Selección de la versión específica de java de las intaladas en el equipo
sudo update-alternatives --config java

# Variable de entorno para el PATH (necesario el export anterior)
export PATH=$PATH:$HOME/bin:$JAVA_HOME/bin
```

Una vez comprobado que la compilación funciona generamos el war con el siguiente comando situados en el mismo directorio que mencionamos anteriormente.

```bash
mvn clean install -P docker -Dmaven.test.skip=true
```

**Nota:** `-P` se refiere al perfil indicado, siendo en este caso `docker` porque es el que nos interesa. En este perfil se indican varios parámetros, entre ellos, la url de la base de datos que vamos a utilizar una vez generado el `war`.

Finalmente, encontrarás el war generado en la carpeta `portal/target` con el nombre `oaw.war`. **No cambies el nombre ni muevas el war de esta ubicación.**


### 5. Arranque

Una vez generado el war, ve a la carpeta de `docker` que se encuentra en la raíz y levanta los contenedores con `docker compose`.

```bash
docker compose up -d --build
```

El volumen de la base de datos se encuentra en la carpeta `/docker/volumes/mysql`.
Recuerda que si esta carpeta se encuentra en el directorio, Mysql cargará internamente la base de datos existente y no generará una nueva con los scripts SQL.

Para reiniciar la base de datos simplemente elimina la carpeta `/docker/volumes/mysql`. 

**Nota:** No confundir esta carpeta con `docker/mysql`, esta última contiene los script SQL necesarios para generar el volumen inicial.


### 6. Comprobaciones

Tomcat se ejecuta en http://localhost:18081

Por lo que si todos los pasos se han ejecutado correctamente, deberías encontrar la aplicación desplegada en http://localhost:18081/oaw