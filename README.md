# Openwhisk Catalog

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
./installCatalog.sh
```

## Existing packages in catalog

| Package | Description |
| --- | --- |
| `/whisk.system/github` | offers a convenient way to use the [GitHub APIs](https://developer.github.com/). |
| `/whisk.system/samples` | offers sample actions in different languages |
| `/whisk.system/slack` | offers a convenient way to use the [Slack APIs](https://api.slack.com/). |
| `/whisk.system/utils` | offers utilities actions such as cat, echo, and etc. |
| `/whisk.system/watson` | offers a convenient way to call various Watson APIs.|
| `/whisk.system/weather` | offers a convenient way to call the IBM Weather Insights API.|

For more details about how to use these packages, you can go to the README.md under each package folder.
