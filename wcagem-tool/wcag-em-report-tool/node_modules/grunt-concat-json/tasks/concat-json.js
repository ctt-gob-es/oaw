module.exports = function (grunt)
{
	grunt.registerMultiTask("concat-json", "Merge Multiple JSON Files", function ()
	{
		var path = require('path');
		var colors = require("colors");
		var jsonlint = require("jsonlint");
		var stripJsonComments = require("strip-json-comments");

		// prepare options
		var options = this.options(
		{
			replacer: null,
			space: "",
			folderArrayMarker: '[]'
		});

		grunt.verbose.writeflags(options, "Options");

		// Iterate over all src-dest file pairs
		this.files.forEach(function (f)
		{
			try
			{
				// Start with an empty object
				var output = {};

				// Save paths of unnamed arrays to prevent duplicates
				var arrDict = {};

				// Add fragments
				f.src.forEach(function(src)
				{
					// Source file using cwd
					if (f.cwd)
					{
						src = path.join(f.cwd, src);
					}

					// Merge JSON file into object
					if (!grunt.file.exists(src))
					{
						throw "JSON source file \"" + src.red + "\" not found.";
					}
					else
					{
						try
						{
							var json;
							// Read the raw file
							var fileText = grunt.file.read(src);
							
							var ext = src.substring(src.lastIndexOf('.') + 1);
							if(ext == "js")
							{
								var firstChar = fileText[0];
								//detect object literals
								if(firstChar == '{' || firstChar == '[' || firstChar == '\"' ||
									firstChar.match(/[0-9]/) || fileText == "false" ||
									fileText == "true")
								{
									eval("json = " + fileText + ";");//jshint ignore:line
								}
								//otherwise make a function out of the text and use the
								//return value
								else
								{
									json = eval("(function(){" + fileText + "})();");//jshint ignore:line
								}
							}
							else
							{
								// Strip the file of the comments
								var withoutComments = stripJsonComments(fileText);

								// Lint the comment-free file.
								// If linting errors, terminal will let you know!
								json = jsonlint.parse(withoutComments);
							}
							
							// Fix slashes for windows
							src = src.replace(/\\/g, '/');

							// Remove the path to the contianer,
							// and the .json extension
							var cwd = f.base || f.cwd; // backward support
							var target = src.replace(cwd + '/', '')
								.replace('.json', '')
								.replace('.js', '')
								.split('/');

							var key, child = output;
							while(target.length)
							{
								key = target.shift();

								if (target.length > 0)
								{
									if (!child[key])
									{
										child[key] = {};
									}
									child = child[key];
								}
								else
								{
									child[key] = json; // add linted JSON
								}
							}
						}
						catch (e)
						{
							grunt.fail.warn(e);
						}
					}
				});

				/**
				 * Search the JSON object for the folders (now object keys) that
				 * were marked to be arrays, convert the values to array items.
				 * Process removes the folderArrayMarker from the final JSON file,
				 * so you can access keys in code without the special symbol.
				 * i.e. if folderArrayMarker is the default '[]' then
				 * the "directory[]": key becomes "directory"
				 *
				 * Reminder, if an folder-array is nested in a folder-array, only
				 * the top level folder-array will get a name change, as the children
				 * arrays will become nameless array index items.
				 *
				 * @method  convertCollections
				 * @param {Object} json
				 */
				var convertCollections = function(json)
				{
					if (typeof json !== 'object')
					{
						return false;
					}
					for (var key in json)
					{
						var contents = json[key];

						// Send contents through recursively before doing
						// the contents copy.
						// The process removes all but the top level key.
						convertCollections(contents);

						// Check all keys for the folderArrayMarker
						var indexOfMarker = key.indexOf(options.folderArrayMarker);
						if (indexOfMarker > -1)
						{
							// Push the values one by one of the original key
							// into a new fresh array who's key has the
							// folderArrayMarker removed from it
							var keyWithoutMarker = key.slice(0, indexOfMarker);
							json[keyWithoutMarker] = [];

							for (var k in contents)
							{
								json[keyWithoutMarker].push(contents[k]);
							}
							// Delete the original marker key.
							delete json[key];
						}
					}
				};

				// Fixed folders marked as arrays
				convertCollections(output);

				// Write object as new JSON
				grunt.log.debug("Writing JSON destination file \"" + f.dest.green + "\"");

				grunt.file.write(
					f.dest,
					JSON.stringify(
						output,
						options.replacer,
						options.space
					)
				);

				grunt.log.writeln("File \"" + f.dest.green + "\" created.");
			}
			catch (e)
			{
				grunt.fail.warn(e);
			}
		});
	});
};