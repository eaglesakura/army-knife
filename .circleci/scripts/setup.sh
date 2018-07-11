#! /bin/bash -eu

echo "#######################################"
echo "# checkout secret files"
echo "#######################################"
mkdir ${CIRCLE_ARTIFACTS}
chmod +x -R ./scripts
chmod +x ./gradlew

./gradlew --parallel androidDependencies
