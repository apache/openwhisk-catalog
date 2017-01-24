# Contributing to OpenWhisk catalog

We welcome contributions, but request you to follow these guidelines.

 - [Raising issues](#raising-issues)
 - [Contributor License Agreement](#contributor-license-agreement)
 - [Coding Standards](#coding-standards)
 - [Contributing your own package](#contributing-your-own-package)

## Raising issues

Please submit bug reports on the issue tracker. Be sure to
search the list to see if your issue has already been raised.

A good bug report is one that makes it easy for us to understand what you were
trying to do and what went wrong. Provide as much context as possible so we can try to recreate the issue.

## Contributor License Agreement

In order for us to accept pull-requests, the contributor must first complete
a Contributor License Agreement (CLA). This clarifies the intellectual
property license granted with any contribution. It is for your protection as a
Contributor as well as the protection of IBM and its customers; it does not
change your rights to use your own Contributions for any other purpose.

You can download the CLAs here:

- [individual](https://github.com/openwhisk/openwhisk/blob/master/CLA-INDIVIDUAL.md)
- [corporate](https://github.com/openwhisk/openwhisk/blob/master/CLA-CORPORATE.md)

### Coding standards

Please ensure you follow the coding standards used throughout the existing
code base. Some basic rules include:

 - all files must have the Apache license in the header.
 - all PRs must have passing builds for all operating systems.


## Contributing your own package

If you want to contribute your own package to openwhisk-catalog, you need to have the following files ready:
- source codes to be put under the folder `openwhisk-catalog/packages`. You can create a folder with a meaningful name to maintain your package files.
- test files to be put under the folder `openwhisk-catalog/tests/src`. You can create a folder with the same name as you created in package to maintain your package test files.
- `README.md` to describe the actions/feeds in your package and how to use them.
- `installYOURPKG.sh` to install your package. `YOURPKG` can be the name of your package.
- Add the invocation of your package install script to `installCatalog.sh`.

### How to write the tests for your package

You must provide the test codes for your package. OpenWhisk catalog follows the same coding style of OpenWhisk. OpenWhisk recommends to use [scalatest](http://www.scalatest.org) in the test codes.

#### Base classes for test

OpenWhisk has defined the base classes to formalize the test codes in the folder of openwhisk/tests/src/common. Generally, your tests should extend following classes:
- [TestHelpers](https://github.com/openwhisk/openwhisk/blob/master/tests/src/common/TestHelpers.scala) is the abstract base class to define the common features OpenWhisk uses most: FlatSpec and BeforeAndAfterEachTestData.
- [WskTestHelpers](https://github.com/openwhisk/openwhisk/blob/master/tests/src/common/WskTestHelpers.scala) is a test fixture to ease cleaning of whisk entities created during testing. By using functions `withActivation` and `withAssetCleaner` in the test codes, you don't need to care about the entities you create during test.

You may want to use the following utilities to ease the unit test programing.
- [Wsk](https://github.com/openwhisk/openwhisk/blob/master/tests/src/common/Wsk.scala) provides Scala bindings for the whisk CLI. It defines utility classes for top level CLI commands, e.g. `WskAction` for `wsk action`, `WskTrigger` for `wsk trigger` and etc, including some methods for the easily invocation. For example, if you want to invoke an action, you can simply implement by the following codes:
```
val wsk = new Wsk(usePythonCLI=true)
val greetingAction = "/whisk.system/samples/greeting"
val run = wsk.action.invoke(greetingAction, Map("name" -> "Mork".toJson))
```
#### Naming conventions for test

There are some naming conventions to follow:
- The package name of your test codes should be the same as your package. For example, if your package name is "HelloWorld", you should create the same folder under `tests/src/packages` to contain your test codes.
- You can create a Scala class for one action, with the name of `action name`+"Tests". For example, if the action name is "SayHi" in a package "HelloWorld", you should create a Scala class named "SayHiTests" under folder `tests/src/packages/HelloWorld`.

#### What should be tested

For every action, trigger and feed, there should be:
- at least 1 test method to test a success simple invocation.
- at least 1 test method to test a passing parameters case if it accepts parameters.
- at least 1 test method to test a failure invocation if it can handle exceptions and failures.

### How to make sure all tests are passed

Before contributing your package, you must make sure all the tests in tests folder are passed.

For Mac or Linux users:
Open the terminal, and go the directory of the openWhisk-catalog. Then run all the test cases with command
```
./gradlew :tests:test
```

If you would like to test a single test cases, you can either run 
```
./gradlew -Dtest.single=*<name of the test file>* tests:test
```

or 
```
./gradlew :tests:test --tests <package name>
```
.

For Windows users:
Please replace `gradlew` with `gradlew.bat` for the commands in above paragraphs.

Please note openwhisk-catalog have dependencies on openwhisk core projects and tests. In order to run the tests, you must make sure openwhisk repository is cloned and the environment variable `OPENWHISK_HOME` is configured.

### Reference documents

- [How to use and create OpenWhisk packages](https://github.com/openwhisk/openwhisk/blob/master/docs/packages.md)
- [How to create and invoke OpenWhisk actions](https://github.com/openwhisk/openwhisk/blob/master/docs/actions.md)
- [openwhisk-package-template](https://github.com/openwhisk/openwhisk-package-template) is the package template you may refer to.
