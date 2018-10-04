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

package packages.slack

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import common._
import spray.json.DefaultJsonProtocol._
import spray.json._

@RunWith(classOf[JUnitRunner])
class SlackTests extends TestHelpers
    with WskTestHelpers
    with BeforeAndAfterAll {

    implicit val wskprops = WskProps()
    val wsk = new Wsk()

    val credentials = TestUtils.getCredentials("slack_webhook")
    val url = credentials.get("url").getAsString()
    val channel = credentials.get("channel").getAsString()
    val text = "The openwhisk-catalog slack test has finished."
    val username = "incoming-webhook"
    val slackAction = "/whisk.system/slack/post"

    "Slack Package" should "print the object being sent to slack" in {
        val run = wsk.action.invoke(slackAction, Map(
            "username" -> username.toJson,
            "channel" -> channel.toJson,
            "text" -> text.toJson,
            "url" -> url.toJson))
        withActivation(wsk.activation, run) {
            activation =>
                activation.response.success shouldBe true
                val logs = activation.logs.get.toString
                logs should include("successfully sent")
        }
    }

    "Slack Package" should "print the object being sent to default channel in slack" in {
        val run = wsk.action.invoke(slackAction, Map(
            "username" -> username.toJson,
            "text" -> text.toJson,
            "url" -> url.toJson))
        withActivation(wsk.activation, run) {
            activation =>
                activation.response.success shouldBe true
                val logs = activation.logs.get.toString
                logs should include("successfully sent")
        }
    }

}
