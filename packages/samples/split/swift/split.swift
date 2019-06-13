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
 */

/**
 * Sample code using the experimental Swift 3 runtime
 * Splits a string into an array of strings
 * Return lines in an array.
 * @param payload A string.
 * @param separator The separator to use for splitting the string
 */
func main(args: [String:Any]) -> [String:Any] {
    if let payload = args["payload"] as? String {
        let lines: [String]
        if let separator = args["separator"] as? String {
            lines = payload.components(separatedBy: separator)
        } else {
            lines = payload.characters.split {$0 == "\n" || $0 == "\r\n"}.map(String.init)
        }
        return ["lines": lines, "payload": payload]
    } else {
        return ["error": "You must specify a payload parameter!"]
    }
}
