package packages.samples

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import common.JsHelpers
import common.TestHelpers
import common.Wsk
import common.WskProps
import common.WskTestHelpers
import spray.json._
import spray.json.DefaultJsonProtocol._

@RunWith(classOf[JUnitRunner])
class WordCountTest extends TestHelpers
    with WskTestHelpers
    with JsHelpers {

    implicit val wskprops = WskProps()
    val wsk = new Wsk()
    val wordcountAction = "/whisk.system/samples/wordCount"

    behavior of "samples wordCount"

    it should "return the number of words when sending the words as payload" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
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
        "failure when sending no payload" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val expectedError = "An error has occurred: TypeError: Cannot read property 'toString' of undefined"
            val run = wsk.action.invoke(wordcountAction, Map())
            withActivation(wsk.activation, run) {
                activation =>
                    activation.response.success shouldBe false
                    activation.response.result.get.fields.get("error") shouldBe Some(JsString(expectedError))
            }
    }
}
