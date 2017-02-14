#!/bin/bash
set -e

# Build script for Travis-CI.

SCRIPTDIR=$(cd $(dirname "$0") && pwd)
ROOTDIR="$SCRIPTDIR/../.."
WHISKDIR="$ROOTDIR/../openwhisk"

# run scancode
cd $WHISKDIR
tools/build/scanCode.py $ROOTDIR

# run jshint
cd $ROOTDIR/packages
jshint .

# Install OpenWhisk
cd $WHISKDIR/ansible

ANSIBLE_CMD="ansible-playbook -i environments/local"

$ANSIBLE_CMD setup.yml
$ANSIBLE_CMD prereq.yml
$ANSIBLE_CMD couchdb.yml
$ANSIBLE_CMD initdb.yml

cd $WHISKDIR

./gradlew distDocker

cd $WHISKDIR/ansible

$ANSIBLE_CMD wipe.yml
$ANSIBLE_CMD openwhisk.yml

# Set Environment
export OPENWHISK_HOME=$WHISKDIR

# Install Catalog

cat $WHISKDIR/whisk.properties
cd $ROOTDIR/packages
./installCatalog.sh $WHISKDIR/ansible/files/auth.whisk.system

# Set credentials
VCAP_SERVICES_FILE="$(readlink -f $ROOTDIR/tests/credentials.json)"
WHISKPROPS_FILE="$WHISKDIR/whisk.properties"
sed -i 's:^[ \t]*vcap.services.file[ \t]*=\([ \t]*.*\)$:vcap.services.file='$VCAP_SERVICES_FILE':'  $WHISKPROPS_FILE

# Test

cd $ROOTDIR
./gradlew :tests:testWithoutCredentials
