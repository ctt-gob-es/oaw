#!/bin/bash
/usr/local/tomcat/bin/shutdown.sh
cd /usr/local/tomcat/webapps/
sudo rm -r oaw
sudo rm oaw.war
