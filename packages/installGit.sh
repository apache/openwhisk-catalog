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
# use the command line interface to install Git package.
#
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH must be set and non-empty"}
AUTH_KEY=$WHISK_SYSTEM_AUTH

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
PACKAGE_HOME=$SCRIPTDIR
source "$PACKAGE_HOME/util.sh"

echo Installing Git package.

createPackage github \
    -a description "Package which contains actions and feeds to interact with Github"

waitForAll

install "$PACKAGE_HOME/github/webhook.js" \
    github/webhook \
    -a feed true \
    -a description 'Creates a webhook on GitHub to be notified on selected changes' \
    -a parameters '[ {"name":"username", "required":true, "bindTime":true, "description": "Your GitHub username"}, {"name":"repository", "required":true, "bindTime":true, "description": "The name of a GitHub repository"}, {"name":"accessToken", "required":true, "bindTime":true, "description": "A webhook or personal token", "doclink": "https://github.com/settings/tokens/new"},{"name":"events", "required":true, "description": "A comma-separated list", "doclink": "https://developer.github.com/webhooks/#events"} ]' \
    -a sampleInput '{"username":"myUserName", "repository":"myRepository or myOrganization/myRepository", "accessToken":"123ABCXYZ", "events": "push,delete,pull-request"}'

waitForAll

echo Git package ERRORS = $ERRORS
exit $ERRORS
