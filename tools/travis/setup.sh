#!/bin/bash

SCRIPTDIR=$(cd $(dirname "$0") && pwd)
HOMEDIR="$SCRIPTDIR/../../../"

# Docker stuff
sudo apt-get -y update -qq
sudo apt-get -o Dpkg::Options::="--force-confnew" --force-yes -y install docker-engine=1.9.1-0~trusty
docker --version

# Setup docker
sudo -E bash -c 'echo '\''DOCKER_OPTS="-H tcp://0.0.0.0:4243 -H unix:///var/run/docker.sock --api-enable-cors --storage-driver=aufs"'\'' >> /etc/default/docker'
sudo gpasswd -a travis docker
sudo service docker restart

# Python stuff
sudo apt-get -y install python-pip
pip install --user jsonschema
pip install --user ansible==2.0.2.0

# OpenWhisk stuff

cd $HOMEDIR
git clone https://github.com/openwhisk/openwhisk.git
