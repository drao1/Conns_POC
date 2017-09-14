#!/bin/sh
FOLDER="${PWD##*/}"
EXPECTED="AmazonPOC"

echo "folder :  $FOLDER"
echo "expected : $EXPECTED"

if [ "$FOLDER" == "$EXPECTED" ]; then
	mvn test
else
	echo 'Test cases are not running'	
	exit 1
fi

echo
echo "==============================================================="
echo "Test case run successfully. Please see reports for more details"
echo "==============================================================="
