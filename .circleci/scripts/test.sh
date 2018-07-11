#! /bin/bash -eu

echo "#######################################"
echo "# test "
echo "#######################################"
report_cp() {
    MODULE=$1
    mkdir "${CIRCLE_ARTIFACTS}/junit"
    mkdir "${CIRCLE_ARTIFACTS}/junit/${MODULE}"
    mkdir "${CIRCLE_ARTIFACTS}/junit-reports"
    mkdir "${CIRCLE_ARTIFACTS}/junit-reports/${MODULE}"

    cp -r ./${MODULE}/build/reports "${CIRCLE_ARTIFACTS}/junit/${MODULE}"
    cp -r ./${MODULE}/build/test-results "${CIRCLE_ARTIFACTS}/junit-reports/${MODULE}"
}

./gradlew --parallel :kerberus:testDebugUnitTest || report_cp "kerberus"
./gradlew --parallel :army-knife:testDebugUnitTest || report_cp "army-knife"
