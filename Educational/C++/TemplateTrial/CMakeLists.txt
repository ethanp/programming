cmake_minimum_required(VERSION 2.8.4)
project(TemplateTrial)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp TC.cpp TC.h)
add_executable(TemplateTrial ${SOURCE_FILES})
