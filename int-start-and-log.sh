#!/bin/bash
/usr/local/tomcat/bin/startup.sh
tail -f -n 200 /usr/local/tomcat/logs/catalina.out 
