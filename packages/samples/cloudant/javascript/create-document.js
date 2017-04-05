/**
  *
  * main() will be invoked when you Run This Action with:
  *
  * wsk action invoke create-cloudant-document --blocking
  *
  * @param Whisk actions accept a single parameter,
  *        which must be a JSON object.
  *
  * In this case, the params variable will look like:
  *     {
  *            "cloudant_package": "xxxx",
  *            "doc": "xxxx",
  *            "namespace": "xxxx"
  *        }
  *
  * @return which must be a JSON object.
  *         It will be the output of this action.
  *
  */

var openwhisk = require("openwhisk");

function main(params) {

    var wsk = openwhisk();

    // read cloudant package name
    var cloudant = params['cloudant_package'];

    // read document
    var doc = params['doc'];

    var namespace = params['namespace'];

    // access namespace as enviornment variables if not specified in params
    if (typeof namespace === 'undefined' || namespace === null) {
        namespace = process.env['__OW_NAMESPACE'];
    }

    var cloudantActionName = "/" + namespace + "/" + cloudant + "/create-document";

       return wsk.actions.invoke({
        actionName: cloudantActionName,
        params: { 'doc': doc},
        blocking: true,
    })
    .then(activation => {
          console.log("Created a new doc with ID: " + activation.response.result.id);
          return activation.response.result;
    })
    .catch(function (err) {
        console.log("Error creating document");
        return err;
    });
}
