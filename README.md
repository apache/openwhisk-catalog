<!--
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
-->

# Openwhisk Catalog

[![Build Status](https://travis-ci.org/apache/incubator-openwhisk-catalog.svg?branch=master)](https://travis-ci.org/apache/incubator-openwhisk-catalog)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

This openwhisk-catalog maintains the package catalogs of openwhisk. In OpenWhisk, the catalog
of packages gives you an easy way to enhance your app with useful capabilities, and to access
external services in the ecosystem. Examples of external services that are OpenWhisk-enabled
include IBM Watson API, the Weather Company, JIRA, Slack, and GitHub.system packages and sample packages.

The catalog is available as packages in the `/whisk.system` namespace. See [Browsing packages](https://github.com/openwhisk/openwhisk/blob/master/docs/packages.md#browsing-packages)
for information about how to browse the catalog by using the command line tool.

## How to install openWhisk-catalog

### Pre-requisites
- [openwhisk](https://github.com/openwhisk/openwhisk/blob/master/README.md) is installed.
- Environment variable `OPENWHISK_HOME` is configured as the path to `openwhisk` source code directory.


### Install openwhisk-catalog

We should be able to run the script [packages/installCatalog.sh](packages/installCatalog.sh) to install the catalog like:

```
./packages/installCatalog.sh [catalog_auth_key] [api_host] [cli_path]
```

The first argument `catalog_auth_key`, defines the secret key used to authenticate the openwhisk
service. The second argument `api_host`, determines the location, where the openwhisk edge host is running,
in the format of IP or hostname. The third argument `cli_path` is the full path to the `wsk` cli executable.

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
| [/whisk.system/jira](./packages/jira/README.md) | offers a convenient way to use the [JIRA APIs](https://developer.atlassian.com/server/jira/platform/rest-apis/). |

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

## Disclaimer

Apache OpenWhisk Catalog is an effort undergoing incubation at The Apache Software Foundation (ASF), sponsored by the Apache Incubator. Incubation is required of all newly accepted projects until a further review indicates that the infrastructure, communications, and decision making process have stabilized in a manner consistent with other successful ASF projects. While incubation status is not necessarily a reflection of the completeness or stability of the code, it does indicate that the project has yet to be fully endorsed by the ASF.
