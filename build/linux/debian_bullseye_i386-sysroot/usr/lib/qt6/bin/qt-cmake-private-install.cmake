# Calls cmake --install ${QT_BUILD_DIR} --config <config> for each config
# with which Qt was built with.
# This is required to enable installation of all configurations of
# a Qt built with Ninja Multi-Config until the following issues are fixed:
# https://gitlab.kitware.com/cmake/cmake/-/issues/20713
# https://gitlab.kitware.com/cmake/cmake/-/issues/21475
set(configs "None")
if(NOT QT_BUILD_DIR)
    message(FATAL_ERROR "No QT_BUILD_DIR value provided to qt-cmake-private-install.")
endif()
unset(strip_arg)
if ("x" STREQUAL "x")
  set(strip_arg --strip)
endif()
foreach(config ${configs})
    message(STATUS "Installing configuration: '${config}'")
    set(args "${CMAKE_COMMAND}" --install ${QT_BUILD_DIR} --config "${config}" ${strip_arg})
    execute_process(COMMAND ${args}
                    COMMAND_ECHO STDOUT
                    RESULT_VARIABLE result)
    if(NOT "${result}" STREQUAL "0")
        message(FATAL_ERROR "Installing configuration '${config}' failed with exit code: ${result}.")
    endif()
endforeach()
