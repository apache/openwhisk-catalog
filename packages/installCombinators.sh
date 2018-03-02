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
# automatically.
#
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH must be set and non-empty"}
AUTH_KEY=$WHISK_SYSTEM_AUTH

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
PACKAGE_HOME=$SCRIPTDIR
source "$PACKAGE_HOME/util.sh"

echo Installing action combinator package.
PKGNAME=combinators

createPackage "$PKGNAME" -a description "Actions combinators for richer composition"

waitForAll

install "$PACKAGE_HOME/$PKGNAME/eca.js" \
     "$PKGNAME/eca" \
     -a description 'Event-Condition-Action: run condition action and if the result is successful run next action.' \
     -a parameters '[
         { "name": "$conditionName", "required": true, "type": "string", "description": "Name of action to run to compute condition. Must return error to indicate false predicate." },
         { "name": "$actionName",    "required": true, "type": "string", "description": "Name of action to run if result of condition action is not error." } ]' \
     -a sampleInput '{ "$conditionName": "utils/echo", "$actionName": "utils/echo", "error": true }' \
     -a sampleOutput '{ "$eca": "Condition was false" }'

install "$PACKAGE_HOME/$PKGNAME/forwarder.js" \
     "$PKGNAME/forwarder" \
     -a description 'Forward parameters around another action.' \
     -a parameters '[
          { "name": "$actionName", "required": true, "type": "string", "description": "Name of action to run to compute condition. Must return error to indicate false predicate." },
          { "name": "$actionArgs", "required": true, "type": "array",  "description": "Array of parameters names from input arguments to pass to action." },
          { "name": "$forward",    "required": true, "type": "array",  "description": "Array of parameters names from input arguments to merge with result of action." } ]' \
     -a sampleInput '{ "$actionName": "utils/echo", "$actionArgs": [ "x" ], "$forward": [ "y" ], "x": true, "y": true }' \
     -a sampleOutput '{ "x": true, "y": true }'

install "$PACKAGE_HOME/$PKGNAME/retry.js" \
     "$PKGNAME/retry" \
     -a description 'Retry invoking an action until success or attempt count is exhausted, which ever comes first.' \
     -a parameters '[
         { "name": "$actionName", "required": true, "type": "string", "description": "Name of action to run." },
         { "name": "$attempts",   "required": true, "type": "integer", "description": "Name of attempts, must be >= 1." } ]' \
     -a sampleInput '{ "$actionName": "utils/echo", "error": true }' \
     -a sampleOutput '{ "error": "Invocation failed. No retries left." }'

install "$PACKAGE_HOME/$PKGNAME/trycatch.js" \
     "$PKGNAME/trycatch" \
     -a description 'Wraps an action with a try-catch. If the action fails, invokes a second action to handle the error.' \
     -a parameters '[
          { "name": "$tryName",   "required": true, "type": "string", "description": "Name of action to wrap with a try." },
          { "name": "$catchName", "required": true, "type": "string", "description": "Name of action to run if there is a try error." } ]' \
     -a sampleInput '{ "$tryName": "utils/echo", "$catchName": "utils/echo", "error": true }' \
     -a sampleOutput '{ "error": true }'

waitForAll

echo combinator package ERRORS = $ERRORS
exit $ERRORS
