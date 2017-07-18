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

package packages.utils

import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import common._
import spray.json._
import spray.json.DefaultJsonProtocol._

@RunWith(classOf[JUnitRunner])
class UtilsTests extends TestHelpers with WskTestHelpers with Matchers {

    val catalogNamespace = "whisk.system"
    def util(u: String) = s"/$catalogNamespace/utils/$u"

    implicit val wskprops = WskProps()
    val wsk = new Wsk()
    val namespace = wsk.namespace.whois()

    val lines = Array("seven", "eight", "nine")

    "cat" should "concatenate an array of strings using the node.js cat action" in {
        withActivation(wsk.activation, wsk.action.invoke(util("cat"), Map("lines" -> lines.toJson))) {
            _.response.result.get shouldBe {
                JsObject(
                    "lines" -> lines.toJson,
                    "payload" -> lines.mkString("\n").toJson)
            }
        }
    }

    "head" should "extract first n elements of an array of strings using the node.js head action" in {
        withActivation(wsk.activation, wsk.action.invoke(util("head"), Map("lines" -> lines.toJson, "num" -> 2.toJson))) {
            _.response.result.get shouldBe {
                JsObject(
                    "num" -> 2.toJson,
                    "lines" -> lines.take(2).toJson)
            }
        }
    }

    "smash" should "smash fields into a new object" in {
        val args = Map(
            "$fieldName" -> "x".toJson,
            "a" -> 1.toJson,
            "b" -> 2.toJson,
            "c" -> JsObject("d" -> 3.toJson, "e" -> 4.toJson))

        withActivation(wsk.activation, wsk.action.invoke(util("smash"), args)) {
            _.response.result.get shouldBe {
                JsObject("x" -> (args - "$fieldName").toJson)
            }
        }

        withActivation(wsk.activation, wsk.action.invoke(util("smash"), Map("$fieldName" -> "x".toJson))) {
            _.response.result.get shouldBe {
                JsObject("x" -> JsObject())
            }
        }

        withActivation(wsk.activation, wsk.action.invoke(util("smash"), Map())) {
            _.response.result.get shouldBe {
                JsObject("error" -> "Expected an argument '$fieldName' of type 'string'.".toJson)
            }
        }
    }

    "sort" should "sort an array of strings using the node.js sort action" in {
        withActivation(wsk.activation, wsk.action.invoke(util("sort"), Map("lines" -> lines.toJson))) {
            _.response.result.get shouldBe {
                JsObject(
                    "length" -> lines.length.toJson,
                    "lines" -> lines.sortWith(_ < _).toJson)
            }
        }
    }

    "split" should "split a string into an array of strings using the node.js split action" in {
        val args = Map(
            "payload" -> lines.mkString(",").toJson,
            "separator" -> ",".toJson)

        withActivation(wsk.activation, wsk.action.invoke(util("split"), args)) {
            _.response.result.get shouldBe {
                JsObject(
                    "lines" -> lines.toJson,
                    "payload" -> args("payload").toJson)
            }
        }
    }

    "namespace" should "identify namespace" in {
        withActivation(wsk.activation, wsk.action.invoke(util("namespace"))) {
            _.response.result.get shouldBe {
                JsObject("namespace" -> namespace.toJson)
            }
        }
    }

    "hosturl" should "generate url for (web) actions and triggers" in {
        val base = WhiskProperties.getApiHostForAction
        val tests: Seq[(String, Map[String, JsValue])] = Seq(
            (s"$base/api/v1/namespaces/$namespace/actions", Map()),
            (s"$base/api/v1/namespaces/$namespace/actions/echo", Map("path" -> "echo".toJson)),
            (s"$base/api/v1/namespaces/$namespace/actions/utils/echo", Map("path" -> "utils/echo".toJson)),
            (s"$base/api/v1/namespaces/$namespace/triggers/echo", Map("trigger" -> true.toJson, "path" -> "echo".toJson)),
            (s"$base/api/v1/web/$namespace", Map("web" -> true.toJson)),
            (s"$base/api/v1/web/$namespace/default/echo.text", Map("web" -> true.toJson, "path" -> "echo".toJson, "ext" -> ".text".toJson)),
            (s"$base/api/v1/web/$namespace/utils/echo", Map("web" -> true.toJson, "path" -> "utils/echo".toJson)))

        tests.foreach {
            case (url, args) =>
                withActivation(wsk.activation, wsk.action.invoke(util("hosturl"), args)) {
                    _.response.result.get shouldBe {
                        JsObject("url" -> url.toJson)
                    }
                }
        }
    }
}
