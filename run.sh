#!/bin/bash

if [ "$#" == 0 ]; then
	echo "Usage: ${0} JAVA_CLASS"
	exit 1
fi

SEP=:
if [ "$OSTYPE" = "cygwin" ]; then
    SEP=\;
fi

SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "${SCRIPTDIR}"

# cd to scriptdir as opposed to absolute paths, which may encounter problems on cygwin
LIBFOLDER="lib"
BUILDFOLDER="bin"
JAVA_ARGS=("-Xmx2048m")

LIBPATH=
for f in "${LIBFOLDER}"/*.jar; do
	LIBPATH="${f}${SEP}${LIBPATH}" 
done;
CLASSPATH="${BUILDFOLDER}${SEP}${LIBPATH}${SEP}${CLASSPATH}"

# replace ocpl.Server with $* to take class as argument (along with other arguments)
java -cp "${CLASSPATH}" "${JAVA_ARGS[@]}" "$@"

