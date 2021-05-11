cmake_minimum_required(VERSION 3.18.0)

set(CMAKE_BUILD_TYPE Debug)

project(clox C)

file(GLOB clox_SRC CONFIGURE_DEPENDS "*.h" "*.c" )

add_executable(clox ${clox_SRC})
