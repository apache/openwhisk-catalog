const fs = require('fs');
const exec = require('child_process').exec;
const git = require('simple-git');
const yaml = require('js-yaml');

let command = '';

function main(params) {
  return new Promise((resolve, reject) => {
    // Either build the remote URL for simple-git or build error
    const remoteOrError = convertParamsToRemote(params);

    // We received an error, reject with it
    if (typeof remoteOrError !== 'string') {
      const { error } = remoteOrError;
      reject(error);
    }

    const remote = remoteOrError;

    // Grab optional envData and deployPath params for wskdeploy
    const {
      envData,
      manifestPath,
    } = params;

    // Grab wsp api host and auth from params, or process.env
    const { wskApiHost, wskAuth } = getWskApiAuth(params);

    // Extract the name of the repo for the tmp directory
    const repoSplit = params.repo.split('/');
    const repoName = repoSplit[repoSplit.length - 1];
    const localDirName = `${__dirname}/tmp/${repoName}`;

    return checkIfDirExists(localDirName)
      .then((res) => {
        // The directory does not exist, clone BP from Github
        if (!res.skipClone) {
          return git()
            .clone(remote, localDirName, (err) => {
              if (err) {
                reject('There was a problem cloning from github.', err);
              }
              resolve({
                repoDir: localDirName,
                manifestPath,
                manifestFileName: 'manifest.yaml',
                wskAuth,
                wskApiHost,
                envData,
              });
            });
        } else {
          // The directory exists already, check if there is anything new
          //  and pull if so
          return git(localDirName)
            .pull((err, update) => {
              if (err) {
                reject('Error pulling most recent data ', err);
              }
              resolve({
                repoDir: localDirName,
                manifestPath,
                manifestFileName: 'manifest.yaml',
                wskAuth,
                wskApiHost,
                envData,
              });
            });
        }
      });
  })
  // @TODO: Uncomment and fix when we figure out the new way to create a .wskprops file
  .then((data) => {
    console.log('Creating config file for wskdeploy');
    const {
      wskAuth,
      wskApiHost,
    } = data;

    // Create a .wskprops in the root for wskdeploy to reference
    command = `echo "AUTH=${wskAuth}\nAPIHOST=${wskApiHost}\nNAMESPACE=_" > .wskprops`;

    return new Promise((resolve, reject) => {
      exec(command, { cwd: `/root/` }, (err, stdout, stderr) => {
        if (err) {
          reject('Error creating .wskdeploy props', err);
        }
        if (stdout) {
          console.log('stdout from creating .wskdeploy props:');
          console.log(stdout);
          console.log('type');
          console.log(typeof stdout);
        }
        if (stderr) {
          console.log('stderr from creating .wskdeploy.yaml props:');
          console.log(stderr);
        }
        resolve(data);
      });
    });
  })
  .then((data) => {
    const {
      manifestPath,
      repoDir,
      envData,
      manifestFileName
    } = data;

    return new Promise((resolve, reject) => {
      // Check if we need to rename the package in the manifest.yaml
      if (envData && envData.PACKAGE_NAME) {
        fs.readFile(`${repoDir}/${manifestPath}/${manifestFileName}`, (err, manifestFileData) => {
          if (err) {
            reject(`Error loading ${manifestFileName} to edit the package name:`, err);
          }

          try {
            // Load the manifest.yaml content and overwrite the name
            const manifestYamlJSON = yaml.safeLoad(manifestFileData);
            manifestYamlJSON.package.name = envData.PACKAGE_NAME;

            fs.writeFile(`${repoDir}/${manifestPath}/manifest-changed-name.yaml`, yaml.safeDump(manifestYamlJSON), (error) => {
              if (error) {
                reject('Error saving new manifest.yaml file', error);
              }

              // Change the manifestFileName so we read the updated manifest
              //  This helps in the case where one user wants to use a changed name
              //  and then wants to use the normal name, but the invoker isn't fresh
              //  and would accidentally use the overwritten manifest with the new name
              data.manifestFileName = 'manifest-changed-name.yaml';
              resolve(data);
            });
          } catch (e) {
            reject('Error converting manifest.yaml to JSON', e);
          }
        });
      } else {
        // Not trying to rename package, continue as normal
        data.envData = {};
        resolve(data);
      }
    });
  })
  .then((data) => {
    const {
      manifestPath,
      manifestFileName,
      repoDir,
      envData,
    } = data;

    // Set the cwd of the command to be where the manifest/actions live
    const execOptions = {
      cwd: `${repoDir}/${manifestPath}`,
    };

    // If we were passed environment data (Cloudant bindings, etc.) add it to the options for `exec`
    if (envData) {
      execOptions.env = envData;
    }

    // Send 'y' to the wskdeploy command so it will actually run the deployment
    command = `printf 'y' | ${__dirname}/wskdeploy -v -m ${manifestFileName} --config /root/.wskprops`;

    return new Promise((resolve, reject) => {
      if (fs.existsSync(`${repoDir}/${manifestPath}/${manifestFileName}`)) {
        exec(command, execOptions, (err, stdout, stderr) => {
          if (err) {
            reject('Error running `./wskdeploy`: ', err);
          }
          if (stdout) {
            console.log('stdout from wskDeploy:');
            console.log(stdout);
            console.log('type');
            console.log(typeof stdout);

            if (typeof stdout === 'string') {
              try {
                stdout = JSON.parse(stdout);
              } catch (e) {
                console.log('Failed to parse stdout, it wasn\'t a JSON object');
              }
            }

            if (typeof stdout === 'object') {
              if (stdout.error) {
                stdout.descriptiveError = 'Could not successfully run wskdeploy. Did you provide the needed environment variables?';
                reject(stdout);
              }
            }
          }
          if (stderr) {
            console.log('stderr from wskDeploy:');
            console.log(stderr);
            console.log('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~');
          }

          console.log('Finished! Resolving now');
          resolve({
            status: 'success',
            success: true,
          });
        });
      } else {
        reject(`Error loading ${repoDir}/${manifestPath}/${manifestFileName}. Does a manifest file exist?`);
      }
    });
  });
}

/**
 * Checks if the BP directory already exists on this invoker
 * @TODO: Optimize this to use GH tags so we can see whether or not we still need to pull a new version
 * @param  {[string]} dirname [string of directory path to check]
 * @return {[Promise]}        [Whether or not directory exists]
 */
function checkIfDirExists(dirname) {
  return new Promise((resolve, reject) => {
    fs.stat(dirname, (err, stats) => {
      if (err) {
        if (err.code === 'ENOENT') {
          console.log(`Directory ${dirname} does not exist`);
          resolve({
            skipClone: false
          });
        } else {
          reject(`Error checking if ${dirname} exists`, err);
        }
      }
      // Directory does exist, skip git clone
      // @TODO: Add optimization/caching here if repo exists on invoker already
      resolve({
        skipClone: true
      });
    });
  });
}

/**
 * Checks that a GitHub username, password (or access token), and repo
 *  are all passed in the params
 * @param  {[Object]} params    [Params object]
 * @return {[String || Object]} [String of remote URL if successful, object if error]
 */
function convertParamsToRemote(params) {
  const {
    repo,
  } = params;
  if (!repo) {
    return {
      error: 'Please enter the GitHub repo in params',
    };
  } else if (repo.indexOf('https://') === 0) { //Check if `https://` was included in the repo, prepend it if not
    return repo;
  } else {
    return `https://${repo}`;
  }
}

/**
 * Checks if wsk API host and auth were provided in params, if not, gets them from process.env
 * @param  {[Object]} params    [Params object]
 * @return {[Object]}           [Object containing wskApiHost and wskAuth]
 */
function getWskApiAuth(params) {
  let {
    wskApiHost,
    wskAuth,
  } = params;

  if (!wskApiHost) {
    wskApiHost = process.env.__OW_API_HOST;
  }

  if (!wskAuth) {
    wskAuth = process.env.__OW_API_KEY;
  }

  console.log(`Using wskApiHost: ${wskApiHost} and wskAuth: ${wskAuth}`);

  return {
    wskApiHost,
    wskAuth,
  };
}

exports.main = main;
