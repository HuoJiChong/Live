# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

include_directories(./src/main/cpp/inc)



# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

set(distribution_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI} )

add_library( x264 SHARED IMPORTED )
set_target_properties( x264 PROPERTIES IMPORTED_LOCATION ${distribution_DIR}/libx264.so )

add_library( faac STATIC IMPORTED )
set_target_properties( faac PROPERTIES IMPORTED_LOCATION ${distribution_DIR}/faac/libfaac.a )

add_library( rtmp STATIC IMPORTED )
set_target_properties( rtmp PROPERTIES IMPORTED_LOCATION ${distribution_DIR}/rtmp/librtmp.a )

aux_source_directory(./src/main/cpp RootSrc)

add_library(
        live
        SHARED
        ${RootSrc})

find_library(
        log-lib
        log)


target_link_libraries(
        live
        ${log-lib} faac x264 rtmp android rtmp)
