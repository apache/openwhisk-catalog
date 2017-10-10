#!/bin/bash
#
# use the command line interface to install Deploy package.
#
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH must be set and non-empty"}
AUTH_KEY=$WHISK_SYSTEM_AUTH

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
PACKAGE_HOME=$SCRIPTDIR
source "$PACKAGE_HOME/util.sh"

echo Installing Deploy package.

createPackage deploy \
    -p endpoint "openwhisk.ng.bluemix.net" \
    -a description "Package which contains actions to help with Deploys "

waitForAll
asdf
install "$PACKAGE_HOME/deploy/deploy.js" \
    deploy/wskdeploy \
    -a description 'Creates an action that allows you to run wskdeploy from OpenWhisk' \
    -a parameters '[ {"name":"repo", "required":true, "bindTime":true, "description": "The GitHub repository of the Blueprint"}, {"name":"manifestPath", "required":false, "bindTime":true, "description": "The relative path to the manifest file from the GitHub repo"},{"name":"wskApiHost", "required":false, "description": "The URL of the OpenWhisk api host you want to use"}, {"name":"envData", "required":false, "description": "Blueprint-specific environment data object"} ]' \
    -a sampleInput '{"repo":"github.com/my_blueprint", "manifestPath":"runtimes/swift", "wskApiHost":"openwhisk.stage1.ng.bluemix.net", "envData": "{\"KAFKA_ADMIN_URL\":\"https://my_kafka_service\", \"MESSAGEHUB_USER\":\"MY_MESSAGEHUB_USERNAME\"}"}' \
    --docker "openwhisk/wskdeploy:0.8.9.1"
waitForAll

echo Deploy package ERRORS = $ERRORS
exit $ERRORS
