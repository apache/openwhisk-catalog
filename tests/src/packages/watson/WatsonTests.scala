package packages.watson

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
import common.TestUtils
import spray.json.DefaultJsonProtocol.BooleanJsonFormat
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.pimpAny

@RunWith(classOf[JUnitRunner])
class WatsonTests extends TestHelpers with WskTestHelpers with BeforeAndAfterAll with JsHelpers {
  implicit val wskprops = WskProps()
  val wsk = new Wsk(usePythonCLI = true)

  val credentials = TestUtils.getVCAPcredentials("language_translation");

  val languageIdAction = "/whisk.system/watson/languageId"
  val username = credentials.get("username")
  val password = credentials.get("password")

  it should "The language Identify api should works" in withAssetCleaner(wskprops) {
    (wp, assetHelper) =>
      val payload = s"Comment allez-vous?".toJson
      val run = wsk.action.invoke(languageIdAction, Map("username" -> username.toJson, "password" -> password.toJson, "payload" -> payload))
      withActivation(wsk.activation, run, 1 second, 1 second, 180 seconds) {
        activation =>
          activation.getFieldPath("response", "success") should be(Some(true.toJson))
          activation.getFieldPath("response", "result", "payload") should be(Some(payload))
          activation.getFieldPath("response", "result", "confidence") == Some(0.785444)
          activation.getFieldPath("response", "result", "language") should be(Some("fr".toJson))
      }
  }

}
