cmake_minimum_required(VERSION 2.8.4)
project(ExProb1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp birds.h)
add_executable(ExProb1 ${SOURCE_FILES})
