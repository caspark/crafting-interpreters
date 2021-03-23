#!/usr/bin/env bash

set -ex

gradle installDist --info

exec ./app/build/install/app/bin/app "$@"