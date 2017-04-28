#!/bin/bash

SCRIPTDIR=$(cd $(dirname "$0") && pwd)
HOMEDIR="$SCRIPTDIR/../../../"

# jshint support
sudo apt-get -y install nodejs npm
sudo npm install -g jshint

# OpenWhisk stuff
cd $HOMEDIR
git clone https://github.com/openwhisk/openwhisk.git

cd openwhisk
./tools/travis/setup.sh
