#!/bin/bash
#
# Test the script of installCatalog.sh.
#
#
SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
echo "This script is about to test another script installCatalog.sh."
bash "$SCRIPTDIR/../packages/installCatalog.sh" "$SCRIPTDIR/../../openwhisk/config/keys/auth.whisk.system"
echo "This script has just run another script installCatalog.sh."
