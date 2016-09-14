var watson = require('watson-developer-cloud');

/**
 * Identify the language of some text.
 *
 * @param payload The text to identify.
 * @param username The watson service username.
 * @param password The watson service password.
 * @return An object with the following properties: {
 *           payload: the original text,
 *           language: the identified language,
 *           confidence: the confidence score
 *         }
 */
function main(params) {
    var payload = params.payload;
    console.log('payload is', payload);
    var language_translation = watson.language_translation({
        username: params.username,
        password: params.password,
        version: 'v2'
    });
    var promise = new Promise(function(resolve, reject) {
        language_translation.identify({text: payload}, function (err, response) {
            if (err) {
                console.log('error:', err);
                reject(err);
            } else {
                var language = response.languages[0].language;
                var confidence = response.languages[0].confidence;
                console.log('language:', language, ', payload:', payload);
                resolve({language: language, payload: payload, confidence: confidence});
            }
        });
    });

    return promise;
}

