#!/bin/bash
#
# use the command line interface to uninstall standard actions deployed
# automatically
#
WHISK_SYSTEM_AUTH_FILE=$1
: ${WHISK_SYSTEM_AUTH_FILE:?"WHISK_SYSTEM_AUTH_FILE must be set and non-empty"}

export WHISK_SYSTEM_AUTH=`cat $WHISK_SYSTEM_AUTH_FILE`

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
OPENWHISK_HOME=${2:-$OPENWHISK_HOME}
: ${OPENWHISK_HOME:="$SCRIPTDIR/../../openwhisk"}
export "OPENWHISK_HOME"

source "$SCRIPTDIR/util.sh"

echo Uninstalling open catalog

runPackageUninstallScript "$SCRIPTDIR" uninstallSlack.sh

waitForAll

echo open catalog ERRORS = $ERRORS
exit $ERRORS
