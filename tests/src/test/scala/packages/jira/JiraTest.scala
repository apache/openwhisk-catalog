
package packages.slack

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import common._
import spray.json.DefaultJsonProtocol._
import spray.json._

@RunWith(classOf[JUnitRunner])
class JiraTests extends TestHelpers with WskTestHelpers with BeforeAndAfterAll {
  implicit val wskprops = WskProps()
  val wsk = new Wsk()

  val credentials = TestUtils.getCredentials("slack_webhook")
  val username = credentials.get("username").getAsString()
  val siteName = credentials.get("siteName").getAsString()
  val accessToken = credentials.get("accessToken").getAsString()
  val webhookName = credentials.get("webhookName").getAsString()
  val force_http = credentials.get("force_http").getAsString()
  val events = credentials.get("events").getAsString().split(',') //split this
  val myJiraFeed = credentials.get("channel").getAsString()

  "Jira Package" should "print the webhook created on JIRA" in {
    val run = wsk.trigger.create(myJiraFeed, Map(
      "username" -> username.toJson,
      "siteName" -> siteName.toJson,
      "accessToken" -> accessToken.toJson,
      "webhookName" -> webhookName.toJson,
      "force_http" -> force_http.toJson,
      "events" -> events.split(,)toJson))   //can be a syntax error
    withActivation(wsk.activation, run) {
      activation =>
        activation.response.success shouldBe true
        val logs = activation.logs.get.toString
        logs should include("Webhook created successfully")
    }
  }

  "Jira Package" should "print the webhook deleted on JIRA" in {
    val run = wsk.trigger.delete(myJiraTrigger, Map(
      "username" -> username.toJson,
      "siteName" -> siteName.toJson,
      "accessToken" -> accessToken.toJson,
      //        "webhookName" -> webhookName.toJson,
      "force_http" -> force_http.toJson))
    //        "events" -> events.toJson))                       //this should be splitted
    withActivation(wsk.activation, run) {
      activation =>
        activation.response.success shouldBe true
        val logs = activation.logs.get.toString
        logs should include("Webhook deleted successfully")
    }
  }

}
