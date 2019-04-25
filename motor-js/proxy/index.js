'use strict';
const http = require('http');
const net = require('net');
const request = require('request').defaults({ followRedirect: false, encoding: null });
const url = require('url');
const regex = require("regex");
const port = 18088;
const regexCSS = /\.(s?css)(?:\?.*|)$/i;
const regexFontsGoogle = /fonts.googleapis.com/i;

const server = http.createServer((req, res) => {
  var requestURL = req.url;
  console.log("[proxy - http] : " + requestURL);

  let endpoint = requestURL;

  if (regexCSS.test(requestURL)) {
    console.log("[css] : " + requestURL);
  } else if (regexFontsGoogle.test(requestURL)) {
    console.log("[font] : " + requestURL);
  } else {
    endpoint = 'http://nginx:80/?url=' + requestURL;
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

  console.log("[proxy - https] : " + requestURL);

  let endpoint = "https://" + requestURL;

  if (regexCSS.test(req.url)) {
    console.log("[css] : " + requestURL);
  } else if (regexFontsGoogle.test(requestURL)) {
    console.log("[font] : " + requestURL);
  } else {
    endpoint = 'https://nginx:443/?url=' + requestURL;
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
