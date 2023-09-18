#!/bin/bash
export TOMCAT_HOME=/usr/local/tomcat
export HOME=/usr/local/tawmonitor

YELLOW='\033[0;33m'
NC='\033[0m'

echo -e "${YELLOW}++++ Arrancando Tomcat${NC}"
$TOMCAT_HOME/bin/startup.sh
sleep 20

sleep 60
echo -e "${YELLOW}++ Ya puede acceder al OAW {NC}"

tail -f /usr/local/tomcat/logs/catalina.out
