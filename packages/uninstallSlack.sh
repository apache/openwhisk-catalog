#!/bin/bash
#
# use the command line interface to install Slack package.
#
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH must be set and non-empty"}
AUTH_KEY=$WHISK_SYSTEM_AUTH

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
PACKAGE_HOME=$SCRIPTDIR
source "$PACKAGE_HOME/util.sh"

echo Installing Slack package.

uninstall slack/post

waitForAll

deletePackage slack

waitForAll

echo Slack package ERRORS = $ERRORS
exit $ERRORS
