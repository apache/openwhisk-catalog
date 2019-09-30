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
# utility functions used when installing standard whisk assets during deployment
#
# Note use of --apihost, this is needed in case of a b/g swap since the router may not be
# updated yet and there may be a breaking change in the API. All tests should go through edge.

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
OPENWHISK_HOME=${OPENWHISK_HOME:-$SCRIPTDIR/../../openwhisk}

: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH must be set and non-empty"}
AUTH_KEY=$WHISK_SYSTEM_AUTH

: ${WHISK_API_HOST:?"WHISK_API_HOST must be set and non-empty"}
EDGE_HOST=$WHISK_API_HOST

function deployProject() {
    RELATIVE_PATH=$1
    REST=("${@:2}")
    CMD_ARRAY=("$WHISK_CLI_PATH" -i --apihost "$EDGE_HOST" project deploy --auth "$AUTH_KEY" --namespace _ --project "$RELATIVE_PATH" "${REST[@]}")
    export WSK_CONFIG_FILE= #override local property file to avoid namespace clashes
    "${CMD_ARRAY[@]}" &
    PID=$!
    PIDS+=($PID)
    echo "Deploying $RELATIVE_PATH with pid $PID"
}

# PIDS is the list of ongoing processes and ERRORS the total number of processes that failed
PIDS=()
ERRORS=0

# Waits for all processes in PIDS and clears it - updating ERRORS for each non-zero status code
function waitForAll() {
    for pid in ${PIDS[@]}; do
        wait $pid
        STATUS=$?
        echo "$pid finished with status $STATUS"
        if [ $STATUS -ne 0 ]
        then
            let ERRORS=ERRORS+1
        fi
    done
    PIDS=()
}
