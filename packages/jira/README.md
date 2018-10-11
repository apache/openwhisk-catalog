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

# Using the JIRA package

The /whisk.system/jira package offers a convenient way to use the JIRA APIs.

The package includes the following feed:

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/jira` | package | username, siteName, force_http, accessToken | Interact with the JIRA API |
| `/whisk.system/jira/jirafeed` | feed | events, username,  siteName, force_http, accessToken | Fire trigger events on JIRA activity |


Creating a package binding with the username, siteName, force_http and accessToken values is suggested. With binding, you don't need to specify the values each time that you use the feed in the package.
Firing a trigger event with JIRA activity

The /whisk.system/jira/jirafeed feed configures a service to fire a trigger when there is activity in a specified JIRA site. The parameters are as follows:

- `username`: The user name of the atlassian account.
- `siteName`: Your atlassian site name.
- `accessToken`: Your JIRA personal access token.
- `webhookName`: A name for the JIRA webhook (optional).
- `force_http`: 'true' or 'false' (if you want to use http protocol to bypass SSL verification).
- `events`: The [JIRA event type](https://developer.atlassian.com/server/jira/platform/webhooks/) of interest.

The following is an example of creating a trigger that will be fired each time that there is a new issue created, deleted or modified in your atlassian site.

1. Generate a JIRA [personal access token](https://confluence.atlassian.com/bitbucketserver/personal-access-tokens-939515499.html).


The access token will be used in the next step.

2. Create a package binding that is configured for your GitHub repository and with your access token.

```
wsk package bind /whisk.system/jira myJira \
  --param username mike@anymail.com \
  --param siteName mike.atlassian.net \
  --param webhookName my_first_webhook \
  --param force_http false \
  --param accessToken aaaaa1111a1a1a1a1a111111aaaaaa1111aa1a1a
```

3. Create a trigger for the GitHub `push` event type by using your `myJira/jirafeed` feed.


```
wsk trigger create myJiraTrigger --feed myJira/jirafeed --param events jira:issue_created,jira:issue_deleted 
```
  
Creating deleting or updating an issue causes the trigger to be fired by the webhook. If there is a rule that matches the trigger, then the associated action will be invoked. The action receives the JIRA webhook payload as an input parameter. Each JIRA webhook event has a similar JSON schema, but is a unique payload object that is determined by its event type. For more information about the payload content, see the [JIRA API documentation](https://developer.atlassian.com/server/jira/platform/rest-apis/).
