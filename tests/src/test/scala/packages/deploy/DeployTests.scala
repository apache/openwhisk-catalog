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

package packages.deploy

import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import common._
import spray.json.DefaultJsonProtocol._
import spray.json._

@RunWith(classOf[JUnitRunner])
class DeployTests extends TestHelpers
    with WskTestHelpers
    with BeforeAndAfterAll {

    implicit val wskprops = WskProps()
    val wsk = new Wsk()

    //set parameters for deploy tests
    val helloWorldRepo = "https://github.com/ibm-functions/blueprint-hello-world"
    val cloudantTriggerRepo = "https://github.com/ibm-functions/blueprint-cloudant-trigger"
    val incorrectGithubRepo = "https://github.com/ibm-functions/blueprint-hello-world-incorrect"
    val manifestPath = "runtimes/node"
    val incorrectManifestPath = "runtimes/none"
    val uselessEnvData = "\"something\":\"useless\""
    val deployAction = "/whisk.system/deploy/wskdeploy"
    val helloWorldAction = "openwhisk-helloworld/helloworld"

    //test to create the hello world blueprint from github
    "Deploy Package" should "create the hello world action from github url" in {
      val run = wsk.action.invoke(deployAction, Map(
        "repo" -> helloWorldRepo.toJson,
        "manifestPath" -> manifestPath.toJson))
        withActivation(wsk.activation, run) {
          activation =>
          activation.response.success shouldBe true
          val logs = activation.logs.get.toString
          logs should include("Action openwhisk-helloworld/helloworld has been successfully deployed.")
        }
    }

    //test to invoke the hello world action from github
    "Deploy Package" should "run the hello world action after it's created" in {
      val run = wsk.action.invoke(helloWorldAction, Map(
        "message" -> "Mindy".toJson))
        val responseMessage = "you sent me Mindy".toJson
        withActivation(wsk.activation, run) {
          activation =>
          activation.response.success shouldBe true
          activation.response.result shouldBe Some(JsObject("message" -> responseMessage))
        }
    }

    //test to create a blueprint with no github repo provided
    "Deploy Package" should "return error if there is no github repo provided" in {
      val run = wsk.action.invoke(deployAction, Map(
        "manifestPath" -> manifestPath.toJson))
        withActivation(wsk.activation, run) {
          activation =>
          activation.response.success shouldBe false
          activation.response.result.get.toString should include("Please enter the GitHub repo in params")
        }
    }

    //test to create a blueprint with a nonexistant github repo provided
    "Deploy Package" should "return error if there is an nonexistant repo provided" in {
      val run = wsk.action.invoke(deployAction, Map(
        "repo" -> incorrectGithubRepo.toJson,
        "manifestPath" -> manifestPath.toJson))
        withActivation(wsk.activation, run) {
          activation =>
          activation.response.success shouldBe false
          activation.response.result.get.toString should include("There was a problem cloning from github.")
        }
    }

    //test to create a blueprint with a nonexistant github repo provided
    "Deploy Package" should "return succeed if useless envData is provided" in {
      val run = wsk.action.invoke(deployAction, Map(
        "repo" -> helloWorldRepo.toJson,
        "manifestPath" -> manifestPath.toJson,
        "envData" -> uselessEnvData.toJson))
        withActivation(wsk.activation, run) {
          activation =>
          activation.response.success shouldBe true
          val logs = activation.logs.get.toString
          logs should include("Action openwhisk-helloworld/helloworld has been successfully deployed.")
        }
    }

}
