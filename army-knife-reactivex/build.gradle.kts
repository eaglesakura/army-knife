apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

dependencies {
    "api"(project(":army-knife"))


    /**
     * Reactive Extensions
     */
    "api"("io.reactivex.rxjava2:rxkotlin:2.3.0")
    "api"("io.reactivex.rxjava2:rxandroid:2.1.1")

    "testImplementation"(project(":army-knife-android-junit4"))
    "androidTestImplementation"(project(":army-knife-android-junit4"))
}