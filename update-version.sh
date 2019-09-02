#!/bin/bash
cd oaw 
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:set -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml 
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:update-property -Dproperty=oaw.version -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml 
cd ..
cd common 
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:set -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:update-property -Dproperty=oaw.version -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml 
cd ..
cd intavcore
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:set -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:update-property -Dproperty=oaw.version -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml 
cd ..
cd crawler
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:set -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:update-property -Dproperty=oaw.version -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml 
cd ..
cd portal
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:set -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml
/home/alvaro/Development/Programs/apache-maven-3.3.9/bin/mvn versions:update-property -Dproperty=oaw.version -DnewVersion=$1 --settings /home/alvaro/Development/maven/.m2/settings.xml 
cd ..


