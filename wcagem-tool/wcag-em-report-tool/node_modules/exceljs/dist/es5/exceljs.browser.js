"use strict";

/* eslint-disable import/no-extraneous-dependencies,node/no-unpublished-require */
require('core-js/modules/es.promise');

require('core-js/modules/es.object.assign');

require('core-js/modules/es.object.keys');

require('core-js/modules/es.symbol');

require('core-js/modules/es.symbol.async-iterator');

require('regenerator-runtime/runtime');

var ExcelJS = {
  Workbook: require('./doc/workbook')
}; // Object.assign mono-fill

var Enums = require('./doc/enums');

Object.keys(Enums).forEach(function (key) {
  ExcelJS[key] = Enums[key];
});
module.exports = ExcelJS;
//# sourceMappingURL=exceljs.browser.js.map
