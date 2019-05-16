FROM alpine:3.9

#Instalar Node
RUN apk add --no-cache nodejs-current nodejs-npm
  
#Instalar Chromium  
RUN apk add --no-cache \
    udev \
    ttf-freefont \
    chromium

#Copiamos el prerrender
COPY prerender/ /opt/prerender/

# Dependencias necesarias
RUN ls -la /opt/prerender && cd /opt/prerender && npm install --save

#Permisos
RUN chmod -R 777 /opt/prerender/

#Usuario node para ejecutar el renderer
RUN addgroup -S node && adduser -S -G node node
USER node
