// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

// Expects an object with three arguments:
//
// - $actionName: name of the action to invoke
// - $attempts: integer, number of times the action should be tried (>= 1)
// - all other arguments are passed to the inner action

var openwhisk = require('openwhisk');

function main (args) {
  const wsk = openwhisk({ignore_certs: args.$ignore_certs || false});

  const actionName = args.$actionName;
  const attempts = args.$attempts;

  delete args.$ignore_certs;
  delete args.$actionName;
  delete args.$attempts;

  if (typeof actionName !== 'string') {
    return { error: `Expected an argument '$actionName' of type 'string', got '${actionName}'.` };
  }

  if (typeof attempts !== 'number' || attempts < 1) {
    return { error: `Expected an argument '$attempts' of type 'number' and greater than zero, got '${attempts}'.` };
  }

  return new Promise(function (resolve, reject) {
          function retry (n) {
              if (n <= 0) {
                  reject('Invocation failed. No retries left.');
              } else {
                  wsk.actions
                  .invoke({
                          actionName: actionName,
                          params: args,
                          blocking: true
                      })
                  .then(activation => resolve(activation.response.result) )
                  .catch(error => {
                          console.log(`attempt ${n} failed, retrying`);
                          retry(n - 1);
                      });
              }
          }

          retry(attempts);
      });
}
