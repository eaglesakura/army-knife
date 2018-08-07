#! /bin/bash -eu

echo "#######################################"
echo "# assemble"
echo "#######################################"
./gradlew --parallel assembleAndroidTest
./gradlew --parallel assembleDebug
./gradlew --parallel assembleRelease

