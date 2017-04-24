/*
 * Copyright 2015-2016 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.pimpAny
import spray.json.JsArray
import spray.json.JsObject
import spray.json.JsValue

/**
 * Tests for IBM Watson Developer Cloud packages.
 *
 * @see https://www.ibm.com/watson/developercloud/
 *
 * Current services tested are
 * <ul>
 * <li> language translation
 * <li> speech-to-text
 * <li> text-to-speech
 * </ul>
 */
@RunWith(classOf[JUnitRunner])
class WatsonTests
    extends TestHelpers
    with WskTestHelpers
    with BeforeAndAfterAll {

    implicit val wskprops = WskProps()
    val wsk = new Wsk()

    behavior of "Watson actions"

    /**
     * Watson Natural Language Understanding
     */

    /**
     * Check that a response from NLU represents one entity, "IBM"
     */
    def isIBMEntity(response: CliActivationResponse): Unit = {
        response.success shouldBe true
        val entities = response.result.get.fields.get("response").get.asJsObject.fields.get("entities").toArray
        entities.length should be(1)
        val e : JsObject  = entities(0).asInstanceOf[JsArray].elements(0).asJsObject
        e.fields.get("type") should be(Some("Company".toJson))
        e.fields.get("text") should be(Some("IBM".toJson))
    }

    it should "identify entities and keywords via the Watson Natural Language Understanging API" in {
        val credentials = TestUtils.getVCAPcredentials("natural_language_understanding")
        val username = credentials.get("username")
        val password = credentials.get("password")
        val action = "/whisk.system/watson-NLU/analyze"
        val text = s"Leonardo DiCaprio won Best Actor in a Leading Role for his performance".toJson
        val empty = JsObject(Map.empty[String, JsValue])
        val features = JsObject(Map("entities"-> empty, "keywords" -> empty))
        val run = wsk.action.invoke(action, Map("username" -> username.toJson, "password" -> password.toJson,
                                                        "text" -> text, "features" -> features))
        withActivation(wsk.activation, run) {
            activation =>
               println(activation.response)
               activation.response.success shouldBe true
               val entities = activation.response.result.get.fields.get("response").get.asJsObject.fields.get("entities").toArray
               entities.length should be(1)
               val keywords = activation.response.result.get.fields.get("response").get.asJsObject.fields.get("keywords").toArray
               keywords.length should be(1)
        }
    }

    it should "identify an entity via the Watson Natural Language Understanging API" in {
        val credentials = TestUtils.getVCAPcredentials("natural_language_understanding")
        val username = credentials.get("username")
        val password = credentials.get("password")
        val action = "/whisk.system/watson-NLU/analyzeOneFeature"
        val text = s"IBM".toJson
        val run = wsk.action.invoke(action, Map("username" -> username.toJson, "password" -> password.toJson,
                                                        "text" -> text))
        withActivation(wsk.activation, run) {
            activation =>
                isIBMEntity(activation.response)
        }
    }

    it should "respect the limit when retrieving entities via the Watson Natural Language Understanging API" in {
        val credentials = TestUtils.getVCAPcredentials("natural_language_understanding")
        val username = credentials.get("username")
        val password = credentials.get("password")
        val action = "/whisk.system/watson-NLU/analyzeOneFeature"
        val text = s"IBM or GE".toJson
        val run = wsk.action.invoke(action, Map("username" -> username.toJson, "password" -> password.toJson, "text" -> text,
                                                        "limit" -> "1".toJson))
        withActivation(wsk.activation, run) {
            activation =>
                activation.response.success shouldBe true
                val entities = activation.response.result.get.fields.get("response").get.asJsObject.fields.get("entities").toArray
                entities.length should be(1)
        }
    }

    /**
     * Watson Language Translator
     */

    it should "identify the language of the text via the Watson Language Translator API" in {
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

    it should "translate the language from one to another via the Watson Language Translator API" in {
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

    /**
     * Watson Text to Speech and Speech to Text
     */

    it should "convert the text into speech via the Watson Text To Speech API and convert the speech back to the same text via via the Watson Speech To Text API" in {
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
