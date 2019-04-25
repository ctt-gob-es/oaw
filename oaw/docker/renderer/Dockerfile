FROM alpine:edge

#Instalar Node
RUN apk add --no-cache nodejs-current
  
#Instalar Chromium  
RUN apk add --no-cache \
    udev \
    ttf-freefont \
    chromium

#Copiamos el prerrender
COPY prerender.tar.gz /opt/

#Descomprimimos
RUN tar -xf /opt/prerender.tar.gz -C /opt/

#Usuario node para ejecutar el renderer
RUN addgroup -S node && adduser -S -G node node
USER node
