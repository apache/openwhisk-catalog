package packages.slack


import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
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
class SlackTests extends TestHelpers
    with WskTestHelpers
    with BeforeAndAfterAll
    with JsHelpers {

  implicit val wskprops = WskProps()
  val wsk = new Wsk(usePythonCLI = true)
  val username = "Test";
  val channel = "gittoslack";
  val text = "Hello Test!";
  val url = "https://hooks.slack.com/services/ABC/";
  val slackAction = "/whisk.system/slack/post"

  val expectedChannel = "channel: '" + channel + "'"
  val expectedUsername = "username: '" + username + "'";
  val expectedText = "text: '" + text + "'";
  val expectedIcon = "icon_emoji: undefined";

  it should "The slack api should works" in withAssetCleaner(wskprops) {
    (wp, assetHelper) =>
      val run = wsk.action.invoke(slackAction, Map("username" -> username.toJson, "channel" -> channel.toJson, "text" -> text.toJson, "url" -> url.toJson))
      withActivation(wsk.activation, run, 1 second, 1 second, 180 seconds) {
        activation =>
          activation.getFieldPath("response", "success") should be(Some(true.toJson))
          val logs = activation.getFieldPath("logs").toString
          logs should include(expectedChannel)
          logs should include(expectedUsername)
          logs should include(expectedText)
          logs should include(expectedIcon)
          logs should include(url)
      }
  }

}
