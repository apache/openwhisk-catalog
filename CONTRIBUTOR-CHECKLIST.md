## OpenWhisk Package On-boarding Checklist (In-Progress)

_For process questions, please contact the owners:
Matt Rutkowski ([mrutkows@us.ibm.com](mailto:mrutkows@us.ibm.com)),
Carlos Santana ([csantana@us.ibm.com](mailto:csantana@us.ibm.com))_

1. **Contributor License Agreement (CLA)**
  1. **Contributors**
    * **Sign the CLA**: All contributors MUST sign and submit a CLA. _This includes *all developers of the code submission** NOT just the submitter._
      * This is either the [CLA-Individual.md](https://github.com/openwhisk/openwhisk/blob/master/CLA-INDIVIDUAL.md) or the [CLA-CORPORATE.md](https://github.com/openwhisk/openwhisk/blob/master/CLA-CORPORATE.md) as contained in the main OpenWhisk repository in GitHub.
    * **Verify CLA received**: Verify the CLAs have been received and are complete by **_both_** **Stephen Fink** (sjfink@us.ibm.com) and **Rodric Rabbah** (rabbah@us.ibm.com) as identified in the CLA instructions.
  2. **OpenWhisk Admins**
     * OpenWhisk admins will not accept pull requests from contributors unless CLAs from all contributors for the submission are received.
[comment]: <=========================================================================================>
2. **Open Source Approval**
  1. **Contributors**
    * In general, all open source contributions are in accordance to the Contributor License Agreements (CLAs) and submitted under the [LICENSE.txt](https://github.com/openwhisk/openwhisk/blob/master/LICENSE.txt) file of the OpenWhisk project (i.e., Apache 2 license).
    * **Email Contributor list**: Email all contributor names, company affiliation and their email addresses to **Richard Jacob** ([richard.jacob@de.ibm.com](mailto:richard.jacob@de.ibm.com)).
      * _Copy:_ Andreas Nauerz ([andreas.nauerz@de.ibm.com](mailto:andreas.nauerz@de.ibm.com)), Matt Rutkowski ([mrutkows@us.ibm.com](mailto:mrutkows@us.ibm.com)), Carlos Santana ([csantana@us.ibm.com](mailto:csantana@us.ibm.com))
  2. **OpenWhisk Admins**
    * OpenWhisk admins will not accept pull requests on new packages unless all contributor names are received.
[comment]: <=========================================================================================>
3. **Package Content**
   * Verify required package content including:
     * _Documentation_ (see next item for details).
     * _Directory structure_
     * _Manifests_
       * **TBD** (OpenWhisk specification under development)
     * _Metadata_
       * **TBD** (OpenWhisk specification under development)
[comment]: <=========================================================================================>
4. **Documentation Review**
   * Provide **_README.md_** with all sections completed:
   * Provide **_LICENSE.txt_** (which must be Apache 2 for OpenWhisk)
     * If other licenses are submitted, stop the process and contact process owners for guidance.
   * Provide **_CONTRIBUTING.md_**
     * If the package code will be under the existing "openwhisk" repo, you may copy the following single CONTRIBUTING.md (from "openwhisk-catalog") that links to the parent CLAs (both corporate and individual): 
       * [https://github.com/openwhisk/openwhisk-catalog/blob/master/CONTRIBUTING.md](https://github.com/openwhisk/openwhisk-catalog/blob/master/CONTRIBUTING.md)
[comment]: <=========================================================================================>
5. **Code Review**
  1. **Contributors**
    * The following items should be part of a code review and documented:
      * **License Headers**: Verify all code has Apache 2 license in the header (see [CONTRIBUTING.md](https://github.com/openwhisk/openwhisk/blob/master/CONTRIBUTING.md) in the OpenWhisk Github repository.
      * **Peer Review code**: (actions/feeds etc.) with more than one member of the (core) OpenWhisk team.
      * **Functionality**: Verify the function performs as desired and is easily consumable.
      * **Namespace**: Verify appropriateness of (catalog) namespace.
      * **Value assessment** Reviewers should make sure the package has value over existing packages (and avoids overlap)
      * **Verify Runtime Conventions**: Links to code conventions by language (e.g., JS, Swift, Python, etc.)
        * **TBD**
  2. **OpenWhisk Admins**
    * **Project Names** and **IP Addresses**: OpenWhisk Admins must assure no project names, IP addresses, etc. are in any code being submitted to OpenWhisk (if developed or tested internally first).
    * **Credentials** or any form Identification, Authentication, or Authorization (IAA) information should NOT appear in any package code submissions (including installation, scripts, environment scripts or file). OpenWhisk admins will not accept any submissions that has any indication of such IAA information.
[comment]: <=========================================================================================>
6. **Automated Testing**
   * Review automated tests
     * Various tests may be required, especially for packages that include deployment of or integration with hosted services (e.g., that implement triggers)
   * "Automated tests implemented, reviewed, and properly integrated
[comment]: <=========================================================================================>
7. **Runbooks** defined and reviewed
    1. **Contributors**
      * Verify installation scripts (bash, etc.) exist to install the package components into the catalog via "wsk" CLI.
      * See example: TBD
[comment]: <=========================================================================================>
8. **Maintenance**
   * Identify 2 responsible persons for maintenance of package code
   * Assure these persons and email addresses are listed in README.md
explanation how this fits into any of our reference architectures appreciated but no must)
[comment]: <=========================================================================================>
9. **Demo / Sample**
   * At least one - the more the better) available and reviewed
   * Assure the demo/sample install/run is documented or linked in README.md
[comment]: <=========================================================================================>
10. **Marketing / Promotion Materials**
   * for OpenWhisk.org, submit
     * Blogs - Prepare 1 or more articles explaining how to use service, with screen captures, diagrams, videos
       * Note: *these will initially be posted to **developerWorks** until .org site has capability to host.*
     * Videos - Prepare and host (public links) to user videos of package/service in use. Note: may link these to blogs.
[comment]: <=========================================================================================>
11. **Cross-Reference/Promotion**
   * Verify links from the services own doc to OpenWhisk has been added.
   * **Note:** includes product/service docs. and websites where applicable.
[comment]: <=========================================================================================>
12. **Merge Pre-reqs**
  * **Schedule Internal Presentations**
    * Assure issue is on agenda and presented (same week) to IBM Blue/OpenWhisk team during:
      * @ Monday "Sprint" meeting AND 
      * @ Tuesday "Playback" meeting.
  * **Review period**: Allow OpenWhisk devs. to test/review for 1 week min.
    * Resolve any issues noted from 1 week period.


