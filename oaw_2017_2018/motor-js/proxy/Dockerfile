FROM node:9.5.0-alpine

#Instalamos dependencias necesarias
RUN  npm install request --save
RUN  npm install regex --save

#Copiamos el prerrender
COPY index.js /opt/proxy/index.js