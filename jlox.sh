#!/usr/bin/env bash

set -ex

gradle installDist

exec ./app/build/install/app/bin/app "$@"