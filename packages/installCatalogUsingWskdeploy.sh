#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# use the command line interface to install standard actions deployed
# automatically
#

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
echo $SCRIPTDIR

# The Whisk Deploy binary path is passed as the argument.
# If it is not provided, use "wskdeploy" as the default value.
# Here, the entire argument list is called as is which enables us
# to provide wskdeploy command line arguments via this shell script.
# e.g. call installCatalogUsingWskdeploy.sh with:
# installCatalogUsingWskdeploy.sh /usr/bin/wskdeploy --apihost <host> --auth <auth> --namespace <namespace>
wskdeploy_path=${@:-"wskdeploy"}
echo $wskdeploy_path

file $wskdeploy_path

# this is equivalent to running wskdeploy with:
# installCatalogUsingWskdeploy.sh /usr/bin/wskdeploy --apihost <host> --auth <auth> --namespace <namespace> -p $SCRIPTDIR/combinators/
$wskdeploy_path -p $SCRIPTDIR/combinators/
$wskdeploy_path -p $SCRIPTDIR/github/
$wskdeploy_path -p $SCRIPTDIR/slack/
$wskdeploy_path -p $SCRIPTDIR/utils/
$wskdeploy_path -p $SCRIPTDIR/watson-speechToText/
$wskdeploy_path -p $SCRIPTDIR/watson-textToSpeech/
$wskdeploy_path -p $SCRIPTDIR/weather/
$wskdeploy_path -p $SCRIPTDIR/websocket/
$wskdeploy_path -p $SCRIPTDIR/samples/

