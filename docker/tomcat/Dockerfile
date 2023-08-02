FROM tomcat:7.0-jre8

# Proxy Java
ENV JAVA_OPTS "-Dhttp.proxyHost=proxy -Dhttp.proxyPort=18088"

#Script para arrancar todo lo necesario para tawmonitor (colas activemq, ow_tawgrid y tawmonitor)
COPY start.sh /usr/local/start.sh 
RUN chmod 755 /usr/local/start.sh 

# Instalar herramienta validador gramatica http://confluence.fundacionctic.org/pages/viewpage.action?pageId=27492906
# RUN apt update

#Cliente de MYSQL para comprobar si la base de datos esta ya lista
# RUN apt install -y mysql-client 

ENTRYPOINT ["/usr/local/start.sh"]



