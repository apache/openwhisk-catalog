// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

// Augments an action with the ability to "forward parameters around".
// Example invocation:
//   $ wsk -v action invoke -br dfwd \
//         -p '$actionName' test \
//         -p '$actionArgs' '[ "x" ]' \
//         -p '$forward' '[ "y" ]' \
//         -p x 11 -p y 42

var openwhisk = require('openwhisk');

function main (args) {
  const wsk = openwhisk({ignore_certs: args.$ignore_certs || false});

  delete args.$ignore_certs;
  const actionName = args.$actionName;
  const actionArgs = args.$actionArgs;
  const toForward = args.$forward;

  if (typeof actionName !== 'string') {
    return { error: "Expected an argument '$actionName' of type 'string'." };
  }

  if (!Array.isArray(actionArgs)) {
    return { error: "Expected an array argument '$actionArgs'." };
  }

  if (!Array.isArray(toForward)) {
    return { error: "Expected an array argument '$toFoward'." };
  }

  let subArgs = {};
  for (const k of actionArgs) {
    subArgs[k] = args[k];
  }

  let result = {};
  for (const k of toForward) {
    result[k] = args[k];
  }

  return wsk.actions
  .invoke({
          actionName: actionName,
          params: subArgs,
          blocking: true
      })
  .then(activation => {
          for (let k in activation.response.result) {
              result[k] = activation.response.result[k];
          }
          return result;
      })
  .catch(error => {
          console.log(error);
          return { error: `There was a problem invoking ${actionName}` };
      });
}
