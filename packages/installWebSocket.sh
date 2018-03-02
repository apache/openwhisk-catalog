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
# use the command line interface to install websocket package.
#
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH must be set and non-empty"}
AUTH_KEY=$WHISK_SYSTEM_AUTH

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
PACKAGE_HOME=$SCRIPTDIR
source "$PACKAGE_HOME/util.sh"

echo Installing WebSocket package.

createPackage websocket \
    -a description "Utilities for communicating with WebSockets" \
    -a parameters '[ {"name":"uri", "required":true, "bindTime":true} ]'

waitForAll

install "$PACKAGE_HOME/websocket/sendWebSocketMessageAction.js" \
    websocket/send \
    -a description 'Send a message to a WebSocket' \
    -a parameters '[
      {
        "name": "uri",
        "required": true,
        "description": "The URI of the websocket server."
      },
      {
        "name": "payload",
        "required": true,
        "description": "The data you wish to send to the websocket server."
      }
    ]' \
    -a sampleInput '{"uri":"ws://MyAwesomeService.com/sweet/websocket", "payload":"Hi there, WebSocket!"}' \
    -a sampleOutput '{"result":{"payload":"Hi there, WebSocket!"},"status":"success","success":true}'

waitForAll

echo WebSocket package ERRORS = $ERRORS
exit $ERRORS
