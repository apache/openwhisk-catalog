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



package packages.jira

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

  val credentials = TestUtils.getCredentials("jira_webhook")
  val username = credentials.get("username").getAsString()
  val siteName = credentials.get("siteName").getAsString()
  val accessToken = credentials.get("accessToken").getAsString()
  val webhookName = credentials.get("webhookName").getAsString()
  val force_http = credentials.get("force_http").getAsString()
  val triggerName = "/_/" + credentials.get("triggerName").getAsString()
  val events = credentials.get("events").getAsString()
  val jirafeeds = "/whisk.system/jira/jirafeed"

  "Jira Package" should "print the webhook created on JIRA" in {
    val run = wsk.action.invoke(jirafeeds, Map(
      "username" -> username.toJson,
      "siteName" -> siteName.toJson,
      "accessToken" -> accessToken.toJson,
      "webhookName" -> webhookName.toJson,
      "force_http" -> force_http.toJson,
      "triggerName" -> triggerName.toJson,
      "lifecycleEvent" -> "CREATE".toJson,
      "events" -> events.toJson))

    withActivation(wsk.activation, run) {
      activation =>
        activation.response.success shouldBe true
        val logs = activation.logs.get.toString
        logs should include("Webhook created successfully")
    }
  }

  "Jira Package" should "print the webhook deleted on JIRA" in {
    val run = wsk.action.invoke(jirafeeds, Map(
      "username" -> username.toJson,
      "siteName" -> siteName.toJson,
      "accessToken" -> accessToken.toJson,
      "webhookName" -> webhookName.toJson,
      "force_http" -> force_http.toJson,
      "triggerName" -> triggerName.toJson,
      "lifecycleEvent" -> "DELETE".toJson,
      "events" -> events.toJson))
    withActivation(wsk.activation, run) {
      activation =>
        activation.response.success shouldBe true
        val logs = activation.logs.get.toString
        logs should include("Webhook deleted successfully")
    }
  }

}
