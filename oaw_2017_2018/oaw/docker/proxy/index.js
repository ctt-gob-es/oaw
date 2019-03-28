'use strict';

const http = require('http');
const net = require('net');
const request = require('request').defaults({ followRedirect: false, encoding: null });
const url = require('url');
const regex = require("regex");
const port = 18088;
const regexCSS = /\.(s?css)(?:\?.*|)$/i;
const regexFontsGoogle = /fonts.googleapis.com/i;
const regexW3CValidator = /validator.w3.org/i
const regexImages = /\.(jpg|jpeg|png|gif|bmp|svg)\b/i;
const log4js = require('log4js');
const logger = log4js.getLogger();

//https://github.com/log4js-node/log4js-node

log4js.configure({
  appenders: {
    log: { type: 'file', filename: 'oawproxy.log' },
    console: { type: 'console' }
  },
  categories: {
    default: { appenders: ['console', 'log'], level: 'debug' }
  }
});

const server = http.createServer((req, res) => {
  var requestURL = req.url;

  logger.level = 'info';
  logger.info("URL recibida: " + requestURL + " procesada por HTTP");

  let endpoint = requestURL;

  var directo = 0;
  var tipo = "";

  if (regexCSS.test(requestURL)) {
    directo = 1;
    tipo = "CSS";
  } else if (regexFontsGoogle.test(requestURL)) {
    directo = 1;
    tipo = "FUENTE";
  }
  else if (regexW3CValidator.test(requestURL)) {
    directo = 1;
    tipo = "VALIDADOR W3C";
  }
  else if (regexImages.test(requestURL)) {
    directo = 1;
    tipo = "IMAGEN";
  }
  else {
    endpoint = 'http://nginx:80/?url=' + requestURL;
  }

  if(tipo!=0){
    logger.info("La URL (" + requestURL + ") detectada como recurso "+ tipo +" no se envía al renderizador");
  }

  req.pipe(request(endpoint)).pipe(res).on('error', err => {
    const msg = 'Error al conectarse a la url';
    console.error(msg, err);
    res.status(500).send(msg);
  });
}).listen(port, (err) => {
  if (err) {
    return console.log('Error: ', err)
  }

  console.log(`Servidor en puerto ${port}`)
});


server.on('connect', function (req, socket, head) {
  var requestURL = req.url;

  logger.level = 'info';
  logger.info("URL recibida: " + requestURL + " procesada por HTTPS");

  let endpoint = "https://" + requestURL;

  var directo = 0;
  var tipo = "";

  if (regexCSS.test(requestURL)) {
    directo = 1;
    tipo = "CSS";
  } else if (regexFontsGoogle.test(requestURL)) {
    directo = 1;
    tipo = "FUENTE";
  }
  else if (regexW3CValidator.test(requestURL)) {
    directo = 1;
    tipo = "VALIDADOR W3C";
  }
  else if (regexImages.test(requestURL)) {
    directo = 1;
    tipo = "IMAGEN";
  }
  else {
    endpoint = 'https://nginx:443/?url=' + requestURL;
  }

  if(tipo!=0){
    logger.info("La URL (" + requestURL + ") detectada como recurso " + tipo +" no se envía al renderizador");
  }

  // Conectar al servidor
  const srvUrl = url.parse(endpoint);

  const srvSocket = net.connect(srvUrl.port, srvUrl.hostname, () => {
    socket.write('HTTP/1.1 200 Connection Established\r\n' +
      'Proxy-agent: Node.js-Proxy\r\n' +
      '\r\n');
    srvSocket.write(head);
    srvSocket.pipe(socket);
    socket.pipe(srvSocket).on('error', err => {
      const msg = 'Error al conectarse a la url';
      console.error(msg, err);
      res.status(500).send(msg);
    });;
  });

});
