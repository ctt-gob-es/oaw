"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = _default;
exports.messages = exports.ruleName = void 0;

var _stylelint = require("stylelint");

var _utils = require("../../utils");

var _lodash = _interopRequireDefault(require("lodash"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

var ruleName = (0, _utils.namespace)("at-rule-conditional-no-parentheses");
exports.ruleName = ruleName;

var messages = _stylelint.utils.ruleMessages(ruleName, {
  rejected: "Unexpected () used to surround statements for @-rules"
}); // postcss picks up else-if as else.


exports.messages = messages;
var conditional_rules = ["if", "while", "else"];

function report(atrule, result) {
  _stylelint.utils.report({
    message: messages.rejected,
    node: atrule,
    result: result,
    ruleName: ruleName
  });
}

function fix(atrule) {
  var regex = /(if)? ?\((.*)\)/; // 2 regex groups: 'if ' and cond.

  var groups = atrule.params.match(regex).slice(1);
  atrule.params = _lodash["default"].uniq(groups).join(" ");
}

function _default(primary, _unused, context) {
  return function (root, result) {
    var validOptions = _stylelint.utils.validateOptions(result, ruleName, {
      actual: primary
    });

    if (!validOptions) {
      return;
    }

    root.walkAtRules(function (atrule) {
      // Check if this is a conditional rule.
      if (!_lodash["default"].includes(conditional_rules, atrule.name)) {
        return;
      } // Else uses a different regex
      // params are of format "`if (cond)` or `if cond`
      // instead of `(cond)` or `cond`"


      if (atrule.name === "else") {
        if (atrule.params.match(/ ?if ?\(.*\) ?$/)) {
          if (context.fix) {
            fix(atrule);
          } else {
            report(atrule, result);
          }
        }
      } else {
        if (atrule.params.match(/ ?\(.*\) ?$/)) {
          if (context.fix) {
            fix(atrule);
          } else {
            report(atrule, result);
          }
        }
      }
    });
  };
}