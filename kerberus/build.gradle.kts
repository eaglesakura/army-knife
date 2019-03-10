apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

val kotlin_version
    get() = rootProject.extra["kotlin_version"] as String
val kotlin_coroutines_version
    get() = rootProject.extra["kotlin_coroutines_version"] as String

dependencies {
    /**
     * Kotlin support
     */
    "api"("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    "api"("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    "api"("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_version")
    "api"("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_version")

    /**
     * Support Libraries
     * https://developer.android.com/topic/libraries/architecture/adding-components
     * https://developer.android.com/topic/libraries/support-library/refactor
     */
    "api"("androidx.core:core-ktx:1.0.1")
    "api"("androidx.appcompat:appcompat:1.0.2")
}
