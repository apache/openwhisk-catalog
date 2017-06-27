// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

/**
 * @return the namespace for the key used to invoke this action.
 */
function main() {
    return { namespace: process.env.__OW_NAMESPACE };
}
