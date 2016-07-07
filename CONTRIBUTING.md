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
