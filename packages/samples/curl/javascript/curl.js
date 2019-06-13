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
 * Attempt to Curl to the URL provided in the payload.
 */
function main(msg) {
    var hostToCurl = msg.payload;
    console.log('Curl to ' + hostToCurl);

    var spawn = require('child_process').exec;

    var promise = new Promise(function(resolve, reject) {
        var child = spawn('curl --connect-timeout 3 ' + hostToCurl);

        var tmp = {stdout: "", stderr: "", code: "undefined"};

        child.stdout.on('data', function (data) {
            tmp.stdout = tmp.stdout + data;
        });

        child.stderr.on('data', function (data) {
            tmp.stderr = tmp.stderr + data;
        });

        child.on('close', function (code) {
            tmp.code = code;
            if (tmp.code === 0) {
                console.log(tmp.stdout);
                resolve({msg: tmp.stdout});
            } else {
                console.log(tmp.stderr);
                resolve({msg: tmp.stderr});
            }

        });
    });

    return promise;
}
