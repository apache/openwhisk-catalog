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

package packages.samples

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import common.TestHelpers
import common.Wsk
import common.WskProps
import common.WskTestHelpers
import spray.json._
import spray.json.DefaultJsonProtocol._

@RunWith(classOf[JUnitRunner])
class WordCountTest extends TestHelpers
    with WskTestHelpers {

    implicit val wskprops = WskProps()
    val wsk = new Wsk()
    val wordcountAction = "/whisk.system/samples/wordCount"

    behavior of "samples wordCount"

    it should "return the number of words when sending the words as payload" in {
        val expectedNumber = 3
        val run = wsk.action.invoke(wordcountAction,
            Map("payload" -> "Five fuzzy felines".toJson))
        withActivation(wsk.activation, run) {
            activation =>
                activation.response.success shouldBe true
                activation.response.result.get.fields.get("count") shouldBe Some(JsNumber(expectedNumber))
        }
    }

    it should "return an error has occurred: TypeError: Cannot read property 'toString' of undefined " +
        "failure when sending no payload" in {
            val expectedError = "An error has occurred: TypeError: Cannot read property 'toString' of undefined"
            val run = wsk.action.invoke(wordcountAction, Map())
            withActivation(wsk.activation, run) {
                activation =>
                    activation.response.success shouldBe false
                    activation.response.result.get.fields.get("error") shouldBe Some(JsString(expectedError))
            }
        }
}
