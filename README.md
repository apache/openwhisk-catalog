# Openwhisk Catalog

[![Build Status](https://travis-ci.org/openwhisk/openwhisk-catalog.svg?branch=master)](https://travis-ci.org/openwhisk/openwhisk-catalog)
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

We should be able to run the script installCatalog.sh to install the catalog like:

```
./installCatalog.sh <catalog_auth_key> <api_host> <catalog_namespace> 
```

The first argument \<catalog_auth_key\>, defines the secret key used to authenticate the openwhisk
service. The second argument \<api_host\>, determines the location, where the openwhisk edge host is running,
in the format of IP or hostname. The third argument \<catalog_namespace\>, specifies the namespace used for all the
actions and packages.

## Existing packages in catalog

| Package | Description |
| --- | --- |
| [/whisk.system/github](./packages/github/README.md) | offers a convenient way to use the [GitHub APIs](https://developer.github.com/). |
| `/whisk.system/samples` | offers sample actions in different languages |
| [/whisk.system/slack](./packages/watson-translator/README.md) | offers a convenient way to use the [Slack APIs](https://api.slack.com/). |
| `/whisk.system/utils` | offers utilities actions such as cat, echo, and etc. |
| [/whisk.system/watson-translator](./packages/watson-translator/README.md) | Package for text translation and language identification|
| [/whisk.system/watson-speechToText](./packages/watson-speechToText/README.md) | Package to convert speech into text|
| [/whisk.system/watson-textToSpeech](./packages/watson-textToSpeech/README.md) | Package to convert text into speech|
| [/whisk.system/weather](./packages/weather/README.md) | Services from the Weather Company Data for IBM Bluemix API|

## How to create packages

If you want to create your own packages,  [openwhisk-package-template](https://github.com/openwhisk/openwhisk-package-template)
is a good package creation template to help you build, test and integrate new packages.

For more details about how to use these packages, you can go to the README.md under each package folder.


## Using the Push package

The `/whisk.system/pushnotifications` package enables you to work with a push service. 

### Install the IBM Push Notification OpenWhisk package 

Download the Push package form the  [wsk-pkg-pushnotification](https://github.com/openwhisk/wsk-pkg-pushnotifications) repository.

Run the install script provided inside the package download

  ```
  $ git clone --depth=1 https://github.com/openwhisk/wsk-pkg-pushnotifications
  $ cd wsk-pkg-pushnotifications
  $ ./install.sh APIHOST AUTH WSK_CLI
  ```

  The `APIHOST` is the OpenWhisk API hostname, `AUTH` is your auth key, and `WSK_CLI` is location of the Openwhisk CLI binary.		     

The package includes the following action and feed:

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/pushnotifications` | package | appId, appSecret  | Work with the Push Service |
| `/whisk.system/pushnotifications/sendMessage` | action | text, url, deviceIds, platforms, tagNames, apnsBadge, apnsCategory, apnsActionKeyTitle, apnsSound, apnsPayload, apnsType, gcmCollapseKey, gcmDelayWhileIdle, gcmPayload, gcmPriority, gcmSound, gcmTimeToLive | Send push notification to one or more specified devices |
| `/whisk.system/pushnotifications/webhook` | feed | events | Fire trigger events on device activities (device registration, unregistration, subscription, or unsubscription) on the Push service |
Creating a package binding with the `appId` and `appSecret` values is suggested. This way, you don't need to specify these credentials every time you invoke the actions in the package.

## Using the WebSocket package

The `/whisk.system/websocket` package offers a convenient way post messages to a WebSocket.

The package includes the following action:

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/websocket` | package | uri | Utilities for communicating with WebSockets |
| `/whisk.system/websocket/send` | action | uri, payload | Send the payload to the WebSocket URI |

If you plan to send many messages to the same WebSocket URI, creating a package binding with the `uri` value is suggested.  With binding, you don't need to specify the value each time that you use the `send` action.

### Sending a message to a WebSocket

The `/whisk.system/websocket/send` action will send a payload to a WebSocket URI. The parameters are as follows:

- `uri`: The URI of the websocket server (e.g. ws://mywebsockethost:80)
- `payload`: The message you wish to send to the WebSocket
