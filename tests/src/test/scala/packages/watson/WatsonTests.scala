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

package packages.watson

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner

import common.TestHelpers
import common.Wsk
import common.WskProps
import common.WskTestHelpers
import common.TestUtils
import spray.json.DefaultJsonProtocol._
import spray.json._

@RunWith(classOf[JUnitRunner])
class WatsonTests
    extends TestHelpers
    with WskTestHelpers
    with BeforeAndAfterAll {

    implicit val wskprops = WskProps()
    val wsk = new Wsk()

    behavior of "Watson actions"

    it should "identify the language of the text via the Watson Indentify API" in {
        val credentials = TestUtils.getVCAPcredentials("language_translator")
        val username = credentials.get("username")
        val password = credentials.get("password")
        val languageIdAction = "/whisk.system/watson-translator/languageId"
        val payload = s"Comment allez-vous?".toJson
        val run = wsk.action.invoke(languageIdAction, Map("username" -> username.toJson, "password" -> password.toJson, "payload" -> payload))
        withActivation(wsk.activation, run) {
            activation =>
                activation.response.success shouldBe true
                activation.response.result.get.fields.get("payload") should be(Some(payload))
                activation.response.result.get.fields.get("confidence") == Some(0.785444)
                activation.response.result.get.fields.get("language") should be(Some("fr".toJson))
        }
    }

    it should "translate the language from one to another via the Watson Translator API" in {
        val credentials = TestUtils.getVCAPcredentials("language_translator")
        val username = credentials.get("username")
        val password = credentials.get("password")
        val translatorAction = "/whisk.system/watson-translator/translator"
        val text = s"good morning".toJson
        val result = "bonjour"
        val run = wsk.action.invoke(translatorAction, Map("username" -> username.toJson, "password" -> password.toJson,
            "translateFrom" -> "en".toJson, "translateTo" -> "fr".toJson, "translateParam" -> "text".toJson, "text" -> text))
        withActivation(wsk.activation, run) {
            activation =>
                activation.response.success shouldBe true
                activation.response.result.get.fields.get("text").toString.toLowerCase should include(result)
        }
    }

    it should "convert the text into speech via the Watson textToSpeech API and convert the speech back to the same text via via the Watson speechToText API" in {
        var credentials = TestUtils.getVCAPcredentials("text_to_speech")
        var username = credentials.get("username")
        var password = credentials.get("password")
        val textToSpeechAction = "/whisk.system/watson-textToSpeech/textToSpeech"
        val speech = "hello watson"
        var results = s""
        var run = wsk.action.invoke(textToSpeechAction, Map("username" -> username.toJson, "password" -> password.toJson,
            "payload" -> speech.toJson, "accept" -> "audio/wav".toJson, "encoding" -> "base64".toJson))
        withActivation(wsk.activation, run) {
            activation =>
                activation.response.success shouldBe true
                results = activation.response.result.get.fields.get("payload").get.toString
        }

        credentials = TestUtils.getVCAPcredentials("speech_to_text")
        username = credentials.get("username")
        password = credentials.get("password")
        val speechToTextAction = "/whisk.system/watson-speechToText/speechToText"
        run = wsk.action.invoke(speechToTextAction, Map("username" -> username.toJson, "password" -> password.toJson,
            "payload" -> results.toJson, "content_type" -> "audio/wav".toJson, "encoding" -> "base64".toJson))
        withActivation(wsk.activation, run) {
            activation =>
                activation.response.success shouldBe true
                activation.response.result.get.fields.get("data").toString.toLowerCase should include(speech)
        }
    }
}
