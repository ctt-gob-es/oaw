"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.NonQuotedColumnParser = void 0;
const ColumnFormatter_1 = require("./ColumnFormatter");
const Token_1 = require("../Token");
const { isTokenDelimiter, isTokenRowDelimiter } = Token_1.Token;
class NonQuotedColumnParser {
    constructor(parserOptions) {
        this.parserOptions = parserOptions;
        this.columnFormatter = new ColumnFormatter_1.ColumnFormatter(parserOptions);
    }
    parse(scanner) {
        if (!scanner.hasMoreCharacters) {
            return null;
        }
        const { parserOptions } = this;
        const characters = [];
        let nextToken = scanner.nextCharacterToken;
        for (; nextToken; nextToken = scanner.nextCharacterToken) {
            if (isTokenDelimiter(nextToken, parserOptions) || isTokenRowDelimiter(nextToken)) {
                break;
            }
            characters.push(nextToken.token);
            scanner.advancePastToken(nextToken);
        }
        return this.columnFormatter.format(characters.join(''));
    }
}
exports.NonQuotedColumnParser = NonQuotedColumnParser;
//# sourceMappingURL=NonQuotedColumnParser.js.map