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

package packages.websocket

import java.net.URI

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import common._
import spray.json._
import spray.json.DefaultJsonProtocol._

@RunWith(classOf[JUnitRunner])
class WebSocketTests extends TestHelpers with WskTestHelpers with BeforeAndAfterAll {

  implicit val wskprops: WskProps = WskProps()
  val wsk = new Wsk()

  val websocketSendAction = "/whisk.system/websocket/send"

  behavior of "Websocket action"

  /**
   * This test requires a websocket server running on the given URI.
   */
  val serverURI: URI = new URI("ws://echo.websocket.org")

  it should "Use the websocket action to send a payload" in {
    val uniquePayload = s"The cow says ${System.currentTimeMillis()}".toJson
    val run =
      wsk.action.invoke(websocketSendAction, Map("uri" -> serverURI.toString.toJson, "payload" -> uniquePayload))
    withActivation(wsk.activation, run, 1 second, 1 second, 180 seconds) { activation =>
      activation.response.success shouldBe true
      activation.response.result shouldBe Some(JsObject("payload" -> uniquePayload))
    }
  }

  it should "Return an error due to a malformed URI" in {
    val badURI = new URI("ws://localhost:80")

    val run = wsk.action.invoke(
      websocketSendAction,
      Map("uri" -> badURI.toString.toJson, "payload" -> "This is the message to send".toJson))
    withActivation(wsk.activation, run) { activation =>
      activation.response.success shouldBe false

      // the exact error content comes from the ws Node module
      activation.response.result.get.fields.get("error") shouldBe defined
    }
  }

  it should "Require a payload parameter" in {
    val run = wsk.action.invoke(websocketSendAction, Map("uri" -> serverURI.toString.toJson))
    withActivation(wsk.activation, run) { activation =>
      activation.response.success shouldBe false
      activation.response.result shouldBe Some(JsObject("error" -> "You must specify a payload parameter.".toJson))
    }
  }

  it should "Require a uri parameter" in {
    val run = wsk.action.invoke(websocketSendAction, Map("payload" -> "This is the message to send".toJson))
    withActivation(wsk.activation, run) { activation =>
      activation.response.success shouldBe false
      activation.response.result shouldBe Some(JsObject("error" -> "You must specify a uri parameter.".toJson))
    }
  }

}
