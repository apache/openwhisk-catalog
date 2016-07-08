package packages.samples

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
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
class GreetingTest extends TestHelpers with WskTestHelpers with BeforeAndAfterAll with JsHelpers {
  implicit val wskprops = WskProps()
  val wsk = new Wsk(usePythonCLI=true)

  val greetingAction = "/whisk.system/samples/greeting"

  behavior of "samples greeting"

  it should "contains stranger when send wrong default parameters" in withAssetCleaner(wskprops) {
    (wp, assetHelper) =>
      val helloStranger = "Hello, stranger from somewhere!".toJson
      val run = wsk.action.invoke(greetingAction, Map("dummy" -> "dummy".toJson))
      withActivation(wsk.activation, run, 1 second, 1 second, 180 seconds) {
        activation =>
          activation.getFieldPath("response", "success") should be(Some(true.toJson))
          activation.getFieldPath("response", "result", "payload") should be(Some(helloStranger))
      }
  }
  
  it should "contains name with name paramerer!" in withAssetCleaner(wskprops){
    (wp, assetHelper) =>
      val helloStranger = "Hello, Mork from somewhere!".toJson
      val run = wsk.action.invoke(greetingAction, Map("name" -> "Mork".toJson))
      withActivation(wsk.activation, run, 1 second, 1 second, 180 seconds) {
        activation =>
          activation.getFieldPath("response", "success") should be(Some(true.toJson))
          activation.getFieldPath("response", "result", "payload") should be(Some(helloStranger))
      }
  }
  
  it should "contains name and place with both paramerer!" in withAssetCleaner(wskprops){
    (wp, assetHelper) =>
      val helloStranger = "Hello, Mork from Ork!".toJson
      val run = wsk.action.invoke(greetingAction, Map("name" -> "Mork".toJson, "place" -> "Ork".toJson))
      withActivation(wsk.activation, run, 1 second, 1 second, 180 seconds) {
        activation =>
          activation.getFieldPath("response", "success") should be(Some(true.toJson))
          activation.getFieldPath("response", "result", "payload") should be(Some(helloStranger))
      }
  }
}
