cmake_minimum_required(VERSION 3.7)
project(OpenGlGlut)
set(CMAKE_CXX_STANDARD 14)

set(SOURCE_FILES main.cpp)
add_executable(OpenGlGlut ${SOURCE_FILES})

find_package(GLUT REQUIRED)
find_package(OpenGL REQUIRED)
target_link_libraries(OpenGlGlut ${OPENGL_LIBRARIES} ${GLUT_LIBRARY} )
