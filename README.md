# Openwhisk Catalog

[![Build Status](https://travis-ci.org/apache/incubator-openwhisk-catalog.svg?branch=master)](https://travis-ci.org/apache/incubator-openwhisk-catalog)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

This openwhisk-catalog maintains the package catalogs of openwhisk. In OpenWhisk, the catalog
of packages gives you an easy way to enhance your app with useful capabilities, and to access
external services in the ecosystem. Examples of external services that are OpenWhisk-enabled 
include IBM Watson API, the Weather Company, Slack, and GitHub.system packages and sample packages. 

The catalog is available as packages in the `/whisk.system` namespace. See [Browsing packages](https://github.com/openwhisk/openwhisk/blob/master/docs/packages.md#browsing-packages) 
for information about how to browse the catalog by using the command line tool.

## How to install openWhisk-catalog

### Pre-requisites
- [openwhisk](https://github.com/openwhisk/openwhisk/blob/master/README.md) is installed.
- Environment variable `OPENWHISK_HOME` is configured as the path to `openwhisk` source code directory.

### Install openwhisk-catalog

Run the `installCatalog.sh` script to install the catalog.

 ```
$ OPENWHISK_HOME=<path_to_openwhisk> ./packages/installCatalog.sh
 ```

This script needs the API hostname to target and the authentication credentials for administration account. These values can be set explicitly using the `API_HOST` and `CATALOG_AUTH_KEY` environment parameters.

*If `API_HOST` is not set, the script looks for the `edge.host` value in the `whisk.properties` file under `$OPENWHISK_HOME`.*

*If `CATALOG_AUTH_KEY` is not set, the script looks for the contents of the `ansible/files/auth.whisk.system` file under `$OPENWHISK_HOME`.*

## Existing packages in catalog

| Package | Description |
| --- | --- |
| [/whisk.system/github](./packages/github/README.md) | offers a convenient way to use the [GitHub APIs](https://developer.github.com/). |
| [/whisk.system/slack](./packages/watson-translator/README.md) | offers a convenient way to use the [Slack APIs](https://api.slack.com/). |
| [/whisk.system/watson-translator](./packages/watson-translator/README.md) | Package for text translation and language identification|
| [/whisk.system/watson-speechToText](./packages/watson-speechToText/README.md) | Package to convert speech into text|
| [/whisk.system/watson-textToSpeech](./packages/watson-textToSpeech/README.md) | Package to convert text into speech|
| [/whisk.system/weather](./packages/weather/README.md) | Services from the Weather Company Data for IBM Bluemix API|
| [/whisk.system/websocket](./packages/websocket/README.md) | Package to send messages to Web Socket server|

<!--
TODO: place holder until we have a README for samples 
| [/whisk.system/samples](./packages/samples/README.md) | offers sample actions in different programming languages |
-->
<!--
TODO: place holder until we have a README for utils
| [/whisk.system/utils](./packages/utils/README.md) | offers utilities actions such as cat, echo, and etc. |
-->

## How to create packages

If you want to create your own packages,  [openwhisk-package-template](https://github.com/openwhisk/openwhisk-package-template)
is a good package creation template to help you build, test and integrate new packages.

For more details about how to use these packages, you can go to the README.md under each package folder.

