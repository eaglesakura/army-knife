#! /bin/bash -eu

echo "#######################################"
echo "# upload artifacts"
echo "#######################################"
./gradlew bintrayUpload
