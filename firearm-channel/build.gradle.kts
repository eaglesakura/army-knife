apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

val extras = Extras(this)

dependencies {
    /**
     * Kotlin support
     */
    "api"("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${extras.kotlin.version}")
    "api"("org.jetbrains.kotlinx:kotlinx-coroutines-core:${extras.kotlin.coroutinesVersion}")
    "api"("org.jetbrains.kotlinx:kotlinx-coroutines-android:${extras.kotlin.coroutinesVersion}")

    /**
     * Support Libraries
     * https://developer.android.com/topic/libraries/architecture/adding-components
     * https://developer.android.com/topic/libraries/support-library/refactor
     */
    "api"("androidx.appcompat:appcompat:1.0.2")
    "implementation"("androidx.lifecycle:lifecycle-extensions:2.0.0")
    "implementation"("androidx.lifecycle:lifecycle-viewmodel-ktx:2.0.0")

    "testImplementation"(project(":army-knife-android-junit4"))
    "androidTestImplementation"(project(":army-knife-android-junit4"))
}