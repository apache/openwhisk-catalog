/**
 * Return a hello message as an array of strings. This demonstrates the use of returning
 * a Promise for asynchronous actions.
 *
 * @param name A person's name.
 * @param place Where the person is from.
 */
function main(params) {
    return whisk.invoke({
        name: '/whisk.system/samples/greeting',
        parameters: {
            name: params.name,
            place: params.place
        },
        blocking: true
    }).then(function (activation) {
        console.log('activation:', activation);
        var payload = activation.result.payload.toString();
        var lines = payload.split(' ');
        return { lines: lines };
    });
}
