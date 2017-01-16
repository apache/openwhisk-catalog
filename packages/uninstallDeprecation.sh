#!/bin/bash
#
# use the command line interface to uninstall the deprecated actions and packages
#
: ${OPENWHISK_HOME:?"OPENWHISK_HOME must be set and non-empty"}

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
OPENWHISK_HOME=${OPENWHISK_HOME:-$SCRIPTDIR/../../openwhisk}

source "$SCRIPTDIR/validateParameter.sh" $1 $2 $3 $4
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH is not configured. Please input the correctly parameter: CATALOG_AUTH_KEY"}
: ${WHISK_API_HOST:?"WHISK_API_HOST is not configured. Please input the correctly parameter: API_HOST"}
: ${WHISK_NAMESPACE:?"WHISK_NAMESPACE is not configured. Please input the correctly parameter: catalog_namespace"}
: ${WHISK_CLI_PATH:?"WHISK_CLI_PATH is not configured. Please input the correctly parameter: cli_path"}

source "$SCRIPTDIR/util.sh"

echo Uninstalling open catalog deprecated actions and packages

actionArray=("util/cat" "util/date" "util/head" "util/sort" "util/split" "samples/echo" "watson/speechToText" "watson/textToSpeech" "watson/languageId" "watson/translate" "system/pipe")
for i in "${actionArray[@]}"
do
	echo Deleting the deprecated open catalog action $i
	removeAction $i
done

waitForAll

packageArray=("util" "watson" "system")
for i in "${packageArray[@]}"
do
	echo Deleting the deprecated open catalog package $i
	removePackage $i
done

waitForAll

exit
