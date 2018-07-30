#! /bin/bash -eu

echo "#######################################"
echo "# upload artifacts"
echo "#######################################"
# TODO upload archives
./gradlew bintrayUpload
