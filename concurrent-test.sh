#!/bin/bash

SHORT_CODE="kbcDmAk"
URL="http://localhost:8080/r/$SHORT_CODE"

echo "Firing 20 concurrent requests to $URL ..."

for i in $(seq 1 20); do
  curl -s -o /dev/null -w "%{http_code}\n" "$URL" &
done

wait

echo "Done. Now check clickCount in the database or via /stats endpoint."