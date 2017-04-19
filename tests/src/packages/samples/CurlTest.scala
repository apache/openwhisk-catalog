package packages.samples

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

import common.JsHelpers
import common.TestHelpers
import common.Wsk
import common.WskProps
import common.WskTestHelpers
import spray.json.DefaultJsonProtocol.BooleanJsonFormat
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.pimpAny

@RunWith(classOf[JUnitRunner])
class CurlTest extends TestHelpers with WskTestHelpers with JsHelpers {
    implicit val wskprops = WskProps()
    val wsk = new Wsk(usePythonCLI=true)
    val greetingAction = "/whisk.system/samples/curl"
    val catalogDir = new File(scala.util.Properties.userDir.toString(), "../packages")
    val path = "samples/curl/javascript/curl.js"
    behavior of "samples curl"

    it should "Return Could not resolve host when sending no parameter" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val expectedError = "Could not resolve host"
            val run = wsk.action.invoke(greetingAction, Map())
            withActivation(wsk.activation, run) {
                _.fields("response").toString should include (expectedError)
            }
    }

    it should "Return the web content when sending the public google as the payload" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
        val expectedBody = "<HTML>"
        val run = wsk.action.invoke(greetingAction, Map("payload" -> "google.com".toJson))
        withActivation(wsk.activation, run) {
            _.fields("response").toString should include (expectedBody)
        }
    }

    it should "Return the web content when sending the public google as the payload on nodejs" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val expectedBody = "<HTML>"
            val file = Some(new File(catalogDir, path).toString())
            val actionName = "curlNodejs"

            assetHelper.withCleaner(wsk.action, actionName) { (action, _) =>
                action.create(name = actionName, artifact = file, kind = Some("nodejs"))
            }

            withActivation(wsk.activation, wsk.action.invoke(actionName, Map("payload" -> "google.com".toJson))) {
                _.fields("response").toString should include(expectedBody)
            }
    }

    it should "Return the web content when sending the public google as the payload on nodejs 6" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val expectedBody = "<HTML>"
            val file = Some(new File(catalogDir, path).toString())
            val actionName = "curlNodejs6"

            assetHelper.withCleaner(wsk.action, actionName) { (action, _) =>
                action.create(name = actionName, artifact = file, kind = Some("nodejs:6"))
            }

            withActivation(wsk.activation, wsk.action.invoke(actionName, Map("payload" -> "google.com".toJson))) {
                _.fields("response").toString should include(expectedBody)
            }
    }
}
