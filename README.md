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

# About the relationship of OpenWhisk and OpenWhisk-Catalog

All the packages under OpenWhisk-Catalog originates from the catalog directory under OpenWhisk. They contain
the actions to be installed in OpenWhisk. Since OpenWhisk will become the project which only hosts the core
components, all the packages will be moved into OpenWhisk-Catalog for a better distributed structure.

OpenWhisk-Catalog depends on OpenWhisk installation. All the test cases here depends on the OpenWhisk test
dependencies as well. For all the test cases, the build.gradle file for tests in OpenWhisk-Catalog will
add the test dependencies from OpenWhisk.

# How to install OpenWhisk-Catalog

If you have already installed openwhisk:
Make sure an environment variable <OPENWHISK_HOME> is correctly set for your machine.
Open a terminal, and go to the directory of openwhisk-catalog/packages. Run the command
"./installCatalog.sh <OPENWHISK_HOME>/config/keys/auth.whisk.system".

If you have not installed openwhisk:
Currently, this is only available in Mac and Linux environments.

Open a terminal, and go to the directory of openwhisk-catalog. Run the command
"./tools/travis/setup.sh" to install all the dependencies, including cloning the OpenWhisk repository, which
will be copied in the same directory as openwhisk-catalog is located. Then make sure an environment
variable <OPENWHISK_HOME> is correctly set for your machine.

Then run the command "./tools/travis/build.sh" to install all the packages in openwhisk-catalog and run all
the test cases.

# How to run the test cases in OpenWhisk-Catalog

For Mac or Linux users:
Open the terminal, and go the directory of the OpenWhisk-Catalog. Then run the command "./gradlew :tests:test"
to run all the test cases.

If you would like to test a single test cases, you can either run "./gradlew -Dtest.single=*<name of the test file>* tests:test"
or "./gradlew :tests:test --tests <package name>".

For Windows users:
Please replace 'gradlew' with 'gradlew.bat' for the commands in above paragraphs.

>>>>>>> 253a937... Update readme with more instructions on openwhisk-catalog
