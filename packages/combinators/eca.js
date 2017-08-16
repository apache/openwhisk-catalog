// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

// Runs a boolean check on the input arguments (an action),
// if true (any non-error value), runs an action, else, returns {}.
// Example invocation:
//   $ wsk action invoke -br eca \
//          -p '$conditionName' check \
//          -p '$actionName' test \
//          -p x 12 -p password 'ok'

var openwhisk = require('openwhisk');

function main (args) {
  const wsk = openwhisk({ignore_certs: args.$ignore_certs || false});

  const conditionName = args.$conditionName;
  const actionName = args.$actionName;

  delete args.$ignore_certs;
  delete args.$conditionName;
  delete args.$actionName;

  if (typeof conditionName !== 'string') {
    return { error: "Expected an argument '$conditionName' of type 'string'." };
  }

  if (typeof actionName !== 'string') {
    return { error: "Expected an argument '$actionName' of type 'string'." };
  }

  return wsk.actions
  .invoke({
          actionName: conditionName,
          params: args,
          blocking: true
      })
  .then(() => {
          return wsk.actions
          .invoke({
                  actionName: actionName,
                  params: args,
                  blocking: true
              })
          .then(activation => activation.response.result)
          .catch(error => {
                  console.log(error);
                  return { error: `There was a problem invoking ${actionName}` };
              });
      })
  .catch(error => ({ '$eca': 'Condition was false.' }));
}
