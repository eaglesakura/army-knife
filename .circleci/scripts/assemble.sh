#! /bin/bash -eu

echo "#######################################"
echo "# assemble"
echo "#######################################"
./gradlew --parallel assembleAndroidTest

