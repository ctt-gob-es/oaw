/*
 * fuzzyMatch String matching algorithm
 * https://github.com/jbt/fuzzyMatch
 *
 * Author: James Taylor <jt@gosquared.com>
 * License: MIT
 */

var fuzzyMatch = (function(){
  /**
   * ## escapeRegex
   *
   * Escapes special characters in a string to use when creating a new RegExp
   *
   * @param {string} term String to escape
   * @return {string} Regex-compatible escaped string
   */
  function escapeRegex(term){
    return term.replace(/[\[\]\{\}\(\)\^\$\.\*\+\|]/g, function(a){
      return '\\' + a;
    });
  }

  // A few heper constants; they don't really do much, but they
  // indicate the corresponding rows and columns in the matrix below.
  var UPPER = 0, LOWER = 1, NUMBER = 2, COMMON_DELIMS = 3, OTHER = 4;

  // Amount by which one character stands out when compared
  // to another character. Row = character in question,
  // col = character to compare to. E.g. uppercase letter
  // stands out with a factor of 240 compared to lowercase letter.
  // These numbers are pretty much plucked out of thin air.
  var relevanceMatrix = [
    [  0,   240,   120,   240,   220],
    [ 20,     0,    20,   120,   120],
    [140,   140,     0,   140,   140],
    [120,   120,   120,     0,   120],
    [120,   120,   120,   160,     0]
  ];

  /**
   * ## charType
   *
   * Categorizes a character as either lowercase, uppercase,
   * digit, strong delimiter, or other.
   *
   * @param {string} c The character to check
   * @return {number} One of the constants defined above
   */
  function charType(c){
    if(/[a-z]/.test(c)) return LOWER;
    if(/[A-Z]/.test(c)) return UPPER;
    if(/[0-9]/.test(c)) return NUMBER;
    if(/[\/\-_\.]/.test(c)) return COMMON_DELIMS;
    return OTHER;
  }

  /**
   * ## compareCharacters
   *
   * Compares a character to the characters before and
   * after it to see how much it stands out. For example
   * The letter B would stand out strongly in aBc
   *
   * @param {string} theChar The character in question
   * @param {string} before The immediately preceding character
   * @param {string} after The immediately following character
   * @return {number} Score according to how much the character stands out
   */
  function compareCharacters(theChar, before, after){

    // Grab the character types of all three characters
    var theType = charType(theChar),
        beforeType = charType(before),
        afterType = charType(after);

    // **MAGIC NUMBER ALERT** 0.4 is a number that makes it work best in my tests
    return relevanceMatrix[theType][beforeType] +
     0.4 * relevanceMatrix[theType][afterType];
  }

  /**
   * ## stripAccents
   *
   * Replaces all accented characters in a string with their
   * unaccented equivalent.
   *
   * @param {string} str The input accented string
   * @return {string} String with accents removed
   */
  var stripAccents = (function(accented, unaccented){
    var matchRegex = new RegExp('[' + accented + ']', 'g'),
        translationTable = {}, i;
        lookup = function(chr){
          return translationTable[chr] || chr;
        };

    for(i = 0; i < accented.length; i += 1){
      translationTable[accented.charAt(i)] = unaccented.charAt(i);
    }

    return function(str){
      return str.replace(matchRegex, lookup);
    };
  })('àáâãäçèéêëìíîïñòóôõöùúûüýÿÀÁÂÃÄÇÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜÝ',
     'aaaaaceeeeiiiinooooouuuuyyAAAAACEEEEIIIINOOOOOUUUUY');

  /**
   * ## bestRank
   *
   * The real meat of this searching algorithm. Provides a score for a
   * given string against a given search term.
   *
   * The `startingFrom` parameter is necessary (rather than just truncating
   * `item` so we can use the initial characters of `item` to provide better
   * context.
   *
   * @param {string} item The string to rank
   * @param {string} term The search term against which to rank it
   * @param {number} startingFrom Ignore the first _n_ characters
   * @return {object} Rank of `item` against `term` with highlights
   */
  function bestRank(item, term, startingFrom){

    // If we've reached the end of our search term, add some extra points for being short
    if(term.length === 0) return startingFrom * 100 / item.length;

    // If we've reached the end of the item but not the term, then fail.
    if(item.length === 0) return -1;

    // Quick sanity check to make sure the remaining item has all the characters we need in order
    if(!item.slice(startingFrom).match(
      new RegExp( ('^.*' + escapeRegex(term.split('').join('~~K~~')) + '.*$').split('~~K~~').join('.*'), 'i' )
    )){
      return -1;
    }

    // Grab the first character that we're going to look at
    var firstSearchChar = term.charAt(0);

    // These variables store our best guess so far, and the character
    // indices to which it applies
    var bestRankSoFar = -1;
    var highlights;

    // Now loop over the item, and test all instances of `firstSearchChar` (case-insensitive)
    for(var i = startingFrom; i < item.length; i += 1){
      if(item.charAt(i).toLowerCase() !== firstSearchChar.toLowerCase()) continue;

      // Find out what the rest of the string scores against the rest of the term
      var subsequentRank = bestRank(item.substr(i), term.slice(1), 1);
      if(subsequentRank == -1) continue;

      // Inverse linear score for the character. Earlier in string = much better
      var characterScore = 400 / Math.max(1, i);

      // If, starting at this character, we have the whole of the search term in order, that's really
      // good. And if the term is really long, make it cubically good (quadratic scores added up)
      if(item.substr(i).toLowerCase().indexOf(term.toLowerCase()) === 0) characterScore += 3 * term.length * term.length;

      // Add on score for how much this character stands out
      characterScore += compareCharacters(
        item.charAt(i),
        i === 0 ? '/' : item.charAt(i - 1),
        i === item.length - 1 ? '/' : item.charAt(i + 1)
      );

      // Add on score from the rest of the string
      characterScore += subsequentRank;

      // If we've managed to better what we have so far, store it away
      if(characterScore > bestRankSoFar){
        bestRankSoFar = characterScore;

        // Save highlighted characters as well
        highlights = [i];
        var subsequentHighlights = subsequentRank.highlights || [];
        for(var j = 0; j < subsequentHighlights.length; j += 1){
          highlights.push(subsequentHighlights[j] + i);
        }
      }
    }

    // Return an object with valueOf so it can be directly compared using < and >
    // but also stores the highlight indices
    return {
      __score: bestRankSoFar,
      valueOf: function(){ return this.__score; },
      highlights: highlights
    };
  }

  /**
   * ## fuzzyScoreStr
   *
   * Actual function to use when matching an item against a term
   * (bestRank should only be used internally)
   *
   * @param {string} item Item to search
   * @param {string} term Term against which to search
   * @return {object} Rank of `item` against `term` with highlights
   */
  function fuzzyScoreStr(item, term){
    return bestRank(stripAccents(item), stripAccents(term), 0);
  }

  /**
   * ## fuzzyScore
   *
   * Matches an object against a given term with particular weights being
   * applied to different properties. If the given item is instead a string,
   * just match it directly against the term.
   *
   * The `relevances` parameter should be an object containing properties
   * with the same names as those on `item` that should be counted. For
   * example, a value of `{ propA: 2, propB: 1 }` would count matches in
   * `propA` twice as highly as matches in `propB`.
   *
   * The returned `highlights` property contains arrays of character indices
   * to highlight in the term, indexed by the same property names
   *
   * @param {object} item Item containing multiple properties to search
   * @param {string} term Term against which to search
   * @param {object} relevances Object congaining key/val pairs as above
   * @return {object} Rank of `item` against `term` with highlights.
   */
  function fuzzyScore(item, term, relevances){

    // If we have a string, just match it directly
    if(typeof item == 'string') return fuzzyScoreStr(item, term);

    // Initialize the return object
    var result = {
      __score: 0,
      valueOf: function(){ return this.__score; },
      highlights: {}
    };

    // Loop through all the specified properties
    for(var i in relevances){
      if(!relevances.hasOwnProperty(i) || !item.hasOwnProperty(i)) continue;

      // Grab the score for that particular property
      var thatScore = fuzzyScoreStr(item[i], term);

      // Add the rank on to the return object
      result.__score += relevances[i] * thatScore;
      result.highlights[i] = thatScore > 0 ? thatScore.highlights : [];
    }

    return result;
  }

  return fuzzyScore;
})();
