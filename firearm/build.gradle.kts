apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

dependencies {
    /**
     * Kotlin support
     */
    "api"(project(":army-knife"))
    "api"(project(":army-knife-persistence"))
    "api"(project(":army-knife-reactivex"))
    "api"(project(":firearm-event"))

    /**
     * Support Libraries
     * https://developer.android.com/topic/libraries/architecture/adding-components
     * https://developer.android.com/topic/libraries/support-library/refactor
     */
    "compileOnly"("androidx.sqlite:sqlite:2.0.0")
    "compileOnly"("androidx.sqlite:sqlite-ktx:2.0.0")

    "testImplementation"(project(":army-knife-android-junit4"))
    "androidTestImplementation"(project(":army-knife-android-junit4"))
}
