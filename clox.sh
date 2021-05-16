#!/usr/bin/env bash

set -ex

cmake -S clox -B clox/build

pushd clox/build
make
popd

exec ./clox/build/clox "$@"
