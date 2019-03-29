apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

val extras = Extras(this)

dependencies {
    /**
     * Kotlin support
     */
    "api"("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${extras.kotlin.version}")
    "api"("org.jetbrains.kotlin:kotlin-reflect:${extras.kotlin.version}")
    "api"("org.jetbrains.kotlinx:kotlinx-coroutines-core:${extras.kotlin.coroutinesVersion}")
    "api"("org.jetbrains.kotlinx:kotlinx-coroutines-android:${extras.kotlin.coroutinesVersion}")

    /**
     * Support Libraries
     * https://developer.android.com/topic/libraries/architecture/adding-components
     * https://developer.android.com/topic/libraries/support-library/refactor
     */
    "api"("androidx.core:core-ktx:1.0.1")
    "api"("androidx.appcompat:appcompat:1.0.2")
}