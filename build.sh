#!/usr/bin/env bash

SEP=:
if [ "$OSTYPE" = "cygwin" ]; then
    SEP=\;
fi

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "${SCRIPTDIR}"

LIBFOLDER="lib/jar"
BUILDFOLDER="bin"
SRCFOLDER="src"

mkdir -p "${BUILDFOLDER}"

LIBPATH=
for f in "${LIBFOLDER}"/*.jar; do
	LIBPATH="${f}${SEP}${LIBPATH}"
done;
CLASSPATH="${BUILDFOLDER}${SEP}${LIBPATH}${SEP}${CLASSPATH}"

shopt -s globstar
javac -d "${BUILDFOLDER}" -cp "${CLASSPATH}" "${SRCFOLDER}"/**/*.java

