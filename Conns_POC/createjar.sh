#!/bin/sh
FOLDER="${PWD##*/}"
EXPECTED="AmazonPOC"

echo "folder :  $FOLDER"
echo "expected : $EXPECTED"

if [ "$FOLDER" == "$EXPECTED" ]; then
	mvn package
else
	echo 'Jar is not created. Path mismatch'	
	exit 1
fi

echo
echo "================================================"
echo "jar file is created and stored in /target folder"
echo "================================================"
