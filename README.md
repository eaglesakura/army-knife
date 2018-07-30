# What is this repository?

If You will go to battlefield of development then choose weapons from this repository.

|Version|Build Status|
|----|----|
|v0.1.x|[![CircleCI](https://circleci.com/gh/eaglesakura/army-knife/tree/v0.1.x.svg?style=svg&circle-token=a26b28c5daa1b5160b87c3501747f8ae21990295)](https://circleci.com/gh/eaglesakura/army-knife/tree/v0.1.x)|

`army-knife` is Library for android applications with Kotlin.

Source codes are all test(or playground) version in this repository.

# Kerberus

`Kerberus` is coroutine-base async tasks library with Kotlin.

If you love java, use the [Cerberus](https://github.com/eaglesakura/cerberus) library.

# OneshotLiveData(include EventStream classes)

`OneshotLiveData` is LiveData for Event, SnackBar, Toast, and more.

LiveData's observer is subscribe to new-data and not-modified data.

But, SnackBar will require one-shot data, not required not-modified data.

e.g. e.g. LiveData's Observer will subscribe not-modified data on "onResume" function. OneshotLiveData's Observer will `not` subscribe it.

