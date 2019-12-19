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
 * Sends a payload message to the designated WebSocket URI
 *
 * @param uri       String representation of the WebSocket uri
 * @param payload   Message to send to the WebSocket
 * @return  Standard OpenWhisk success/error response
 */
function main(params) {
    var promise = new Promise(function(resolve, reject) {
    	if (!params.uri) {
            reject('You must specify a uri parameter.');
        }
        var uri = params.uri;

        console.log("URI param is " + params.uri);

        if (!params.payload) {
            reject('You must specify a payload parameter.');
        }
        var payload = params.payload;

        console.log("Payload param is " + params.payload);

        var WebSocket = require('ws');

        var connectionEstablished = false;
        var ws = new WebSocket(uri);

        var connectionTimeout = 30 * 1000; // 30 seconds

        setTimeout(function () {
            if (!connectionEstablished) {
                reject('Did not establish websocket connection to ' + uri + ' in a timely manner.');
            }
        }, connectionTimeout);

        ws.on('open', function () {
            connectionEstablished = true;

            console.log("Sending payload: " + payload);
            ws.send(payload, function (error) {
                if (error) {
                    console.log("Error received communicating with websocket: " + error);
                    ws.close();
                    reject(error);
                } else {
                    console.log("Send was successful.");
                    ws.close();
                    resolve({
                        'payload': payload
                    });
                }
            });
        });

        ws.on('error', function (error) {
            console.log("Error communicating with websocket: " + error);
            ws.close();
            reject(error);
        });
    });

    return promise;
}

exports.main = main;
