# Using the Watson package

The `/whisk.system/watson` package offers a convenient way to call various Watson APIs.

The package includes the following actions.

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/watson` | package | username, password | Actions for the Watson analytics APIs |
| `/whisk.system/watson/translate` | action | translateFrom, translateTo, translateParam, username, password | Translate text |
| `/whisk.system/watson/languageId` | action | payload, username, password | Identify language |
| `/whisk.system/watson/speechToText` | action | payload, content_type, encoding, username, password, continuous, inactivity_timeout, interim_results, keywords, keywords_threshold, max_alternatives, model, timestamps, watson-token, word_alternatives_threshold, word_confidence, X-Watson-Learning-Opt-Out | Convert audio into text |
| `/whisk.system/watson/textToSpeech` | action | payload, voice, accept, encoding, username, password | Convert text into audio |

While not required, it's suggested that you create a package binding with the `username` and `password` values. This way you don't need to specify these credentials every time you invoke the actions in the package.

## Translating text

The `/whisk.system/watson/translate` action translate text from one language to another. The parameters are as follows:

- `username`: The Watson API username.
- `password`: The Watson API password.
- `translateParam`: The input parameter to translate. For example, if `translateParam=payload`, then the value of the `payload` parameter passed to the action is translated.
- `translateFrom`: A two digit code of the source language.
- `translateTo`: A two digit code of the target language.

The following is an example of creating a package binding and translating some text.

1. Create a package binding with your Watson credentials.

  ```
  $ wsk package bind /whisk.system/watson myWatson --param username 'MY_WATSON_USERNAME' --param password 'MY_WATSON_PASSWORD'
  ```

2. Invoke the `translate` action in your package binding to translate some text from English to French.

  ```
  $ wsk action invoke myWatson/translate --blocking --result --param payload 'Blue skies ahead' --param translateParam 'payload' --param translateFrom 'en' --param translateTo 'fr'
  ```

  ```
  {
      "payload": "Ciel bleu a venir"
  }
  ```


## Identifying the language of some text

The `/whisk.system/watson/languageId` action identifies the language of some text. The parameters are as follows:

- `username`: The Watson API username.
- `password`: The Watson API password.
- `payload`: The text to identify.

Here is an example of creating a package binding and identifying the language of some text.

1. Create a package binding with your Watson credentials.

  ```
  $ wsk package bind /whisk.system/watson myWatson -p username 'MY_WATSON_USERNAME' -p password 'MY_WATSON_PASSWORD'
  ```

2. Invoke the `languageId` action in your package binding to identify the language.

  ```
  $ wsk action invoke myWatson/languageId --blocking --result --param payload 'Ciel bleu a venir'
  ```
  ```
  {
    "payload": "Ciel bleu a venir",
    "language": "fr",
    "confidence": 0.710906
  }
  ```


## Converting some text to speech

The `/whisk.system/watson/textToSpeech` action converts some text into an audio speech. The parameters are as follows:

- `username`: The Watson API username.
- `password`: The Watson API password.
- `payload`: The text to convert into speech.
- `voice`: The voice of the speaker.
- `accept`: The format of the speech file.
- `encoding`: The encoding of the speech binary data.

Here is an example of creating a package binding and converting some text to speech.

1. Create a package binding with your Watson credentials.

  ```
  $ wsk package bind /whisk.system/watson myWatson -p username 'MY_WATSON_USERNAME' -p password 'MY_WATSON_PASSWORD'
  ```

2. Invoke the `textToSpeech` action in your package binding to convert the text.

  ```
  $ wsk action invoke myWatson/textToSpeech --blocking --result --param payload 'Hey.' --param voice 'en-US_MichaelVoice' --param accept 'audio/wav' --param encoding 'base64'
  ```
  ```
  {
    "payload": "<base64 encoding of a .wav file>"
  }
  ```


## Converting speech to text

The `/whisk.system/watson/speechToText` action converts audio speech into text. The parameters are as follows:

- `username`: The Watson API username.
- `password`: The Watson API password.
- `payload`: The encoded speech binary data to turn into text.
- `content_type`: The MIME type of the audio.
- `encoding`: The encoding of the speech binary data.
- `continuous`: Indicates whether multiple final results that represent consecutive phrases separated by long pauses are returned.
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
 
Here is an example of creating a package binding and converting speech to text.

1. Create a package binding with your Watson credentials.

  ```
  $ wsk package bind /whisk.system/watson myWatson -p username 'MY_WATSON_USERNAME' -p password 'MY_WATSON_PASSWORD'
  ```

2. Invoke the `speechToText` action in your package binding to convert the encoded audio.

  ```
  $ wsk action invoke myWatson/speechToText --blocking --result --param payload <base64 encoding of a .wav file> --param content_type 'audio/wav' --param encoding 'base64'
  ```
  ```
  {
    "data": "Hello Watson"
  }
  ```
