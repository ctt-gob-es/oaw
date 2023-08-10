#!/usr/bin/env node
var prerender = require('./lib');

var server = prerender({ 
	followRedirects: true,
	chromeLocation: '/usr/bin/chromium-browser', 
	pageDoneCheckInterval: 1000,
	waitAfterLastRequest: 1000,
	pageLoadTimeout: 25000,
	workers: 4,
	enableServiceWorker: true
});

server.use(prerender.sendPrerenderHeader());
//server.use(prerender.blockResources());
server.use(prerender.removeScriptTags());
server.use(prerender.httpHeaders());

server.start();
