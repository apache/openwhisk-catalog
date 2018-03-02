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
# use the command line interface to install Weather.com package.
#
: ${WHISK_SYSTEM_AUTH:?"WHISK_SYSTEM_AUTH must be set and non-empty"}
AUTH_KEY=$WHISK_SYSTEM_AUTH

SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
PACKAGE_HOME=$SCRIPTDIR
source "$PACKAGE_HOME/util.sh"

echo Installing Weather package.

createPackage weather \
    -p bluemixServiceName "weatherinsights" \
    -a description "Services from the Weather Company Data for IBM Bluemix" \
    -a parameters '[{"name":"username", "required":false,"bindTime":true}, {"name":"password", "required":false, "type":"password","bindTime":true}]'

waitForAll

install "$PACKAGE_HOME/weather/forecast.js" \
    weather/forecast \
    -a description 'IBM Weather Insights 10-day forecast' \
    -a parameters '[ {"name":"latitude", "required":true}, {"name":"longitude", "required":true},{"name":"language", "required":false},{"name":"units", "required":false}, {"name":"timePeriod", "required":false}, {"name":"username", "required":true, "bindTime":true},{"name":"password", "required":true,"type":"password", "bindTime":true}, {"name":"host", "required":false} ]' \
    -a sampleInput '{"latitude":"34.063", "longitude":"-84.217", "username":"XXX","password":"XXX"}' \
    -a sampleOutput '{"forecasts":[ {"dow":"Monday", "min_temp":30, "max_temp":38, "narrative":"Cloudy"} ]}'

waitForAll

echo Weather package ERRORS = $ERRORS
exit $ERRORS
