cmake_minimum_required(VERSION 3.30)
project(untitled4)

set(CMAKE_CXX_STANDARD 20)

add_executable(untitled4 App.cpp
    Bag.cpp
    Bag.h
    BagIterator.cpp
    BagIterator.h
    ExtendedTest.cpp
    ExtendedTest.h
    ShortTest.cpp
    ShortTest.h
)
