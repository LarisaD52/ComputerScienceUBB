cmake_minimum_required(VERSION 3.30)
project(H1matrix)

set(CMAKE_CXX_STANDARD 20)

add_executable(H1matrix
        App.cpp
        Matrix.h
        Matrix.cpp
        ExtendedTest.h
        ExtendedTest.cpp
        ShortTest.cpp
        ShortTest.h)
