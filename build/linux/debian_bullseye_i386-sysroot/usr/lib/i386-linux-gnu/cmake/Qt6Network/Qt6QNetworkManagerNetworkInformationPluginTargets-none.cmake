#----------------------------------------------------------------
# Generated CMake target import file for configuration "None".
#----------------------------------------------------------------

# Commands may need to know the format version.
set(CMAKE_IMPORT_FILE_VERSION 1)

# Import target "Qt6::QNetworkManagerNetworkInformationPlugin" for configuration "None"
set_property(TARGET Qt6::QNetworkManagerNetworkInformationPlugin APPEND PROPERTY IMPORTED_CONFIGURATIONS NONE)
set_target_properties(Qt6::QNetworkManagerNetworkInformationPlugin PROPERTIES
  IMPORTED_COMMON_LANGUAGE_RUNTIME_NONE ""
  IMPORTED_LOCATION_NONE "${_IMPORT_PREFIX}/lib/i386-linux-gnu/qt6/plugins/networkinformation/libqnetworkmanager.so"
  IMPORTED_NO_SONAME_NONE "TRUE"
  )

list(APPEND _cmake_import_check_targets Qt6::QNetworkManagerNetworkInformationPlugin )
list(APPEND _cmake_import_check_files_for_Qt6::QNetworkManagerNetworkInformationPlugin "${_IMPORT_PREFIX}/lib/i386-linux-gnu/qt6/plugins/networkinformation/libqnetworkmanager.so" )

# Commands beyond this point should not need to know the version.
set(CMAKE_IMPORT_FILE_VERSION)
