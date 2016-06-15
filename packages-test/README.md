# openwhisk-catalog test

In order to make sure all the packages are moved in a correct way, we created all the test
scripts in this directory. Each script has one corresponding test script. When all the packages
have successfully moved into openwhisk-catalog, this test folder can be removed if necessary.

Initially all the test scripts will fail, since there are packages' relocations on their way.
After this patch, each pull request should run the designated test with no error to make sure
the package is moved in a correct way. For example, if you are working on the movement of utils,
the UtilTest.sh should run with no error; if you are working on the movement of github, you need
to make sure installGitTest.sh is running fine.

1. Before running each script, please make sure that the ${OPENWHISK_HOME} directory is in the
same directory as openwhisk-catalog is. The script "installCatalogTest.sh" is supposed to
test all the scripts, while each of the rest scripts can run individually as well. 
2. In addition, please make sure that there is a valid file whisk.properties under openwhisk home and a valid file auth.whisk.system under ${OPENWHISK_HOME}/config/keys/ with authorization key. You need to specify a valid key "edge.host" in whisk.properties. The edge host is meant to be the machine, where the service is installed. 
3. Don't forget to give all the permissions to all the scripts and their test scripts by
"(sudo) chmod 777 <script_file>".

