#!/usr/bin/env node
var prerender = require('./lib');

var server = prerender({ 
	//followRedirects: true,
	chromeLocation: '/usr/bin/chromium-browser', 
	//pageDoneCheckInterval: 5000
	waitAfterLastRequest: 500
});

server.use(prerender.sendPrerenderHeader());
//server.use(prerender.blockResources());
server.use(prerender.removeScriptTags());
server.use(prerender.httpHeaders());

server.start();
