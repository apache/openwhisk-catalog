package packages.samples

import java.io.File

import common._
import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import spray.json._
import spray.json.DefaultJsonProtocol.StringJsonFormat

@RunWith(classOf[JUnitRunner])
class HelloPromisesTest extends TestHelpers with WskTestHelpers with Matchers {

    implicit val wskprops = WskProps()
    val wsk = new Wsk()
    val catalogDir = new File(scala.util.Properties.userDir.toString(), "../packages")
    val path = "samples/helloPromises/javascript/helloPromises.js"

    behavior of "helloPromises action"

    /**
     * Test the "helloPromises" action using Node.js 6
     */
    it should "return a hello message as an array of strings using helloPromises action on nodejs 6" in withAssetCleaner(wskprops) {
        (wp, assetHelper) =>
            val file = new File(catalogDir, path).toString()
            val actionName = "promisesNodejs6"

            assetHelper.withCleaner(wsk.action, actionName) { (action, _) =>
                action.create(name = actionName, artifact = Some(file), kind = Some("nodejs:6"))
            }

            withActivation(wsk.activation, wsk.action.invoke(actionName, Map("place" -> "Chicago".toJson))) {
                _.response.result.get.toString should include(""""lines":["Hello,","stranger","from","Chicago!"]""")
            }
    }
}
