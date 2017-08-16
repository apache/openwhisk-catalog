// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

/**
 * Print the parameters to the console, sorted alphabetically by key
 */
function main(params) {
    var sep = '';
    var retn = {};
    var keys = [];

    for (var k in params) {
        if (params.hasOwnProperty(k)) {
            keys.push(k);
        }
    }

    keys.sort();
    for (var i in keys) {
        var key = keys[i];
        var value = params[key];
        console.log(sep + 'params.' + key + ':', value);
        sep = ' ';
        retn[key] = value;
    }

    return {params: retn};
}
