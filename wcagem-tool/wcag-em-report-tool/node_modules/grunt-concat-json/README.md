# grunt-concat-json [![Build Status](https://travis-ci.org/SpringRoll/grunt-concat-json.svg)](https://travis-ci.org/SpringRoll/grunt-concat-json) [![Dependency Status](https://david-dm.org/SpringRoll/grunt-concat-json.svg?style=flat)](https://david-dm.org/SpringRoll/grunt-concat-json) [![npm version](https://badge.fury.io/js/grunt-concat-json.svg)](http://badge.fury.io/js/grunt-concat-json)

Grunt Task for Merging Multiple JSON Files

## Getting Started

This plugin requires Grunt `~0.4.5`

If you haven't used [Grunt](http://gruntjs.com/)
before, be sure to check out the [Getting
Started](http://gruntjs.com/getting-started) guide, as it explains how
to create a [Gruntfile](http://gruntjs.com/sample-gruntfile) as well as
install and use Grunt plugins. Once you're familiar with that process,
you may install this plugin with this command:

```shell
npm install grunt-concat-json --save
```

Once the plugin has been installed, it may be enabled inside your
Gruntfile with this line of JavaScript:

```js
grunt.loadNpmTasks('grunt-concat-json');
```

## Task Properties

### src

Type: `String`|`Array`

The path to the source JSON files or collection of individual files. For instance: `"src/**/*.{json,js}"`, which will concatenate all JSON and JS files in the `src` folder.

### dest

Type: `String`

The path to the output concatenated JSON file.

### cwd

Type: `String`
Default: `null`

The root folder to source files from. This will exclude this folder and it's parents from nested layer representation in the output JSON file. If `cwd` is set, then the root folder does not need to be specified as part of the `src`.

## Task Options

### options.folderArrayMarker

Type: `String`
Default: `[]`

The token to use as suffix to a folder name to signify the contents should be rendered as an Array of items and not a Object.

### options.replacer

Type: `function`
Default: `null`

The replacer argument for `JSON.stringify()` (second argument).

### options.space

Type: `String`
Default: `""`

The space argument for `JSON.stringify()` (third argument).

## Merge JSON Task

_Run this task with the `grunt concat-json` command._

Task targets, files and options may be specified according to the Grunt
[Configuring tasks](http://gruntjs.com/configuring-tasks) guide.

## Usage Example

Assuming we have the following types of source JSON files:

`src/foo/foo-en.json`:

```json
{
    "foo": {
        "title": "The Foo",
        "name":  "A wonderful component"
    }
}
```

`src/bar/bar-en.json`:

```json
{
    "bar": {
        "title": "The Bar",
        "name":  "An even more wonderful component"
    }
}
```

Will generate the following destination JSON file:

```json
{
    "foo": {
        "title": "The Foo",
        "name":  "A wonderful component"
    },
    "bar": {
        "title": "The Bar",
        "name":  "An even more wonderful component"
    }
}
```

## Merging of Files and Folders

If a .json file and a folder share the same name, they will be merged into one
object when the JSON is concatenated.
Assuming we have the following source JSON files:

`src/foo.json`:

```json
{
    "default": {
        "title": "The Foo",
        "name":  "A wonderful component"
    }
}
```

`src/foo/bar.json`:

```json
{
    "title": "The Bar",
    "name":  "An even more wonderful component"
}
```

Will generate the following destination JSON file:

```json
{
    "foo": {
        "default": {
            "title": "The Foo",
            "name":  "A wonderful component"
        },
        "bar": {
            "title": "The Bar",
            "name":  "An even more wonderful component"
        }
    }
}
```

## Folder-as-Array Example

The contents of a folder can be grouped together as an array. The folder must
end in a unique symbol, the default is '[]'; For the files

- `src/foo[]/foo1.json`:
- `src/foo[]/foo2.json`:
- `src/foo[]/foo3.json`:

```js
{
    "foo": [
        {
            //contents of foo1.json...
        },
        {
            //contents of foo2.json...
        },
        {
            //contents of foo3.json...
        },
    ]
}
```

## Handling JavaScript files

The javascript file can take two forms - either an object literal, or the contents of a function
where your return value becomes the JSON object for the file.

```js
{
    //if the first character is the first character of an object literal, then it is evaluated that
    //way. This means that if your JSON as JS is set up that way, you can't have whitespace or
    //a comment as the first text
    TWO_PI: Math.PI * 2,
    foo: "bar"
}
```

```js
//other javacript is wrapped within a function, allowing you to create your object however you like
var rtn;
for(var i = 100; i > 50; --i)
    rtn.push(i);
//The return value here is the final result, which saves us from having to make our array
//of integer values form 100 to 51 by hand.
return rtn;
```

Note, that the .json files in an array folder do not retain their file names as keys,
since they are now array index items.


### Single file per target variant

```js
grunt.initConfig({
    "concat-json": {
        "en": {
            src: [ "src/**/*-en.json" ],
            dest: "www/en.json"
        },
        "de": {
            src: [ "src/**/*-de.json" ],
            dest: "www/de.json"
        }
    }
});
```

### Multiple files per target variant

```js
grunt.initConfig({
    "concat-json": {
        "i18n": {
            files: {
                "www/en.json": [ "src/**/*-en.json" ],
                "www/de.json": [ "src/**/*-de.json" ]
            }
        }
    }
});
```
