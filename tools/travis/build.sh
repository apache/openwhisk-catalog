#!/bin/bash
set -e

# Build script for Travis-CI.

SCRIPTDIR=$(cd $(dirname "$0") && pwd)
ROOTDIR="$SCRIPTDIR/../.."
WHISKDIR="$ROOTDIR/../openwhisk"

# Install OpenWhisk

cd $WHISKDIR
tools/build/scanCode.py .

cd $WHISKDIR/ansible

ANSIBLE_CMD="ansible-playbook -i environments/travis"

$ANSIBLE_CMD setup.yml
$ANSIBLE_CMD prereq.yml
$ANSIBLE_CMD couchdb.yml
$ANSIBLE_CMD initdb.yml

cd $WHISKDIR

./gradlew distDocker

cd $WHISKDIR/ansible

$ANSIBLE_CMD openwhisk.yml

# Set Environment
export OPENWHISK_HOME=$WHISKDIR

# Install Catalog

cat $WHISKDIR/whisk.properties
cd $ROOTDIR/packages
./installCatalog.sh $WHISKDIR/config/keys/auth.whisk.system

# Test

cd $ROOTDIR
./gradlew :tests:test
