/**
 * @return the namespace for the key used to invoke this action.
 */
function main() {
    return { namespace: process.env.__OW_NAMESPACE };
}
