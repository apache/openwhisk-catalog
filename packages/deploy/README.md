# Using the Deploy Package

The `/whisk.system/deploy` package offers a convenient way for you to describe and deploy any part of the OpenWhisk programming model using a Manifest file written in YAML.

The package includes the following actions.

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/deploy` | package | envData | Package to deploy OpenWhisk programming model elements |
| `/whisk.system/deploy/wskdeploy` | action | repo, manifestPath, envData | Deploy from github repositories with the appropriate structure and a defining manifest. |

## Setting up your Repository

A simple hello world example of a deployable github repository can be found [here](https://github.com/ibm-functions/blueprint-hello-world/).

A more complex example of a deployale github repository, including a trigger, a sequence, and cloudant credentials  can be found [here](https://github.com/ibm-functions/blueprint-cloudant-trigger).

1. Create a github repository with the following structure and naming conventions:


* runtimes
  * node
      * actions
          * my\_action\_name.js
      * manifest.yaml
  * python
      * actions
          * my\_action\_name.js
      * manifest.yaml
  * php
      * actions
          * my\_action\_name.js
      * manifest.yaml
  * swift
      * actions
          * my\_action\_name.js
      * manifest.yaml

It is not required to have every language, but you must have at least one.

2. Please see the above referenced repositories for samples of the manifest.yaml.  The manifest.yaml describes the OpenWhisk elements to be created.  There is a great guide for writing manifests [here](https://github.com/apache/incubator-openwhisk-wskdeploy/blob/master/docs/programming_guide.md#wskdeploy-utility-by-example).


## Run the wskdeploy command

With the repository created, you can now deploy from it.

- For the most simple manifests, with no associated services you can run the command with a repo parameter and a manifestPath parameter which tells wskdeploy which language you want from your project.

  ```
  wsk action invoke /whisk.system/deploy/wskdeploy
  -p repo https://github.com/ibm-functions/blueprint-hello-world/
  -p manifestPath "runtimes/node"
  ```

## Create a package binding and then run the wskdeploy command

- For more complex manifests with associated services you will need to provide an envData variable with the required information.  You can create a package binding that is configured with your service information.

  ```
  wsk package bind /whisk.system/deploy myDeploy -p envData 
  "{"CLOUDANT_USERNAME":"username",
  "CLOUDANT_PASSWORD":"password",
   "CLOUDANT_HOSTNAME":"hostname",
   "CLOUDANT_DATABASE":"database_name"}"
  ```

- Once your package binding is configured with your service information, you can invoke it with the repo and manifestPath parameters.

  ```
  wsk action invoke myDeploy/wskdeploy
  -p repo https://github.com/ibm-functions/blueprint-hello-world/
  -p manifestPath "runtimes/node"
  ```
