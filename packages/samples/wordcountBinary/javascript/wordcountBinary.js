/**
 * Return word count as a binary number. This demonstrates the use of a blocking
 * invoke.
 */
function main(params) {
    var str = params.payload;
    console.log("The payload is '" + str + "'");

    var promise = new Promise(function(resolve, reject) {
        whisk.invoke({
            name: '/whisk.system/samples/wordCount',
            parameters: {
                payload: str
            },
            blocking: true
        }).then(function (activation) {
            console.log('activation:', activation);
            var wordsInDecimal = activation.result.count;
            var wordsInBinary = wordsInDecimal.toString(2) + ' (base 2)';
            resolve({
                binaryCount: wordsInBinary
            });
        }).catch(function (error) {
            console.log('error:', error);
            reject(error);
        });
    });

    return promise;
}
