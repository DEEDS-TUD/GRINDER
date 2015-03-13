#!/bin/sh

# Add -i to show response headers

#curl -H "Accept: text/xml" -X GET http://localhost:8182/targets

curl -i -X PUT -d "start" http://localhost:8182/targets/1
