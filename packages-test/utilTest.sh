#!/bin/bash
#
# Test the script of util.sh.
#
#
SCRIPTDIR="$(cd $(dirname "$0")/ && pwd)"
echo "This script is about to test another script util.sh."
sh ./../packages/util.sh
echo "This script has just run another script util.sh."
