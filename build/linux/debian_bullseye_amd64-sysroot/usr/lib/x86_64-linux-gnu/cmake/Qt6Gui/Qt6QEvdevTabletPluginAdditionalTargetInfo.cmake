# Additional target information for Qt6QEvdevTabletPlugin
if(NOT DEFINED QT_DEFAULT_IMPORT_CONFIGURATION)
    set(QT_DEFAULT_IMPORT_CONFIGURATION NONE)
endif()
__qt_internal_promote_target_to_global_checked(Qt6::QEvdevTabletPlugin)
get_target_property(_qt_imported_location Qt6::QEvdevTabletPlugin IMPORTED_LOCATION_NONE)
get_target_property(_qt_imported_location_default Qt6::QEvdevTabletPlugin IMPORTED_LOCATION_${QT_DEFAULT_IMPORT_CONFIGURATION})

# Import target "Qt6::QEvdevTabletPlugin" for configuration "Release"
set_property(TARGET Qt6::QEvdevTabletPlugin APPEND PROPERTY IMPORTED_CONFIGURATIONS RELEASE)

if(_qt_imported_location)
    set_property(TARGET Qt6::QEvdevTabletPlugin PROPERTY IMPORTED_LOCATION_RELEASE "${_qt_imported_location}")
endif()

# Import target "Qt6::QEvdevTabletPlugin" for configuration "RelWithDebInfo"
set_property(TARGET Qt6::QEvdevTabletPlugin APPEND PROPERTY IMPORTED_CONFIGURATIONS RELWITHDEBINFO)

if(_qt_imported_location)
    set_property(TARGET Qt6::QEvdevTabletPlugin PROPERTY IMPORTED_LOCATION_RELWITHDEBINFO "${_qt_imported_location}")
endif()

# Import target "Qt6::QEvdevTabletPlugin" for configuration "MinSizeRel"
set_property(TARGET Qt6::QEvdevTabletPlugin APPEND PROPERTY IMPORTED_CONFIGURATIONS MINSIZEREL)

if(_qt_imported_location)
    set_property(TARGET Qt6::QEvdevTabletPlugin PROPERTY IMPORTED_LOCATION_MINSIZEREL "${_qt_imported_location}")
endif()

# Default configuration
if(_qt_imported_location_default)
    set_property(TARGET Qt6::QEvdevTabletPlugin PROPERTY IMPORTED_LOCATION "${_qt_imported_location_default}")
endif()

unset(_qt_imported_location)
unset(_qt_imported_location_default)
unset(_qt_imported_soname)
unset(_qt_imported_soname_default)
unset(_qt_imported_configs)
