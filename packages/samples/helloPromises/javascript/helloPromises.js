/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Return a hello message as an array of strings. This demonstrates the use of returning
 * a Promise for asynchronous actions.
 *
 * @param name A person's name.
 * @param place Where the person is from.
 */
var openwhisk = require('openwhisk');

function main(params) {
    var wsk = openwhisk({ignore_certs: params.ignore_certs || false});
    return wsk.actions.invoke({
        actionName: '/whisk.system/samples/greeting',
        params: {
            name: params.name,
            place: params.place
        },
        blocking: true
    }).then(activation => {
        console.log('activation:', activation);
        var payload = activation.response.result.payload.toString();
        var lines = payload.split(' ');
        return { lines: lines };
    });
}
