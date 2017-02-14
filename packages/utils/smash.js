/**
 * Nests all properties under given property.
 *
 * @param { a: 1, b: 2, c: { d: 1, e: 2 }, '$fieldName': 'p' }
 * @returns { p: { a: 1, b: 2, c: { d: 1, e: 2 } } }
 */
function main(args) {
    let parent = args['$fieldName']

    if (typeof parent !== 'string') {
        return {
            error : "Expected an argument '$fieldName' of type 'string'."
        }
    }

    delete args['$fieldName']

    let result = {}
    result[parent] = args
    return result
}
