FROM alpine:3.10
#FROM artefactos-ic.scae.redsara.es:6000/alpine:3.10

# Install base packages
RUN apk update 
RUN apk upgrade 
RUN apk add curl wget bash

# Install NodeJS and Ruby
RUN apk add --no-cache nodejs-current nodejs-npm zip

RUN apk --update --no-cache add git openssh-client ruby ruby-ffi zlib-dev autoconf automake gcc make g++ optipng nasm curl python3 xdg-utils

RUN pip3 install awscli
RUN gem install compass --no-ri --no-rdoc
#RUN npm_config_unsafe_perm=true npm install -g grunt-cli sass

RUN echo @edge http://nl.alpinelinux.org/alpine/v3.10/community >> /etc/apk/repositories \
    && echo @edge http://nl.alpinelinux.org/alpine/v3.10/main >> /etc/apk/repositories \
    && apk add --no-cache \
    # chromium@edge \
    # harfbuzz@edge \
    # nss@edge \
    # freetype@edge \
    # ttf-freefont@edge \
    && rm -rf /var/cache/* \
    && mkdir /var/cache/apk

# Copy app files and set working dir
COPY . /opt/wcag-em-tool
WORKDIR /opt/wcag-em-tool

# Install dependencies
RUN npm install -g bower 
RUN npm install -g forever 
RUN npm install
RUN bower install --allow-root

#Usuario node para ejecutar el renderer
# RUN addgroup -S node && adduser -S -G node node
# USER node
