// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

var watson = require('watson-developer-cloud');

function isValidEncoding(encoding) {
  return encoding === 'ascii' ||
    encoding === 'utf8' ||
    encoding === 'utf16le' ||
    encoding === 'ucs2' ||
    encoding === 'base64' ||
    encoding === 'binary' ||
    encoding === 'hex';
}

/**
 * Synthesizes text to spoken audio.
 * See https://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/text-to-speech/api/v1/
 *
 * @param voice The voice to be used for synthesis. Example: en-US_MichaelVoice
 * @param accept The requested MIME type of the audio. Example: audio/wav
 * @param payload The text to synthesized. Required.
 * @param encoding The encoding of the speech binary data. Defaults to base64.
 * @param username The Watson service username.
 * @param password The Watson service password.
 *
 * @return {
 *  "payload": "<encoded speech file>",
 *  "encoding": "<encoding of payload>",
 *  "content_type": "<content_type of payload>"
 * }
 */
function main(params) {
  var voice = params.voice;
  var accept = params.accept;
  var payload = params.payload;
  var encoding = isValidEncoding(params.encoding) ? params.encoding : 'base64';
  var username = params.username;
  var password = params.password;

  console.log('params:', params);

  var textToSpeech = watson.text_to_speech({
    username: username,
    password: password,
    version: 'v1'
  });

  var promise = new Promise(function(resolve, reject) {
    textToSpeech.synthesize({
      voice: voice,
      accept: accept,
      text: payload,
    }, function (err, res) {
      if (err) {
        reject(err);
      } else {
        resolve({
          payload: res.toString(encoding),
          encoding: encoding,
          mimetype: accept
        });
      }
    }, function (err) {
      reject(err);
    });
  });

  return promise;
}

