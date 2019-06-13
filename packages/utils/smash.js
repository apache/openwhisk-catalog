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
 * Nests all properties under given property.
 *
 * @param { a: 1, b: 2, c: { d: 1, e: 2 }, '$fieldName': 'p' }
 * @returns { p: { a: 1, b: 2, c: { d: 1, e: 2 } } }
 */
function main(args) {
    let parent = args.$fieldName;

    if (typeof parent !== 'string') {
        return {
            error : "Expected an argument '$fieldName' of type 'string'."
        };
    }

    delete args.$fieldName;

    let result = {};
    result[parent] = args;
    return result;
}
