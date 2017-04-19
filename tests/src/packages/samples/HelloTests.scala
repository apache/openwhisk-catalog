package packages.samples

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import common.JsHelpers
import common.TestHelpers
import common.Wsk
import common.WskProps
import common.WskTestHelpers
import spray.json.DefaultJsonProtocol.BooleanJsonFormat
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.pimpAny

@RunWith(classOf[JUnitRunner])
class HelloTests
        extends TestHelpers
        with WskTestHelpers
        with JsHelpers {

    implicit val wskprops = WskProps()
    val wsk = new Wsk(usePythonCLI=true)
    val helloAction = "/whisk.system/samples/helloWorld"

    behavior of "samples hello"

    it should "indicates this action is successfully called" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val run = wsk.action.invoke(helloAction, Map())
            withActivation(wsk.activation, run) {
                activation =>
                    activation.getFieldPath("response", "success") should be(Some(true.toJson))
            }
    }

}
