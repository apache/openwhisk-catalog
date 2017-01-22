#!/bin/bash
#
# use the command to validate the input parameters
#

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

# The CLI path is passed as the fourth argument. If it is not provided, use
# "$OPENWHISK_HOME/bin/wsk" as the default value.
cli_path=${4:-"$OPENWHISK_HOME/bin/wsk"}
export WHISK_CLI_PATH=$cli_path
