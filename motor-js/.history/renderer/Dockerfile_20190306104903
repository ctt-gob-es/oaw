FROM alpine:3.8

#Instalar Node
RUN apk add --no-cache nodejs-current nodejs-npm
  
#Instalar Chromium  
RUN apk add --no-cache \
    udev \
    ttf-freefont \
    chromium

# Dependencias necesarias
RUN npm install express --save
RUN npm install compression --save
RUN npm install chrome-remote-interface --save
RUN npm install he --save
RUN npm install valid-url --save

#Copiamos el prerrender
COPY prerender /opt/prerender

#Dependencias necesarias
RUN ls -l /opt/prerender && cd /opt/prerender && npm install --save

#Usuario node para ejecutar el renderer
RUN addgroup -S node && adduser -S -G node node
USER node
