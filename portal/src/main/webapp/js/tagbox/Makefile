NPM_BIN = node_modules/.bin
LESS_CMD = $(NPM_BIN)/lessc
MINIFY_CSS = $(NPM_BIN)/cleancss
AUTOPREFIX = $(NPM_BIN)/autoprefixer

all: clean build

.DEFAULT: all
.PHONY: clean distclean
.INTERMEDIATE: tagbox.css.unprefixed

clean:
	rm -f tagbox*js tagbox*css tagbox.css.unprefixed

distclean: clean
	rm -rf lib/compiler.jar lib/jquery-1.8-extern.js node_modules

build: \
	tagbox.min.js \
	tagbox.min.css

node_modules:
	npm install

tagbox.js: \
	js/_begin.js \
	js/fuzzyMatch.js \
	js/DropdownRow.js \
	js/CompletionDropdown.js \
	js/Token.js \
	js/TagBox.js \
	js/fn.tagbox.js \
	js/_end.js

tagbox.css: \
	tagbox.css.unprefixed \
	node_modules

	$(AUTOPREFIX) < $< > $@

tagbox.css.unprefixed: \
	less/tagbox.less \
	node_modules

	$(LESS_CMD) $< > $@


lib/compiler.jar:
	wget -O- http://dl.google.com/closure-compiler/compiler-latest.tar.gz | tar -xz -C lib compiler.jar

lib/jquery-1.8-extern.js:
	wget -O $@ http://closure-compiler.googlecode.com/svn/trunk/contrib/externs/jquery-1.8.js

tagbox.min.js: \
	tagbox.js \
	lib/compiler.jar \
	lib/jquery-1.8-extern.js \

	java -jar lib/compiler.jar --compilation_level ADVANCED_OPTIMIZATIONS --externs lib/jquery-1.8-extern.js < $< > $@

tagbox.js:
	@rm -f $@
	cat $^ > $@

tagbox.min.css: \
	tagbox.css \
	node_modules

	$(MINIFY_CSS) $< > $@
