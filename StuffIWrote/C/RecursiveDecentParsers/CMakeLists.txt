cmake_minimum_required(VERSION 3.3)
project(RecursiveDecentParsers)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.c)
add_executable(RecursiveDecentParsers ${SOURCE_FILES})
