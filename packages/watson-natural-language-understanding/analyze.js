var NLUv1= require('watson-developer-cloud/natural-language-understanding/v1.js');
/**
 * General Natural Language analysis entrypoint
 *
 * @param text plain text to analyze
 * @param username username for Watson NLU service
 * @param password username for Watson NLU service
 * @param features features as expected by the NLU /analyze API
 *
 * @see https://www.ibm.com/watson/developercloud/natural-language-understanding/api/v1/#versioning
 */
function analyze({text: text,
    username: username,
    password: password,
    features: features}) {

    console.log('text is', text);
    var nlu = new NLUv1({
        username: username,
        password: password,
        version_date: NLUv1.VERSION_DATE_2017_02_27
    });

    var promise = new Promise(function(resolve, reject) {
        nlu.analyze({'text': text,
            'features': features},
            function (err, response) {
                if (err) {
                    console.log('error:', err);
                    reject(err);
                } else {
                    resolve({ 'response' : response});
                }
            });
    });

    return promise;
}

/**
 * Natural Language analysis : analyze for one type of feature
 *
 * @param text plain text to analyze
 * @param username username for Watson NLU service
 * @param password username for Watson NLU service
 * @param limit limit on number of results to return
 *
 * @see https://www.ibm.com/watson/developercloud/natural-language-understanding/api/v1/#versioning
 */
function analyzeOneFeature(
    {text : text,
     feature : feature = "entities",
     username : username,
     password : password,
     limit : limit = 3}) {

    var features = {};
    features[feature] = { "limit" : parseInt(limit) };
    return analyze({text:text,username:username,password:password,
                    features: features
                   }
    );
}

exports.main = analyzeOneFeature;
