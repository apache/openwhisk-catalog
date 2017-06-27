#!/bin/bash
#
# use the command line interface to install standard actions deployed
# automatically
#
: ${OPENWHISK_HOME:?"OPENWHISK_HOME must be set and non-empty"}

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
OPENWHISK_HOME=${OPENWHISK_HOME:-$SCRIPTDIR/../../openwhisk}

source "$SCRIPTDIR/validateParameter.sh" $1 $2 $3 $4 $5
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH is not configured. Please input the correct parameter: CATALOG_AUTH_KEY"}
: ${WHISK_API_HOST:?"WHISK_API_HOST is not configured. Please input the correct parameter: API_HOST"}
: ${WHISK_CLI_PATH:?"WHISK_CLI_PATH is not configured. Please input the correct parameter: cli_path"}
: ${WHISK_HOST_NAME:?"WHISK_HOST_NAME is not configured. Please input the correct parameter: whisk_host_name"}
source "$SCRIPTDIR/util.sh"

echo Installing OpenWhisk packages

runPackageInstallScript "$SCRIPTDIR" installCombinators.sh
runPackageInstallScript "$SCRIPTDIR" installGit.sh
runPackageInstallScript "$SCRIPTDIR" installSlack.sh
runPackageInstallScript "$SCRIPTDIR" installSystem.sh
runPackageInstallScript "$SCRIPTDIR" installWatson.sh
runPackageInstallScript "$SCRIPTDIR" installWeather.sh
runPackageInstallScript "$SCRIPTDIR" installWebSocket.sh

waitForAll

echo open catalog ERRORS = $ERRORS
exit $ERRORS
