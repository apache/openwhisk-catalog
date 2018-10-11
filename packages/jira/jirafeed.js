// Licensed to the Apache Software Foundation (ASF) under one or more contributor
// license agreements; and to You under the Apache License, Version 2.0.

var request = require('request');

/**
 *  Feed to create a webhook on JIRA
 *
 *  @param {object} params - information about the trigger
 *  @param {string} siteName - atlassian website to create webhook (enter only the first part of the site name)
 *  @param {string} username - atlassian username
 *  @param {string} force_http - 'true' or 'false'
 *  @param {string} accessToken - atlassian access token
 *  @param {string} events - list of the events the webhook should fire on
 *  @return {object} whisk async
 */
function main(params) {
    var username = params.username;
    var accessToken = params.accessToken;
    var siteName = params.siteName;
    var webhookName = params.webhookName || "My JIRA Webhook";
    var force_http = (JSON.stringify(params.force_http) === 'true' || JSON.stringify(params.force_http) === 'True');
    var authenticationHeader = "Basic " + new Buffer.from(username + ":" + accessToken).toString("base64");

    var lifecycleEvent = params.lifecycleEvent;
    var triggerName = params.triggerName.split("/");

    // URL of the whisk system. The calls of JIRA will go here.
    var urlHost = require('url').parse(process.env.__OW_API_HOST);

    if (force_http) {
        var whiskCallbackUrl = 'http://' + process.env.__OW_API_KEY + "@" + urlHost.host.substring(0, urlHost.host.length - 4) + '/api/v1/namespaces/'
            + encodeURIComponent(triggerName[1]) + '/triggers/' + encodeURIComponent(triggerName[2]);
    }
    else {
        var whiskCallbackUrl = urlHost.protocol + '//' + process.env.__OW_API_KEY + "@" + urlHost.host + '/api/v1/namespaces/'
            + encodeURIComponent(triggerName[1]) + '/triggers/' + encodeURIComponent(triggerName[2]);
    }
    // The URL to create the webhook on JIRA
    var myJiraUrl = 'https://' + siteName + '/rest/webhooks/1.0/webhook';

    if (lifecycleEvent === "CREATE") {
        var events = params.events.split(',');

        var body = {
            name: webhookName,
            url: whiskCallbackUrl,
            events: events,
            excludeBody: false
        };

        var options = {
            //hostname: jiraHostUrl,
            //path: '/rest/webhooks/1.0/webhook',
            url: myJiraUrl,
            method: 'POST',
            body: JSON.stringify(body),
            headers: {
                'Authorization': authenticationHeader,
                'Content-Type': 'application/json',
                'User-Agent': 'whisk'
            }
        };

        var promise = new Promise(function (resolve, reject) {
            request(options, function (error, response, body) {
                if (error) {
                    console.log(error);
                    reject({
                        response: response,
                        error: error,
                        body: body
                    });
                } else {
                    console.log("Status code: " + response.statusCode);

                    if (response.statusCode >= 400) {
                        console.log("Response from JIRA: " + body);
                        reject({
                            statusCode: response.statusCode,
                            response: body
                        });
                    } else {
                        resolve({response: body});
                        console.log("Webhook created successfully");
                    }
                }
            });
        });

        return promise;


    } else if (lifecycleEvent === "DELETE") {

        //list all the existing webhooks first.
        var deleteOptions = {
            //hostname : jiraHostUrl,
            //path : '/rest/webhooks/1.0/webhook',
            url: myJiraUrl,
            method: 'GET',
            headers: {
                'Authorization': authenticationHeader,
                'Content-Type': 'application/json',
                'User-Agent': 'whisk'
            }
        };


        var deletePromise = new Promise(function (resolve, reject) {
            request(deleteOptions, function (error, response, body) {
                // the URL that comes back from JIRA does not include auth info
                var foundWebhookToDelete = false;

                if (error) {
                    console.log(error);
                    reject({
                        response: response,
                        error: error,
                        body: body
                    });
                } else {
                    bodyObj = JSON.parse(body);
                    for (var i = 0; i < bodyObj.length; i++) {
                        if (decodeURI(bodyObj[i].url) === whiskCallbackUrl) {
                            foundWebhookToDelete = true;
                            console.log('DELETE Webhook with callBackURL: ' + bodyObj[i].url);
                            var urlArray = bodyObj[i].self.split('/');
                            var webhookId = urlArray[urlArray.length - 1];

                            var options = {
                                method: 'DELETE',
                                url: bodyObj[i].self,
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': authenticationHeader,
                                    'User-Agent': 'whisk'
                                }
                            };


                            request(options, function (error, response, body) {
                                if (error) {
                                    console.log(error);
                                    reject({
                                        response: response,
                                        error: error,
                                        body: body
                                    });
                                } else {
                                    console.log("Status code: " + response.statusCode);
                                    if (response.statusCode >= 400) {
                                        console.log("Response from JIRA: " + body);
                                        if (response.statusCode === 404) {
                                            console.log('Please ensure your accessToken is authorized to delete webhooks.');
                                        }

                                        reject({
                                            statusCode: response.statusCode,
                                            response: body
                                        });
                                    } else {
                                        resolve({response: body});
                                        console.log("Webhook deleted successfully");
                                    }
                                }
                            });
                        }
                    }

                    if (!foundWebhookToDelete) {
                        reject('No related webhooks found for this trigger');
                    }
                }
            });
        });

        return deletePromise;

    }
}
