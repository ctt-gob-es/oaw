#!/usr/bin/env node
var prerender = require('./lib');

var server = prerender({ 
	followRedirects: true,
	chromeLocation: '/usr/bin/chromium-browser', 
	pageDoneCheckInterval: 30000,
	waitAfterLastRequest: 30000,
	pageLoadTimeout: 30000,
	workers: 10,
	enableServiceWorker: true
});

server.use(prerender.sendPrerenderHeader());
//server.use(prerender.blockResources());
server.use(prerender.removeScriptTags());
server.use(prerender.httpHeaders());

server.start();
