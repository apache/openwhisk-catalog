package packages.samples

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import common.JsHelpers
import common.TestHelpers
import common.Wsk
import common.WskProps
import common.WskTestHelpers
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.pimpAny

@RunWith(classOf[JUnitRunner])
class CurlTest extends TestHelpers with WskTestHelpers with JsHelpers {
    implicit val wskprops = WskProps()
    val wsk = new Wsk()
    val greetingAction = "/whisk.system/samples/curl"
    val catalogDir = new File(scala.util.Properties.userDir.toString(), "../packages")
    val path = "samples/curl/javascript/curl.js"
    behavior of "samples curl"

    it should "Return Could not resolve host when sending no parameter" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val expectedError = "Could not resolve host"
            val run = wsk.action.invoke(greetingAction, Map())
            withActivation(wsk.activation, run) {
                _.response.result.get.toString should include (expectedError)
            }
    }

    it should "Return the web content when sending the public google as the payload" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
        val expectedBody = "<HTML>"
        val run = wsk.action.invoke(greetingAction, Map("payload" -> "google.com".toJson))
        withActivation(wsk.activation, run) {
            _.response.result.get.toString should include (expectedBody)
        }
    }
}
