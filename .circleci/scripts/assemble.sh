#! /bin/bash -eu

echo "#######################################"
echo "# assemble"
echo "#######################################"
./gradlew assembleAndroidTest
./gradlew assembleDebug
./gradlew assembleRelease

