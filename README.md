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
| `/whisk.system/github` | offers a convenient way to use the [GitHub APIs](https://developer.github.com/). |
| `/whisk.system/samples` | offers sample actions in different languages |
| `/whisk.system/slack` | offers a convenient way to use the [Slack APIs](https://api.slack.com/). |
| `/whisk.system/utils` | offers utilities actions such as cat, echo, and etc. |
| [/whisk.system/watson-translator](./packages/watson-translator/README.md) | Package for text translation and language identification|
| [/whisk.system/watson-speechToText](./packages/watson-speechToText/README.md) | Package to convert speech into text|
| [/whisk.system/watson-textToSpeech](./packages/watson-textToSpeech/README.md) | Package to convert text into speech|
| [/whisk.system/weather](./packages/weather/README.md) | Services from the Weather Company Data for IBM Bluemix API|

## How to create packages

If you want to create your own packages,  [openwhisk-package-template](https://github.com/openwhisk/openwhisk-package-template)
is a good package creation template to help you build, test and integrate new packages.

For more details about how to use these packages, you can go to the README.md under each package folder.


## Using the Slack package

The `/whisk.system/slack` package offers a convenient way to use the [Slack APIs](https://api.slack.com/).

The package includes the following actions:

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/slack` | package | url, channel, username | Interact with the Slack API |
| `/whisk.system/slack/post` | action | text, url, channel, username | Posts a message to a Slack channel |

Creating a package binding with the `username`, `url`, and `channel` values is suggested. With binding, you don't need to specify the values each time that you invoke the action in the package.

### Posting a message to a Slack channel

The `/whisk.system/slack/post` action posts a message to a specified Slack channel. The parameters are as follows:

- `url`: The Slack webhook URL.
- `channel`: The Slack channel to post the message to.
- `username`: The name to post the message as.
- `text`: A message to post.
- `token`: (optional) A Slack [access token](https://api.slack.com/tokens). See [below](./catalog.md#using-the-slack-token-based-api) for more detail on the use of the Slack access tokens.

The following is an example of configuring Slack, creating a package binding, and posting a message to a channel.

1. Configure a Slack [incoming webhook](https://api.slack.com/incoming-webhooks) for your team.

  After Slack is configured, you get a webhook URL that looks like `https://hooks.slack.com/services/aaaaaaaaa/bbbbbbbbb/cccccccccccccccccccccccc`. You'll need this in the next step.

2. Create a package binding with your Slack credentials, the channel that you want to post to, and the user name to post as.

  ```
  $ wsk package bind /whisk.system/slack mySlack --param url 'https://hooks.slack.com/services/...' --param username 'Bob' --param channel '#MySlackChannel'
  ```

3. Invoke the `post` action in your package binding to post a message to your Slack channel.

  ```
  $ wsk action invoke mySlack/post --blocking --result --param text 'Hello from OpenWhisk!'
  ```

### Using the Slack token-based API

If you prefer, you may optionally choose to use Slack's token-based API, rather than the webhook API. If you so choose, then pass in a `token` parameter that contains your Slack [access token](https://api.slack.com/tokens). You may then use any of the [Slack API methods](https://api.slack.com/methods) as your `url` parameter. For example, to post a message, you would use a `url` parameter value of [slack.postMessage](https://api.slack.com/methods/chat.postMessage).

## Using the GitHub package

The `/whisk.system/github` package offers a convenient way to use the [GitHub APIs](https://developer.github.com/).

The package includes the following feed:

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/github` | package | username, repository, accessToken | Interact with the GitHub API |
| `/whisk.system/github/webhook` | feed | events, username, repository, accessToken | Fire trigger events on GitHub activity |

Creating a package binding with the `username`, `repository`, and `accessToken` values is suggested.  With binding, you don't need to specify the values each time that you use the feed in the package.

### Firing a trigger event with GitHub activity

The `/whisk.system/github/webhook` feed configures a service to fire a trigger when there is activity in a specified GitHub repository. The parameters are as follows:

- `username`: The user name of the GitHub repository.
- `repository`: The GitHub repository.
- `accessToken`: Your GitHub personal access token. When you [create your token](https://github.com/settings/tokens), be sure to select the repo:status and public_repo scopes. Also, make sure that you don't have any webhooks already defined for your repository.
- `events`: The [GitHub event type](https://developer.github.com/v3/activity/events/types/) of interest.

The following is an example of creating a trigger that will be fired each time that there is a new commit to a GitHub repository.

1. Generate a GitHub [personal access token](https://github.com/settings/tokens).

  The access token will be used in the next step.

2. Create a package binding that is configured for your GitHub repository and with your access token.

  ```
  $ wsk package bind /whisk.system/github myGit --param username myGitUser --param repository myGitRepo --param accessToken aaaaa1111a1a1a1a1a111111aaaaaa1111aa1a1a
  ```

3. Create a trigger for the GitHub `push` event type by using your `myGit/webhook` feed.

  ```
  $ wsk trigger create myGitTrigger --feed myGit/webhook --param events push
  ```

A commit to the GitHub repository by using a `git push` causes the trigger to be fired by the webhook. If there is a rule that matches the trigger, then the associated action will be invoked.
The action receives the GitHub webhook payload as an input parameter. Each GitHub webhook event has a similar JSON schema, but is a unique payload object that is determined by its event type.
For more information about the payload content, see the [GitHub events and payload](https://developer.github.com/v3/activity/events/types/) API documentation.


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
