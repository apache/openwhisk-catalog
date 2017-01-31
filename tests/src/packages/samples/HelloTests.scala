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
package packages.samples

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import common.TestHelpers
import common.Wsk
import common.WskProps
import common.WskTestHelpers

@RunWith(classOf[JUnitRunner])
class HelloTests
        extends TestHelpers
        with WskTestHelpers {

    implicit val wskprops = WskProps()
    val wsk = new Wsk()
    val helloAction = "/whisk.system/samples/helloWorld"

    behavior of "samples hello"

    it should "indicates this action is successfully called" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val run = wsk.action.invoke(helloAction, Map())
            withActivation(wsk.activation, run) {
                activation =>
                    activation.response.success shouldBe true
            }
    }

}
