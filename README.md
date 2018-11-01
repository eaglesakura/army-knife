# What is this repository?

If You will go to battlefield of development then choose weapons from this repository.

|Version|Build Status| Feature |
|----|----|----|
|v1.0.x|[![CircleCI](https://circleci.com/gh/eaglesakura/army-knife/tree/v1.0.x.svg?style=svg&circle-token=a26b28c5daa1b5160b87c3501747f8ae21990295)](https://circleci.com/gh/eaglesakura/army-knife/tree/v1.0.x)| Kotlin 1.3 / Coroutines 1.0.0 supported |

# how to implementation into your project

```groovy
// /build.gradle
allprojects {
    repositories {
        // add the below line into build.gradle.
        maven { url 'https://dl.bintray.com/eaglesakura/maven/' }
    }
}

// /app/build.gradle
dependencies {
    // check versions
    // https://github.com/eaglesakura/army-knife/releases
    // 'com.eaglesakura:${library name}:${release version}'
    implementation 'com.eaglesakura:army-knife:1.0.0'
    implementation 'com.eaglesakura:army-knife-reactivex:1.0.0'
}
```

# army-knife

Library for android applications with Kotlin.

Source codes are all test(or playground) version in this repository.

army-knife is small library, but it can be more small.
If you have to shrink to application, then proguard-options set to enable.

# army-knife-android-junit4

JUnit4 Utilities for Android with Kotlin.

[README](./army-knife-android-junit4/README.md)

# army-knife-camera

Camera2 API Wrapper with Coroutines.

# army-knife-gms

Google Play Services API utilities with Coroutines.

# army-knife-persistence

The Key-Value store implementation by SQLite utilities.

# army-knife-reactivex

Pub/Sub utilities by RxKotlin(RxJava2).

RxStream<T> can convert to the LiveData<T>, and can convert to the Channel<T>.

# army-knife-timber

Kotlin utilities for [Timber](https://github.com/JakeWharton/timber).

# firearm

`firearm` includes convenient classes.

however, It restrict the application architecture.

# firearm-channel

"onActivityResult" and "Runtime Permissions" are convert to Channel<T>.

You can call "startActivityForResult" and "onActivityResult"  in coroutines.

But, this library NOT support process-shutdown and process-restart. Don't use this library for long time activity.

short-time activity(so less than 60 sec) can use this.

# firearm-di

Dependency Injection libraries with Kotlin.

This library is very small, and fast.

# firearm-event

Event-Driven support.

# kerberus

`kerberus` is coroutine-base async tasks library with Kotlin.

If you love java, use the [Cerberus](https://github.com/eaglesakura/cerberus) library.

# [Deprecated] ~~OneshotLiveData(include EventStream classes)~~

~~`OneshotLiveData` is LiveData for Event, SnackBar, Toast, and more.~~

~~LiveData's observer is subscribe to new-data and not-modified data.~~

~~But, SnackBar will require one-shot data, not required not-modified data.~~

~~e.g. LiveData's Observer will subscribe not-modified data on "onResume" function. OneshotLiveData's Observer will `not` subscribe it.~~
