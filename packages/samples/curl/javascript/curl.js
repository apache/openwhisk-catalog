// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

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
