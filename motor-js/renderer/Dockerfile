FROM alpine:3.12

#Instalar Node
RUN apk add --no-cache nodejs-current nodejs-npm tini

# Installs latest Chromium package.
RUN echo @edge http://nl.alpinelinux.org/alpine/v3.12/community >> /etc/apk/repositories \
    && echo @edge http://nl.alpinelinux.org/alpine/v3.12/main >> /etc/apk/repositories \
    && apk add --no-cache \
    chromium@edge \
    harfbuzz@edge \
    nss@edge \
    freetype@edge \
    ttf-freefont@edge \
    && rm -rf /var/cache/* \
    && mkdir /var/cache/apk

#Copiamos el prerrender
COPY prerender/ /opt/prerender/

# Dependencias necesarias
RUN cd /opt/prerender && npm install --save

#Permisos
RUN chmod -R 777 /opt/prerender/  

#Usuario node para ejecutar el renderer
RUN addgroup -S node && adduser -S -G node node
USER node

# Set tini as entrypoint
ENTRYPOINT ["/sbin/tini", "--"]
