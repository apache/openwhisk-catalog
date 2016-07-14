#!/bin/bash
#
# use the command line interface to install standard actions deployed
# automatically
#
: ${OPENWHISK_HOME:?"OPENWHISK_HOME must be set and non-empty"}

# The first argument is the authentication key, which can be passed in teams of either
# a file or the key itself. 
AUTH_KEY=${1:-"$OPENWHISK_HOME/ansible/files/auth.whisk.system"}

# If the auth key file exists, read the key from the file. Otherwise, take the
# first argument as the key itself.
if [ -f "$AUTH_KEY" ]; then
    AUTH_KEY=`cat $AUTH_KEY`
fi

: ${AUTH_KEY:?"AUTH_KEY must be set and non-empty"}
export WHISK_SYSTEM_AUTH=$AUTH_KEY

# The api host is passed as the second argument. If it is not provided, take the edge
# host from the whisk properties file.
API_HOST=$2
if [ -z "$API_HOST" ]; then
    WHISKPROPS_FILE="$OPENWHISK_HOME/whisk.properties"
    if [ ! -f "$WHISKPROPS_FILE" ]; then
        echo "API_HOST must be set and non-empty."
        exit 1
    fi
    API_HOST=`fgrep edge.host= "$WHISKPROPS_FILE" | cut -d'=' -f2`
fi
: ${API_HOST:?"API_HOST must be set and non-empty"}
export WHISK_API_HOST=$API_HOST

# The api host is passed as the third argument. If it is not provided, take "/whisk.system".
namespace=${3:-"/whisk.system"}

# If the namespace does not start with a forward slash, add it.
if [[ $namespace != "/*" ]]; then
    namespace = "/$namespace"
fi
export WHISK_NAMESPACE=$namespace

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"

source "$SCRIPTDIR/util.sh"

echo Installing open catalog

runPackageInstallScript "$SCRIPTDIR" installSystem.sh
runPackageInstallScript "$SCRIPTDIR" installGit.sh
runPackageInstallScript "$SCRIPTDIR" installSlack.sh
runPackageInstallScript "$SCRIPTDIR" installWatson.sh
runPackageInstallScript "$SCRIPTDIR" installWeather.sh
runPackageInstallScript "$SCRIPTDIR" installWebSocket.sh

waitForAll

echo open catalog ERRORS = $ERRORS
exit $ERRORS
