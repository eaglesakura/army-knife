apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library-junit4.gradle")
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
    "api"("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_version")
    "api"("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines_version")

    /**
     * Support Libraries
     * https://developer.android.com/topic/libraries/architecture/adding-components
     * https://developer.android.com/topic/libraries/support-library/refactor
     */
    "compileOnly"("androidx.core:core-ktx:1.0.1")
    "compileOnly"("androidx.collection:collection-ktx:1.0.0")
    "compileOnly"("androidx.fragment:fragment-ktx:1.0.0")
    "compileOnly"("androidx.appcompat:appcompat:1.0.2")

    /**
     * Test Tools.
     */
    "api"("org.assertj:assertj-core:3.12.1")
    "api"("junit:junit:4.12")
    "api"("androidx.test:core:1.1.0")
    "api"("androidx.test:monitor:1.1.1")
    "api"("androidx.test.ext:junit:1.1.0")
    "api"("androidx.test:rules:1.1.1")
    "api"("com.nhaarman:mockito-kotlin:1.6.0")
    "api"("com.nhaarman:mockito-kotlin-kt1.1:1.6.0")
    "api"("androidx.test.espresso:espresso-core:3.1.1")
    "compileOnly"("org.robolectric:robolectric:4.0.2")
}