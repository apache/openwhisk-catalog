#!/bin/bash
#
# use the command line interface to install standard actions deployed
# automatically.
#
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH must be set and non-empty"}
AUTH_KEY=$WHISK_SYSTEM_AUTH

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
CATALOG_HOME=$SCRIPTDIR
source "$CATALOG_HOME/util.sh"

echo Installing whisk.system entities.

createPackage system -a description "Low-level OpenWhisk utilities"
createPackage utils -a description "Building blocks that format and assemble data"
createPackage samples -a description "A suite of simple actions to help you get started with OpenWhisk"

waitForAll

install "$CATALOG_HOME/utils/echo.js" \
    utils/echo \
    -a description 'Returns the input' -a parameters '[{"name": "payload", "required":false, "description": "Any JSON entity"}]' \
    -a sampleInput '{ "payload": "Five fuzzy felines"}' \
    -a sampleOutput '{ "payload": "Five fuzzy felines"}'

install "$CATALOG_HOME/utils/cat.js" \
     utils/cat \
     -a description 'Concatenates input into a string' \
     -a parameters '[ { "name": "lines", "required": true, "type": "array", "description": "An array of strings or numbers" } ]' \
     -a sampleInput '{ "lines": [4, 2, 3] }' \
     -a sampleOutput '{ "lines": [4, 2, 3] }'

install "$CATALOG_HOME/utils/split.js" \
     utils/split \
     -a description 'Split a string into an array' \
     -a parameters '[{"name": "payload", "required":true, "description":"A string"}], { "name": "separator", "required": false, "description": "The character, or the regular expression, to use for splitting the string }]' \
     -a sampleInput '{ "payload": "one,two,three" "separator": "," }' \
     -a sampleOutput '{ "lines": [one, two, three], "payload": "one,two,three"}'

install "$CATALOG_HOME/utils/sort.js" \
     utils/sort \
     -a description 'Sorts an array' \
     -a parameters '[ { "name": "lines", "required": true, "type": "array", "description": "An array of strings" } ]' \
     -a sampleInput '{ "lines": [4, 2, 3] }' \
     -a sampleOutput '{ "lines": [2, 3, 4], "length": 3 }'

install "$CATALOG_HOME/utils/head.js" \
     utils/head \
     -a description 'Extract prefix of an array' \
     -a parameters '[ { "name": "lines", "required": true, "type": "array", "description": "An array of strings" }, { "name": "num", "required": false, "type": "integer", "description": "The length of the prefix" }]' \
     -a sampleInput '{ "lines": [4, 2, 3], "num": 2 }' \
     -a sampleOutput '{ "lines": [4, 2], "num": 2 }'

install "$CATALOG_HOME/utils/date.js" \
     utils/date \
     -a description 'Current date and time' \
     -a sampleOutput '{ "date": "2016-03-22T00:59:55.961Z" }'

waitForAll

echo whisk.system entities ERRORS = $ERRORS
exit $ERRORS
