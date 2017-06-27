// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

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
