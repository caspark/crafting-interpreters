#!/usr/bin/env bash

set -ex

gradle installDist --info

if [ -x "$(which rlwrap)" ]; then
  exec rlwrap ./app/build/install/app/bin/app "$@"
else
  exec ./app/build/install/app/bin/app "$@"
fi