#!/usr/bin/env sh
set -e
if command -v mvn >/dev/null 2>&1; then
  exec mvn "$@"
fi
echo "Maven 3.6.3+ is required. Install Maven or replace this script with the official Maven Wrapper." >&2
exit 127
