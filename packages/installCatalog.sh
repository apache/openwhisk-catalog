#!/bin/bash
#
# use the command line interface to install standard actions deployed
# automatically
#
: ${OPENWHISK_HOME:?"OPENWHISK_HOME must be set and non-empty"}

# The first argument is the catalog authentication key, which can be passed via either
# a file or the key itself. 
CATALOG_AUTH_KEY=${1:-"$OPENWHISK_HOME/ansible/files/auth.whisk.system"}

# If the auth key file exists, read the key in the file. Otherwise, take the
# first argument as the key itself.
if [ -f "$CATALOG_AUTH_KEY" ]; then
    CATALOG_AUTH_KEY=`cat $CATALOG_AUTH_KEY`
fi

# Make sure that the catalog_auth_key is not empty.
: ${CATALOG_AUTH_KEY:?"CATALOG_AUTH_KEY must be set and non-empty"}
export WHISK_SYSTEM_AUTH=$CATALOG_AUTH_KEY

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

# Make sure that the api_host is not empty.
: ${API_HOST:?"API_HOST must be set and non-empty"}
export WHISK_API_HOST=$API_HOST

# The api host is passed as the third argument. If it is not provided, take "/whisk.system"
# as the default value.
catalog_namespace=${3:-"/whisk.system"}

# If the catalog_namespace does not start with a forward slash, add it.
if [[ $catalog_namespace != \/* ]] ; then
    catalog_namespace="/$catalog_namespace"
fi

export WHISK_NAMESPACE=$catalog_namespace

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
OPENWHISK_HOME=${OPENWHISK_HOME:-$SCRIPTDIR/../../openwhisk}

# The CLI path is passed as the fourth argument. If it is not provided, use
# "$OPENWHISK_HOME/bin/go-cli/wsk" as the default value.
cli_path=${4:-"$OPENWHISK_HOME/bin/go-cli/wsk"}
export WHISK_CLI_PATH=$cli_path

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
