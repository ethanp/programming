cmake_minimum_required(VERSION 3.2)
project(SFMLOpenGL)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp)

add_executable(SFMLOpenGL ${SOURCE_FILES})

set(CMAKE_MODULE_PATH "${CMAKE_CURRENT_LIST_DIR}/cmake_modules")
find_package(SFML REQUIRED system window graphics network audio)
if (SFML_FOUND)
    include_directories(${SFML_INCLUDE_DIR})
    target_link_libraries(SFMLOpenGL ${SFML_LIBRARIES})
endif()

# OpenGL
find_package(OpenGL REQUIRED)
find_package(GLEW REQUIRED)
include_directories(${OPENGL_INCLUDE_DIR} ${GLEW_INCLUDE_DIRS})
if (OPENGL_FOUND)
    target_link_libraries(SFMLOpenGL ${OPENGL_LIBRARIES} ${GLEW_LIBRARIES})
    target_link_libraries(SFMLOpenGL m)  # if you use maths.h
endif()
