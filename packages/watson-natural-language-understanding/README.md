# Using the Watson Natural Language Understanding package

The `/whisk.system/watson-NLU` package offers a convenient way to call the Watson Natural Language Understanding service.

The package includes the following entities.

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/watson-NLU` | package | username, password | Package for Watson Natural Language Understanding  |
| `/whisk.system/watson-NLU/analyze` | action | text, features, username, password | General API for NLU analysis|
| `/whisk.system/watson-NLU/analyzeOureneFeat` | action | text, feature, limit, username, password | Analyze for one NLU feature |


## Setting up the Watson Natural Language package in Bluemix

If you're using OpenWhisk from Bluemix, OpenWhisk automatically creates package bindings for your Bluemix Watson service instances.

1. Create a Watson Natural Language Understanding service instance in your Bluemix [dashboard](http://console.ng.Bluemix.net).

  Be sure to remember the name of the service instance and the Bluemix organization and space you're in.


2. Create a package binding that is configured for your Watson Translator service.

  ```
  wsk package bind /whisk.system/watson-NLU myWatsonNLU -p username MYUSERNAME -p password MYPASSWORD
  ```


## Anayzing text

The `/whisk.system/watson-NLU/analyzeOneFeature` action invokes the NLU service to analyze one feature of the text. The parameters are as follows:

- `username`: The Watson API user name.
- `password`: The Watson API password.
- `text`: The text to analyze.
- `feature`: The name of the feature type to performance analysis for.
- `limit`: Limit on the number of results to return.

- Invoke the `analyzeOneFeature` action in your package binding as follows:

  ```
  wsk action invoke myWatsonNLU/analyzeOneFeature \
  --blocking \
  --param text "IBM" --param feature "entities"
  ```
