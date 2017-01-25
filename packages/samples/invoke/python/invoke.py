#
#
# main() will be invoked when you Run This Action.
#
# @dict Whisk actions accept a single parameter,
#        which must be a JSON object.
#        It could be empty for this action.
#
# @return which must be a JSON object.
#         It will be the output of this action.
#
#

import sys
import os
import requests
import json
import base64

def main(dict):

    # Check for required environment variables.
    if '__OW_API_HOST' not in os.environ:
        print("Invocation error: Environment variable __OW_API_HOST must be defined.")
        sys.exit(1)
    if '__OW_NAMESPACE' not in os.environ:
        print("Invocation error: Environment variable __OW_NAMESPACE must be defined.")
        sys.exit(1)
    if '__OW_API_KEY' not in os.environ:
        print("Invocation error: Environment variable __OW_API_KEY must be defined.")
        sys.exit(1)

    apiKey = base64.b64encode(os.environ.get('__OW_API_KEY'))
    apiHost = os.environ.get('__OW_API_HOST')
    namespace = "whisk.system"
    action = 'utils/date'
    apiVersion = 'v1'

    headers = {\
        'Authorization': 'Basic ' + apiKey,\
        'content-type': 'application/json'\
    }

    # invoke action: "whisk.system/utils/date"
    url =  apiHost + '/api/' + apiVersion + '/namespaces/' + namespace + '/actions/' + action + '?blocking=true'

    payload = {}

    # Issue the request

    print('Issuing request: url=' + url + ' payload=' + repr(payload) + ' headers=' + repr(headers))
    requests.packages.urllib3.disable_warnings()
    response = requests.post(url, data=json.dumps(payload), headers=headers, verify=False)

    # Show the response
    print('Response status_code=%i' % response.status_code)


    if response.status_code == 200:
        json_data = json.loads(response.text)
        if (json_data['response']['result']['date']):
            return {"Today's date is": json_data['response']['result']['date']}

    return {"Error": "Could not invoke the date, failed with status code %i." % response.status_code}
