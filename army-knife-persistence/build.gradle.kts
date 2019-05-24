apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

dependencies {
    "api"(project(":army-knife"))

    /**
     * Support Libraries
     * https://developer.android.com/topic/libraries/architecture/adding-components
     * https://developer.android.com/topic/libraries/support-library/refactor
     */
    "api"("androidx.sqlite:sqlite:2.0.1")
    "api"("androidx.sqlite:sqlite-ktx:2.0.1")
    "api"("androidx.core:core-ktx:1.0.2")

    "testImplementation"(project(":army-knife-android-junit4"))
    "androidTestImplementation"(project(":army-knife-android-junit4"))
}