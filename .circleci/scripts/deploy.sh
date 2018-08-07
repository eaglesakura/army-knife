#! /bin/bash -eu

echo "#######################################"
echo "# upload artifacts"
echo "#######################################"
./gradlew sourcesJar
./gradlew javadocJar
./gradlew bintrayUpload
