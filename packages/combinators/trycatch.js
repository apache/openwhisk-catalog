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

  const tryName = args.$tryName;
  const catchName = args.$catchName;

  delete args.$ignore_certs;
  delete args.$tryName;
  delete args.$catchName;

  if (typeof tryName !== 'string') {
    return { error: "Expected an argument '$tryName' of type 'string'." };
  }

  if (typeof catchName !== 'string') {
    return { error: "Expected an argument '$catchName' of type 'string'." };
  }

  return wsk.actions
  .invoke({
          actionName: tryName,
          params: args,
          blocking: true
      })
  .then(activation => activation.response.result)
  .catch(error => {
      console.log(`try ${tryName} failed, catching with ${catchName}`);

      var catchArgs;
      try {
          catchArgs = error.error.response.result;
      } catch (e) {
          catchArgs = error;
      }

      return wsk.actions
          .invoke({
                  actionName: catchName,
                  params: catchArgs,
                  blocking: true
              })
          .then(activation => activation.response.result)
          .catch(error => {
                  try {
                      // if the action ran and failed, the result field is guaranteed
                      // to contain an error field causing the overall action to fail
                      // with that error
                      return error.error.response.result;
                  } catch (e) {
                      return {
                          error: {
                              message: `There was a problem invoking ${catchName}.`,
                              cause: error.error
                          }
                      };
                  }
              });
      });
}
