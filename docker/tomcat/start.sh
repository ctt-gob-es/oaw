#!/bin/bash

# Evitar que el tomcat arranque antes que la base de datos esté inicializada
# 

export TOMCAT_HOME=/usr/local/tomcat
export HOME=/usr/local/tawmonitor

YELLOW='\033[0;33m'
NC='\033[0m' # No Color

#Esperamos a que la base de datos esté levantada

#while ! mysqladmin ping -hmysql -uroot -proot --silent; do
#    echo -e "${YELLOW}++++ 1 Esperando el arranque de la base de datos${NC}"
#    sleep 10
#done

# Arracamos el Tomcat (contiene en el webapps el war ow_tawgrid.war)
echo -e "${YELLOW}++++ Arrancando Tomcat${NC}"
$TOMCAT_HOME/bin/startup.sh
sleep 20

#Esperamos a que arranque
sleep 60
echo -e "${YELLOW}++ Ya puede acceder al OAW {NC}"

tail -f /usr/local/tomcat/logs/catalina.out
