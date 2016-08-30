package packages.samples

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import common.JsHelpers
import common.TestHelpers
import common.Wsk
import common.WskProps
import common.WskTestHelpers

@RunWith(classOf[JUnitRunner])
class HelloTests
        extends TestHelpers
        with WskTestHelpers
        with JsHelpers {

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
