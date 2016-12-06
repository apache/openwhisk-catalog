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
| `/whisk.system/watson-translator` | offers a convenient way to call Watson APIs to translate.|
| `/whisk.system/watson-speechToText` | offers a convenient way to call Watson APIs to convert the speech into text.|
| `/whisk.system/watson-textToSpeech` | offers a convenient way to call Watson APIs to convert the text into speech.|
| `/whisk.system/weather` | offers a convenient way to call the IBM Weather Insights API.|

## How to create packages

If you want to create your own packages,  [openwhisk-package-template](https://github.com/openwhisk/openwhisk-package-template)
is a good package creation template to help you build, test and integrate new packages.

For more details about how to use these packages, you can go to the README.md under each package folder.

## Using the Weather package

The `/whisk.system/weather` package offers a convenient way to call the Weather Company Data for IBM Bluemix API.

The package includes the following action.

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/weather` | package | username, password | Services from the Weather Company Data for IBM Bluemix API  |
| `/whisk.system/weather/forecast` | action | latitude, longitude, timePeriod | forecast for specified time period|

Creating a package binding with the `username` and `password` values is suggested. This way, you don't need to specify the credentials every time you invoke the actions in the package.

### Getting a weather forecast for a location

The `/whisk.system/weather/forecast` action returns a weather forecast for a location by calling an API from The Weather Company. The parameters are as follows:

- `username`: Username for The Weather Company Data for IBM Bluemix that is entitled to invoke the forecast API.
- `password`: Password for The Weather Company Data for IBM Bluemix that is entitled to invoke the forecast API.
- `latitude`: The latitude coordinate of the location.
- `longitude`: The longitude coordinate of the location.
- `timeperiod`: Time period for the forecast. Valid options are '10day' - (default) Returns a daily 10-day forecast , '48hour' - Returns an hourly 2-day forecast, , 'current' - Returns the current weather conditions, 'timeseries' - Returns both the current observations and up to 24 hours of past observations, from the current date and time.


The following is an example of creating a package binding and then getting a 10-day forecast.

1. Create a package binding with your API key.

  ```
  $ wsk package bind /whisk.system/weather myWeather --param username 'MY_USERNAME' --param password 'MY_PASSWORD'
  ```

2. Invoke the `forecast` action in your package binding to get the weather forecast.

  ```
  $ wsk action invoke myWeather/forecast --blocking --result --param latitude '43.7' --param longitude '-79.4'
  ```

  ```
  {
      "forecasts": [
          {
              "dow": "Wednesday",
              "max_temp": -1,
              "min_temp": -16,
              "narrative": "Chance of a few snow showers. Highs -2 to 0C and lows -17 to -15C.",
              ...
          },
          {
              "class": "fod_long_range_daily",
              "dow": "Thursday",
              "max_temp": -4,
              "min_temp": -8,
              "narrative": "Mostly sunny. Highs -5 to -3C and lows -9 to -7C.",
              ...
          },
          ...
      ],
  }
  ```

## Using the Watson packages

The Watson packages offer a convenient way to call various Watson APIs.

The following Watson packages are provided:

| Package | Description |
| --- | --- |
| `/whisk.system/watson-translator`   | Actions for the Watson APIs to translate text and language identification |
| `/whisk.system/watson-textToSpeech` | Actions for the Watson APIs to convert the text into speech |
| `/whisk.system/watson-speechToText` | Actions for the Watson APIs to convert the speech into text |

**Note** The package `/whisk.system/watson` is currently deprecated, migrate to the new packages mentioned above, the new actions provide the same interface.

### Using the Watson Translator package

The `/whisk.system/watson-translator` package offers a convenient way to call Watson APIs to translate.

The package includes the following actions.

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/watson-translator` | package | username, password | Actions for the Watson APIs to translate text and language identification |
| `/whisk.system/watson-translator/translator` | action | payload, translateFrom, translateTo, translateParam, username, password | Translate text |
| `/whisk.system/watson-translator/languageId` | action | payload, username, password | Identify language |

**Note**: The package `/whisk.system/watson` is depracted including the actions `/whisk.system/watson/translate` and `/whisk.system/watson/languageId`.

#### Setting up the Watson Translator package in Bluemix

If you're using OpenWhisk from Bluemix, OpenWhisk automatically creates package bindings for your Bluemix Watson service instances.

1. Create a Watson Translator service instance in your Bluemix [dashboard](http://console.ng.Bluemix.net).

  Be sure to remember the name of the service instance and the Bluemix organization and space you're in.

2. Make sure your OpenWhisk CLI is in the namespace corresponding to the Bluemix organization and space that you used in the previous step.

  ```
  $ wsk property set --namespace myBluemixOrg_myBluemixSpace
  ```

  Alternatively, you can use `wsk property set --namespace` to set a namespace from a list of those accessible to you.

3. Refresh the packages in your namespace. The refresh automatically creates a package binding for the Watson service instance that you created.

  ```
  $ wsk package refresh
  ```
  ```
  created bindings:
  Bluemix_Watson_Translator_Credentials-1
  ```

  ```
  $ wsk package list
  ```
  ```
  packages
  /myBluemixOrg_myBluemixSpace/Bluemix_Watson_Translator_Credentials-1 private
  ```


#### Setting up a Watson Translator package outside Bluemix

If you're not using OpenWhisk in Bluemix or if you want to set up your Watson Translator outside of Bluemix, you must manually create a package binding for your Watson Translator service. You need the Watson Translator service user name, and password.

- Create a package binding that is configured for your Watson Translator service.

  ```
  $ wsk package bind /whisk.system/watson-translator myWatsonTranslator -p username MYUSERNAME -p password MYPASSWORD
  ```


#### Translating text

The `/whisk.system/watson-translator/translator` action translates text from one language to another. The parameters are as follows:

- `username`: The Watson API user name.
- `password`: The Watson API password.
- `payload`: The text to be translated.
- `translateParam`: The input parameter indicating the text to translate. For example, if `translateParam=payload`, then the value of the `payload` parameter that is passed to the action is translated.
- `translateFrom`: A two-digit code of the source language.
- `translateTo`: A two-digit code of the target language.

- Invoke the `translator` action in your package binding to translate some text from English to French.

  ```
  $ wsk action invoke myWatsonTranslator/translator --blocking --result --param payload 'Blue skies ahead' --param translateFrom 'en' --param translateTo 'fr'
  ```

  ```
  {
      "payload": "Ciel bleu a venir"
  }
  ```


#### Identifying the language of some text

The `/whisk.system/watson-translator/languageId` action identifies the language of some text. The parameters are as follows:

- `username`: The Watson API user name.
- `password`: The Watson API password.
- `payload`: The text to identify.

- Invoke the `languageId` action in your package binding to identify the language.

  ```
  $ wsk action invoke myWatsonTranslator/languageId --blocking --result --param payload 'Ciel bleu a venir'
  ```
  ```
  {
    "payload": "Ciel bleu a venir",
    "language": "fr",
    "confidence": 0.710906
  }
  ```


### Using the Watson Text to Speech package

The `/whisk.system/watson-textToSpeech` package offers a convenient way to call Watson APIs to convert the text into speech.


The package includes the following actions.

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/watson-textToSpeech` | package | username, password | Actions for the Watson APIs to convert the text into speech |
| `/whisk.system/watson-textToSpeech/textToSpeech` | action | payload, voice, accept, encoding, username, password | Convert text into audio |

**Note**: The package `/whisk.system/watson` is depracted including the action `/whisk.system/watson/textToSpeech`.

#### Setting up the Watson Text to Speech package in Bluemix

If you're using OpenWhisk from Bluemix, OpenWhisk automatically creates package bindings for your Bluemix Watson service instances.

1. Create a Watson Text to Speech service instance in your Bluemix [dashboard](http://console.ng.Bluemix.net).

  Be sure to remember the name of the service instance and the Bluemix organization and space you're in.

2. Make sure your OpenWhisk CLI is in the namespace corresponding to the Bluemix organization and space that you used in the previous step.

  ```
  $ wsk property set --namespace myBluemixOrg_myBluemixSpace
  ```

  Alternatively, you can use `wsk property set --namespace` to set a namespace from a list of those accessible to you.

3. Refresh the packages in your namespace. The refresh automatically creates a package binding for the Watson service instance that you created.

  ```
  $ wsk package refresh
  ```
  ```
  created bindings:
  Bluemix_Watson_TextToSpeech_Credentials-1
  ```

  ```
  $ wsk package list
  ```
  ```
  packages
  /myBluemixOrg_myBluemixSpace/Bluemix_Watson_TextToSpeec_Credentials-1 private
  ```


#### Setting up a Watson Text to Speech package outside Bluemix

If you're not using OpenWhisk in Bluemix or if you want to set up your Watson Text to Speech outside of Bluemix, you must manually create a package binding for your Watson Text to Speech service. You need the Watson Text to Speech service user name, and password.

- Create a package binding that is configured for your Watson Speech to Text service.

  ```
  $ wsk package bind /whisk.system/watson-speechToText myWatsonTextToSpeech -p username MYUSERNAME -p password MYPASSWORD
  ```


#### Converting some text to speech

The `/whisk.system/watson-speechToText/textToSpeech` action converts some text into an audio speech. The parameters are as follows:

- `username`: The Watson API user name.
- `password`: The Watson API password.
- `payload`: The text to convert into speech.
- `voice`: The voice of the speaker.
- `accept`: The format of the speech file.
- `encoding`: The encoding of the speech binary data.


- Invoke the `textToSpeech` action in your package binding to convert the text.

  ```
  $ wsk action invoke myWatsonTextToSpeech/textToSpeech --blocking --result --param payload 'Hey.' --param voice 'en-US_MichaelVoice' --param accept 'audio/wav' --param encoding 'base64'
  ```
  ```
  {
    "payload": "<base64 encoding of a .wav file>"
  }
  ```


### Using the Watson Speech to Text package

The `/whisk.system/watson-speechToText` package offers a convenient way to call Watson APIs to convert the speech into text.

The package includes the following actions.

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/watson-speechToText` | package | username, password | Actions for the Watson APIs to convert the speech into text |
| `/whisk.system/watson-speechToText/speechToText` | action | payload, content_type, encoding, username, password, continuous, inactivity_timeout, interim_results, keywords, keywords_threshold, max_alternatives, model, timestamps, watson-token, word_alternatives_threshold, word_confidence, X-Watson-Learning-Opt-Out | Convert audio into text |

**Note**: The package `/whisk.system/watson` is depracted including the action `/whisk.system/watson/speechToText`.

#### Setting up the Watson Speech to Text package in Bluemix

If you're using OpenWhisk from Bluemix, OpenWhisk automatically creates package bindings for your Bluemix Watson service instances.

1. Create a Watson Speech to Text service instance in your Bluemix [dashboard](http://console.ng.Bluemix.net).

  Be sure to remember the name of the service instance and the Bluemix organization and space you're in.

2. Make sure your OpenWhisk CLI is in the namespace corresponding to the Bluemix organization and space that you used in the previous step.

  ```
  $ wsk property set --namespace myBluemixOrg_myBluemixSpace
  ```

  Alternatively, you can use `wsk property set --namespace` to set a namespace from a list of those accessible to you.

3. Refresh the packages in your namespace. The refresh automatically creates a package binding for the Watson service instance that you created.

  ```
  $ wsk package refresh
  ```
  ```
  created bindings:
  Bluemix_Watson_SpeechToText_Credentials-1
  ```

  ```
  $ wsk package list
  ```
  ```
  packages
  /myBluemixOrg_myBluemixSpace/Bluemix_Watson_SpeechToText_Credentials-1 private
  ```


#### Setting up a Watson Speech to Text package outside Bluemix

If you're not using OpenWhisk in Bluemix or if you want to set up your Watson Speech to Text outside of Bluemix, you must manually create a package binding for your Watson Speech to Text service. You need the Watson Speech to Text service user name, and password.

- Create a package binding that is configured for your Watson Speech to Text service.

  ```
  $ wsk package bind /whisk.system/watson-speechToText myWatsonSpeechToText -p username MYUSERNAME -p password MYPASSWORD
  ```


#### Converting speech to text

The `/whisk.system/watson-speechToText/speechToText` action converts audio speech into text. The parameters are as follows:

- `username`: The Watson API user name.
- `password`: The Watson API password.
- `payload`: The encoded speech binary data to turn into text.
- `content_type`: The MIME type of the audio.
- `encoding`: The encoding of the speech binary data.
- `continuous`: Indicates whether multiple final results that represent consecutive phrases that are separated by long pauses are returned.
- `inactivity_timeout`: The time in seconds after which, if only silence is detected in submitted audio, the connection is closed.
- `interim_results`: Indicates whether the service is to return interim results.
- `keywords`: A list of keywords to spot in the audio.
- `keywords_threshold`: A confidence value that is the lower bound for spotting a keyword.
- `max_alternatives`: The maximum number of alternative transcripts to be returned.
- `model`: The identifier of the model to be used for the recognition request.
- `timestamps`: Indicates whether time alignment is returned for each word.
- `watson-token`: Provides an authentication token for the service as an alternative to providing service credentials.
- `word_alternatives_threshold`: A confidence value that is the lower bound for identifying a hypothesis as a possible word alternative.
- `word_confidence`: Indicates whether a confidence measure in the range of 0 to 1 is to be returned for each word.
- `X-Watson-Learning-Opt-Out`: Indicates whether to opt out of data collection for the call.
 

- Invoke the `speechToText` action in your package binding to convert the encoded audio.

  ```
  $ wsk action invoke myWatsonSpeechToText/speechToText --blocking --result --param payload <base64 encoding of a .wav file> --param content_type 'audio/wav' --param encoding 'base64'
  ```
  ```
  {
    "data": "Hello Watson"
  }
  ```
  

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
