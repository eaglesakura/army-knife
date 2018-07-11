###########################################
##         Project full-build image.
##          Support. kotlin
###########################################
FROM ubuntu:18.04
MAINTAINER @eaglesakura

#####
# setup argments
# Version 1.0.0 : initial
#####
ARG ANDROID_DOWNLOAD_URL=https://dl.google.com/android/repository/sdk-tools-linux-3859397.zip
ARG ANDROID_BUILD_TOOLS_VERSION=28.0.0
ARG ANDROID_TARGET_SDK_VERSION=28

#####
# build container
#####
ENV _JAVA_OPTIONS="-Dfile.encoding=UTF-8" \
    JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64/" \
    LD_LIBRARY_PATH=/usr/local/lib:$LD_LIBRARY_PATH

RUN  dpkg --add-architecture i386 \
  && apt update \
  && mkdir $HOME/tools \
  && apt install --fix-missing -y \
            libc6:i386 libstdc++6:i386 \
            curl wget unzip git-core \
            openjdk-8-jdk

###########################################
## Android SDK
###########################################
ENV ANDROID_HOME=/root/tools/android/sdk \
    ANDROID_NDK_HOME=/root/tools/android/sdk/ndk-bundle
ENV PATH=$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin:$ANDROID_HOME/platform-tools:$ANDROID_NDK_HOME:$PATH
RUN  mkdir $HOME/tools/android \
  && mkdir ${ANDROID_HOME} \
  && mkdir ${ANDROID_HOME}/licenses \
  && mkdir $HOME/.android/ \
  && touch $HOME/.android/repositories.cfg \
  && wget ${ANDROID_DOWNLOAD_URL} -O $HOME/android-sdk.zip \
  && unzip -d $HOME/tools/android/sdk $HOME/android-sdk.zip > /dev/null \
  && rm $HOME/android-sdk.zip \
  && yes | sdkmanager "platform-tools" \
  && yes | sdkmanager "build-tools;${ANDROID_BUILD_TOOLS_VERSION}" \
  && yes | sdkmanager "platforms;android-${ANDROID_TARGET_SDK_VERSION}" \
  && yes | sdkmanager "extras;android;m2repository" \
  && yes | sdkmanager "extras;google;google_play_services" \
  && yes | sdkmanager "extras;google;m2repository" \
  && yes | sdkmanager --channel=3 --update
