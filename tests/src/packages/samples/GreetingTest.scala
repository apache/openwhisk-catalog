package packages.samples

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import common.JsHelpers
import common.TestHelpers
import common.Wsk
import common.WskProps
import common.WskTestHelpers
import spray.json.DefaultJsonProtocol.BooleanJsonFormat
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.pimpAny

@RunWith(classOf[JUnitRunner])
class GreetingTest extends TestHelpers
    with WskTestHelpers
    with JsHelpers {

    implicit val wskprops = WskProps()
    val wsk = new Wsk(usePythonCLI=false)
    var catalogDir = new File(scala.util.Properties.userDir.toString(), "../packages")
    val greetingAction = "/whisk.system/samples/greeting"
    val sample_file = "samples/greeting/javascript/greeting.js"
    behavior of "samples greeting"

    it should "Contain stranger when send wrong default parameters" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val helloMessage = "Hello, stranger from somewhere!".toJson
            val run = wsk.action.invoke(greetingAction, Map("dummy" -> "dummy".toJson))
            withActivation(wsk.activation, run) {
                activation =>
                    activation.getFieldPath("response", "success") should be(Some(true.toJson))
                    activation.getFieldPath("response", "result", "payload") should be(Some(helloMessage))
            }
    }
  
    it should "Contain name with name paramerer" in withAssetCleaner(wskprops){
        (wp, assetHelper) =>
            val helloStranger = "Hello, Mork from somewhere!".toJson
            val run = wsk.action.invoke(greetingAction, Map("name" -> "Mork".toJson))
            withActivation(wsk.activation, run) {
                activation =>
                    activation.getFieldPath("response", "success") should be(Some(true.toJson))
                    activation.getFieldPath("response", "result", "payload") should be(Some(helloStranger))
            }
    }
  
    it should "Contain name and place with both paramerer" in withAssetCleaner(wskprops){
        (wp, assetHelper) =>
            val helloMessage = "Hello, Mork from Ork!".toJson
            val run = wsk.action.invoke(greetingAction, Map("name" -> "Mork".toJson, "place" -> "Ork".toJson))
            withActivation(wsk.activation, run) {
                activation =>
                    activation.getFieldPath("response", "success") should be(Some(true.toJson))
                    activation.getFieldPath("response", "result", "payload") should be(Some(helloMessage))
            }
    }
}
