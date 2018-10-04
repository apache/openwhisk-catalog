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

package packages.weather

import common._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import spray.json.DefaultJsonProtocol._
import spray.json._

@RunWith(classOf[JUnitRunner])
class WeatherTests
    extends TestHelpers
    with WskTestHelpers {

    implicit val wskprops = WskProps()
    val wsk = new Wsk

    val credentials = TestUtils.getVCAPcredentials("weatherinsights")
    val username = credentials.get("username").toJson
    val password = credentials.get("password").toJson
    val host = credentials.get("host").toJson

    behavior of "Weather Package"

    it should "have the ten day forecast" in {
        val name = "/whisk.system/weather/forecast"
        withActivation(wsk.activation, wsk.action.invoke(name, Map("username" -> username, "password" -> password))) {
            _.response.result.get.toString should include("forecasts")
        }
    }

    it should "have the current forecast" in {
        val name = "/whisk.system/weather/forecast"
        withActivation(wsk.activation, wsk.action.invoke(name, Map("username" -> username, "password" -> password, "timePeriod" -> "current".toJson))) {
            _.response.result.get.toString should include("observation")
        }
    }

    it should "have the 48 hour forecast" in {
        val name = "/whisk.system/weather/forecast"
        withActivation(wsk.activation, wsk.action.invoke(name, Map("username" -> username, "password" -> password, "timePeriod" -> "48hour".toJson))) {
            _.response.result.get.toString should include("forecasts")
        }
    }

    it should "have the timeseries forecast" in {
        val name = "/whisk.system/weather/forecast"
        withActivation(wsk.activation, wsk.action.invoke(name, Map("username" -> username, "password" -> password, "timePeriod" -> "timeseries".toJson))) {
            _.response.result.get.toString should include("observation")
        }
    }

    it should """Use "host" property""" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
        val name = "forecastTest"

        assetHelper.withCleaner(wsk.pkg, name) {
            (pkg, name) =>
                pkg.bind("/whisk.system/weather", name,
                    Map("username" -> username,
                        "password" -> password,
                        "host" -> host))
        }
        println("Invoking the action")
        withActivation(wsk.activation, wsk.action.invoke(s"${name}/forecast")) {
            activation =>
                activation.response.success shouldBe true
        }
    }
}
