#!/bin/bash
LIBPATH=""
PROJECTDIR=$(pwd)
SRCDIR=$PROJECTDIR"/src"
SRCLUCENEDIR=$PROJECTDIR"/src/com/lucene"
SRCSERVERDIR=$PROJECTDIR"/src/com/httpserver/core"
BINDIR=$PROJECTDIR"/bin"
RUNPATH="com.httpserver.core.TestServer"

function GenerateLibrariPath {
	local DIR=$PROJECTDIR"/JarFiles/"
	local JXL=$DIR"jxl.jar"
	local LUCENEANALYZER=$DIR"lucene-analyzers-3.6.2.jar"
	local POI=$DIR"poi-3.9.jar"
	local SWINGX=$DIR"swingx-all-1.6.4.jar"
	local LUCENECORE=$DIR"lucene-core-3.6.2.jar"
	local COMMONIO=$DIR"commons-io-2.4.jar"
	local JAVAFX=$DIR"javafx-ui-swing.jar"
	local JSONSIMPLE=$DIR"json-simple-1.1.jar"
	LIBPATH=.:$JXL:$LUCENEANALYZER:$POI:$SWINGX:$LUCENECORE:$COMMONIO:$JAVAFX:$JSONSIMPLE:$SRCSERVERDIR:$SRCLUCENEDIR:$SRCDIR:$BINDIR
}
GenerateLibrariPath
echo "LibraryPath: "$LIBPATH

cd $SRCDIR
#echo "SRC PWD: "$(pwd)
javac -d $BINDIR -cp $LIBPATH *.java

cd $PROJECTDIR
#echo "BIN PWD: "$(pwd)
java -cp $LIBPATH $RUNPATH $PROJECTDIR