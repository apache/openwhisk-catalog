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

package packages.combinators

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import common.{ TestHelpers, Wsk, WskProps, WskTestHelpers }
import spray.json._
import spray.json.DefaultJsonProtocol._

@RunWith(classOf[JUnitRunner])
class CombinatorTests extends TestHelpers with WskTestHelpers {
    implicit val wskprops = WskProps()
    val wsk = new Wsk()

    val catalogNamespace = "whisk.system"
    val echo = s"/$catalogNamespace/utils/echo"
    val hello = s"/$catalogNamespace/samples/greeting"
    def combinator(c: String) = s"/$catalogNamespace/combinators/$c"
    val ignoreCerts = Map("$ignore_certs" -> true.toJson)

    behavior of "retry"

    it should "retry action up to n times" in {
        val rr = wsk.action.invoke(
            combinator("retry"),
            ignoreCerts ++ Map("$actionName" -> echo.toJson,
                "$attempts" -> 3.toJson,
                "error" -> true.toJson))

        withActivation(wsk.activation, rr) { activation =>
            activation.response.success shouldBe false
            activation.logs.get.length shouldBe 3
            activation.response.result shouldBe Some {
                JsObject("error" -> "Invocation failed. No retries left.".toJson)
            }
        }
    }

    it should "retry action just once when successful" in {
        val rr = wsk.action.invoke(
            combinator("retry"),
            ignoreCerts ++ Map("$actionName" -> echo.toJson,
                "$attempts" -> 3.toJson,
                "success" -> true.toJson))

        withActivation(wsk.activation, rr) { activation =>
            activation.response.success shouldBe true
            activation.logs.get.length shouldBe 0
            activation.response.result shouldBe Some {
                JsObject("success" -> true.toJson)
            }
        }
    }

    behavior of "eca"

    it should "run condition action when predicate is true" in {
        val continue = wsk.action.invoke(
            combinator("eca"),
            ignoreCerts ++ Map("$conditionName" -> echo.toJson,
                "$actionName" -> hello.toJson,
                "name" -> "eca".toJson,
                "place" -> "test".toJson))

        withActivation(wsk.activation, continue) { activation =>
            activation.response.success shouldBe true
            activation.response.result shouldBe Some {
                JsObject("payload" -> "Hello, eca from test!".toJson)
            }
        }
    }

    it should "not run condition action when predicate is false" in {
        val break = wsk.action.invoke(
            combinator("eca"),
            ignoreCerts ++ Map("$conditionName" -> echo.toJson,
                "$actionName" -> hello.toJson,
                "error" -> true.toJson))

        withActivation(wsk.activation, break) { activation =>
            activation.response.success shouldBe true
            activation.response.result shouldBe Some {
                JsObject("$eca" -> "Condition was false.".toJson)
            }
        }
    }

    behavior of "forwarder"

    it should "forward parameters" in {
        val rr = wsk.action.invoke(
            combinator("forwarder"),
            ignoreCerts ++ Map("$actionName" -> hello.toJson,
                "$actionArgs" -> Array("name", "place").toJson,
                "$forward" -> Array("xyz").toJson,
                "name" -> "forwarder".toJson,
                "place" -> "test".toJson,
                "xyz" -> "zyx".toJson))

        withActivation(wsk.activation, rr) { activation =>
            activation.response.success shouldBe true
            activation.response.result shouldBe Some {
                JsObject(
                    "payload" -> "Hello, forwarder from test!".toJson,
                    "xyz" -> "zyx".toJson)
            }
        }
    }

    it should "return error when intermediate action fails" in {
        val rr = wsk.action.invoke(
            combinator("forwarder"),
            ignoreCerts ++ Map("$actionName" -> echo.toJson,
                "$actionArgs" -> Array("error").toJson,
                "$forward" -> Array("xyz").toJson,
                "error" -> true.toJson,
                "xyz" -> "zyx".toJson))

        withActivation(wsk.activation, rr) { activation =>
            activation.response.success shouldBe false
            activation.response.result shouldBe Some {
                JsObject("error" -> s"There was a problem invoking $echo".toJson)
            }
        }
    }

    behavior of "trycatch"

    it should "try and continue when there is no error" in {
        val rr = wsk.action.invoke(
            combinator("trycatch"),
            ignoreCerts ++ Map("$tryName" -> echo.toJson,
                "$catchName" -> hello.toJson,
                "name" -> "trycatch".toJson,
                "place" -> "test".toJson))

        withActivation(wsk.activation, rr) { activation =>
            activation.response.success shouldBe true
            activation.response.result shouldBe Some {
                JsObject(
                    "name" -> "trycatch".toJson,
                    "place" -> "test".toJson)
            }
        }
    }

    it should "catch and handle when there is an error" in {
        val rr = wsk.action.invoke(
            combinator("trycatch"),
            ignoreCerts ++ Map("$tryName" -> echo.toJson,
                "$catchName" -> hello.toJson,
                "error" -> true.toJson))

        withActivation(wsk.activation, rr) { activation =>
            activation.response.success shouldBe true
            activation.response.result shouldBe Some {
                JsObject("payload" -> "Hello, stranger from somewhere!".toJson)
            }
        }
    }

    it should "catch and preserve handler error when handler fails" in {
        val rr = wsk.action.invoke(
            combinator("trycatch"),
            ignoreCerts ++ Map("$tryName" -> echo.toJson,
                "$catchName" -> echo.toJson,
                "error" -> true.toJson))

        withActivation(wsk.activation, rr) { activation =>
            activation.response.success shouldBe false
            activation.response.result shouldBe Some {
                JsObject("error" -> true.toJson)
            }
        }
    }

    it should "catch and chain exception when catch handler fails" in {
        val badCatchName = "invalid%name"
        val rr = wsk.action.invoke(
            combinator("trycatch"),
            ignoreCerts ++ Map("$tryName" -> echo.toJson,
                "$catchName" -> badCatchName.toJson,
                "error" -> true.toJson))

        withActivation(wsk.activation, rr) { activation =>
            activation.response.success shouldBe false
            val error = activation.response.result.get.fields("error").asJsObject
            error.fields("message") shouldBe JsString(s"There was a problem invoking $badCatchName.")
            error.fields("cause") shouldBe a[JsString]
        }
    }
}
