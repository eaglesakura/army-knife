apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

val kotlin_version
    get() = rootProject.extra["kotlin_version"] as String
val kotlin_coroutines_version
    get() = rootProject.extra["kotlin_coroutines_version"] as String

dependencies {
    /**
     * java libraries
     */
    "compileOnly"("io.reactivex.rxjava2:rxkotlin:2.3.0")  // Reactive Extension
    "compileOnly"("io.reactivex.rxjava2:rxandroid:2.1.1")   // Reactive Extension

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
    "api"("androidx.annotation:annotation:1.0.2")
    "api"("androidx.core:core:1.0.2")
    "api"("androidx.core:core-ktx:1.0.2")
    "api"("androidx.collection:collection-ktx:1.0.0")
    "api"("androidx.fragment:fragment-ktx:1.0.0")
    "api"("androidx.appcompat:appcompat:1.0.2")
    "api"("androidx.lifecycle:lifecycle-extensions:2.0.0")
    "api"("androidx.lifecycle:lifecycle-viewmodel-ktx:2.0.0")
    "api"("androidx.lifecycle:lifecycle-runtime:2.0.0")
    "api"("com.google.android.material:material:1.0.0") // Material components

    "compileOnly"("androidx.sqlite:sqlite:2.0.1")
    "compileOnly"("androidx.sqlite:sqlite-ktx:2.0.1")

    /**
     * Google Play Services
     */
    "compileOnly"("com.google.android.gms:play-services-auth:16.0.1")

    /**
     * Firebase
     */
    "compileOnly"("com.google.firebase:firebase-core:16.0.9")
    "compileOnly"("com.google.firebase:firebase-auth:17.0.0")
    "compileOnly"("com.google.firebase:firebase-config:16.5.0")
    "compileOnly"("com.google.firebase:firebase-iid:17.1.2")
}