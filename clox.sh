#!/usr/bin/env bash

set -ex

cmake -S clox -B clox/build

pushd clox/build
make
popd

# if [ -x "$(which rlwrap)" ]; then
#   exec rlwrap ./clox/build/clox "$@"
# else
  exec valgrind ./clox/build/clox "$@"
# fi
