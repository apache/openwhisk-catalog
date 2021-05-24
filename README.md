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

[![Build Status](https://travis-ci.com/apache/openwhisk-catalog.svg?branch=master)](https://travis-ci.com/apache/openwhisk-catalog)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

This openwhisk-catalog maintains the package catalogs of openwhisk. In OpenWhisk, the catalog
of packages gives you an easy way to enhance your app with useful capabilities, and to access
external services in the ecosystem. Examples of external services that are OpenWhisk-enabled
include the Slack and GitHub.system packages and sample packages.

The catalog is available as packages in the `/whisk.system` namespace. See [Browsing packages](https://github.com/openwhisk/openwhisk/blob/master/docs/packages.md#browsing-packages)
for information about how to browse the catalog by using the command line tool.

## How to install openWhisk-catalog

### Pre-requisites
- [openwhisk](https://github.com/openwhisk/openwhisk/blob/master/README.md) is installed.
- Environment variable `OPENWHISK_HOME` is configured as the path to `openwhisk` source code directory.


### Install openwhisk-catalog

We should be able to run the script [packages/installCatalogUsingWskdeploy.sh](packages/installCatalogUsingWskdeploy.sh) to install the catalog like:

```
./packages/installCatalogUsingWskdeploy.sh [catalog_auth_key] [api_host] [cli_path]
```

The first argument `catalog_auth_key`, defines the secret key used to authenticate the openwhisk
service. The second argument `api_host`, determines the location, where the openwhisk edge host is running,
in the format of IP or hostname. The third argument `cli_path` is the full path to the `wsk` cli executable.

## Existing packages in catalog

For more details about how to use packages in the catalog, you can go to the README.md under each package subfolder.

| Package | Description |
| --- | --- |
| [/whisk.system/github](./packages/github/README.md) | offers a convenient way to use the [GitHub APIs](https://developer.github.com/). |
| [/whisk.system/slack](./packages/slack/README.md) | offers a convenient way to use the [Slack APIs](https://api.slack.com/). |
| [/whisk.system/websocket](./packages/websocket/README.md) | Package to send messages to Web Socket server|

<!--
TODO: place holder until we have a README for samples
| [/whisk.system/samples](./packages/samples/README.md) | offers sample actions in different programming languages |
-->
<!--
TODO: place holder until we have a README for utils
| [/whisk.system/utils](./packages/utils/README.md) | offers utilities actions such as cat, echo, and etc. |
-->

## How to create top-level packages

If your package is more involved you may want to create your own top-level packages repository using the [openwhisk-package-template](https://github.com/openwhisk/openwhisk-package-template). It will provide a good example on what a good template should include (i.e., sufficient help build, test and integrate into OpenWhisk).
