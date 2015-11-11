#!/bin/sh
export MINIFY=./thirdparty/yuicompressor-2.4.7/build/yuicompressor-2.4.7.jar;
export SRC=./JavaScript/src;
export DEST=./JavaScript/minified;

java -jar $MINIFY $SRC/progress.js -o $DEST/progress.js
java -jar $MINIFY $SRC/ProgressCircle.js -o $DEST/ProgressCircle.js
java -jar $MINIFY $SRC/xFormSelection.js -o $DEST/xFormSelection.js

#! CSS
java -jar $MINIFY ./CSS/src/main.css -o ./CSS/minified/main.css
