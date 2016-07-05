# openwhisk-catalog

This openwhisk-catalog directory is supposed to be put directly under the openwhisk home directory.
It requires that there is a whisk property file "whisk.properties" available under the openwhisk
home directory, and a system authentication file named "auth.whisk.system" available.
We should be able to run the script installCatalog.sh with the path of "auth.whisk.system"
as the first parameter and the openwhisk home directory as the second parameter to install
the catalog like:

```
./installCatalog.sh <PATH OF "auth.whisk.system"> <OPENWHISK_HOME>
```

The first parameter <PATH OF "auth.whisk.system"> is mandatory and the second parameter
<OPENWHISK_HOME> will be set to the parent directory of the openwhisk-catalog if it is not provided.
If openwhisk-catalog directory is put in the openwhisk home directory, we are able to omit the
second parameter. Otherwise, we need to specify the correct OPENWHISK_HOME either as the second
parameter or the environment variable to make it run.

Check if the travis ci works fine.