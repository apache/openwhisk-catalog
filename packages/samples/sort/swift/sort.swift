// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

/**
 * Sample code using the experimental Swift 3 runtime
 * Sort a set of lines.
 * @param lines An array of strings to sort.
 */
func main(args: [String:Any]) -> [String:Any] {
    if let lines = args["lines"] as? [Any] {
        let sorted = lines.sorted { (arg1, arg2) -> Bool in
            String(describing: arg1) < String(describing: arg2)
        }
        return ["lines": sorted, "length": lines.count]
    } else {
        return ["error": "You must specify a lines parameter!"]
    }
}
