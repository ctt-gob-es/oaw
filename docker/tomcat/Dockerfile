FROM tomcat:7.0-jre8

ENV JAVA_OPTS "-Dhttp.proxyHost=proxy -Dhttp.proxyPort=18088"

COPY start.sh /usr/local/start.sh 
RUN chmod 755 /usr/local/start.sh 

ENTRYPOINT ["/usr/local/start.sh"]