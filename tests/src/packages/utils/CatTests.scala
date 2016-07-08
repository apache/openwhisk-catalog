/*
 * Copyright 2015-2016 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package packages.utils

import java.io.File

import common._
import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import spray.json._
import spray.json.DefaultJsonProtocol.StringJsonFormat

@RunWith(classOf[JUnitRunner])
class CatTests extends TestHelpers with WskTestHelpers with Matchers {

    implicit val wskprops = WskProps()
    val wsk = new Wsk(usePythonCLI=true)
    val lines = JsArray(JsString("seven"), JsString("eight"), JsString("nine"))
    var catalogDir = new File(scala.util.Properties.userDir.toString(), "../packages")

    behavior of "/utils/cat Action"

    /**
      * Test the Node.js "cat" action
      */
    it should "concatenate an array of strings using the node.js cat action" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val wsk = new Wsk()

            withActivation(wsk.activation, wsk.action.invoke("/whisk.system/utils/cat", Map("lines" -> lines))) {
                _.fields("response").toString should include(""""payload":"seven\neight\nnine"""")
            }
    }

    /**
      * Test the "cat" action using Node.js 6
      */
    it should "concatenate an array of strings using the cat action on node.js 6" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val wsk = new Wsk()

            val file = new File(catalogDir,"utils/cat.js").toString()
            val actionName = "catNodejs6"

            assetHelper.withCleaner(wsk.action, actionName) { (action, _) =>
                action.create(name = actionName, artifact = Some(file), kind = Some("nodejs:6"))
            }

            withActivation(wsk.activation, wsk.action.invoke(actionName, Map("lines" -> lines))) {
                _.fields("response").toString should include(""""payload":"seven\neight\nnine"""")
            }
    }

}
