/**
 * word count utility
 *
 * Return word count as a binary number. This demonstrates the use of a blocking
 * invoke.
 */
function main(params) {
    var str = params.payload;
    var binary = params.binary || false;
    console.log("The payload is '" + str + "', binary is '" + binary + "'");

    if (binary == "false") {
        var words = str.split(" ");
        var count = words.length;
        console.log("The message '"+str+"' has", count, 'words');
        return { count: count };
    } else {
        whisk.invoke({
            name : 'wordCount',
            parameters : {
                payload : str
            },
            blocking : true,
            next : function(error, activation) {
                console.log('activation:', activation);
		if (!error) {
		    var wordsInDecimal = activation.result.count;
		    var wordsInBinary = wordsInDecimal.toString(2) + ' (base 2)';
		    whisk.done({
		        binaryCount : wordsInBinary
		    });
		} else {
		    console.log('error:', error);
		    whisk.error(error);
                }
	    }
	});
	return whisk.async();
    }
}

