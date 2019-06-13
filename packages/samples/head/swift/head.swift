/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ the License.

/**
 * Sample code using the experimental Swift 3 runtime
 * Return the first num lines of an array.
 * @param lines An array of strings.
 * @param num Number of lines to return.
 */
func main(args: [String:Any]) -> [String:Any] {
    if let lines = args["lines"] as? [Any] {
        var num: Int?
        if let value = args["num"] as? Int {
            num = value
        } else if let value = args["num"] as? Double {
            num = Int(value)
        } else {
            num = 1
        }
        return ["lines": Array(lines.prefix(num!)), "num": num!]
    } else {
        return ["error": "You must specify a lines parameter!"]
    }
}
