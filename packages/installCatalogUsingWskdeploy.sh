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
: ${OPENWHISK_HOME:?"OPENWHISK_HOME must be set and non-empty"}

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
OPENWHISK_HOME=${OPENWHISK_HOME:-$SCRIPTDIR/../../openwhisk}
SKIP_DEPRECATED_PACKAGES=${SKIP_DEPRECATED_PACKAGES:="false"}

source "$SCRIPTDIR/validateParameter.sh" $1 $2 $3
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH is not configured. Please input the correctly parameter: CATALOG_AUTH_KEY"}
: ${WHISK_API_HOST:?"WHISK_API_HOST is not configured. Please input the correctly parameter: API_HOST"}
: ${WHISK_CLI_PATH:?"WHISK_CLI_PATH is not configured. Please input the correctly parameter: cli_path"}

source "$SCRIPTDIR/util.sh"

echo building OpenWhisk packages

pushd "$SCRIPTDIR/websocket/" && ./build.sh && popd

echo Installing OpenWhisk packages

deployProject "$SCRIPTDIR/github/"

deployProject "$SCRIPTDIR/slack/"

deployProject "$SCRIPTDIR/utils/"

deployProject "$SCRIPTDIR/websocket/"

deployProject "$SCRIPTDIR/samples/"

waitForAll

echo open catalog ERRORS = $ERRORS
exit $ERRORS
