#!/bin/bash
#
# Test the script of installSlack.sh.
#
#
SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
source "$SCRIPTDIR/../packages/util.sh"

WHISK_SYSTEM_AUTH_FILE=$SCRIPTDIR/../../openwhisk/config/keys/auth.whisk.system
: ${WHISK_SYSTEM_AUTH_FILE:?"WHISK_SYSTEM_AUTH_FILE must be set and non-empty"}

export WHISK_SYSTEM_AUTH=`cat $WHISK_SYSTEM_AUTH_FILE`

echo "This script is about to test another script installSlack.sh."
sh ./../packages/installSlack.sh
echo "This script has just run another script installSlack.sh."
