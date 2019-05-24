apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

val extras = Extras(this)

dependencies {
    /**
     * RxJava
     */
    "compileOnly"("io.reactivex.rxjava2:rxkotlin:2.3.0")
    "compileOnly"("io.reactivex.rxjava2:rxandroid:2.1.1")

    /**
     * Kotlin support
     */
    "compileOnly"("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${extras.kotlin.version}")
    "compileOnly"("org.jetbrains.kotlin:kotlin-reflect:${extras.kotlin.version}")
    "compileOnly"("org.jetbrains.kotlinx:kotlinx-coroutines-core:${extras.kotlin.coroutinesVersion}")
    "compileOnly"("org.jetbrains.kotlinx:kotlinx-coroutines-android:${extras.kotlin.coroutinesVersion}")

    /**
     * Support Libraries
     * https://developer.android.com/topic/libraries/architecture/adding-components
     * https://developer.android.com/topic/libraries/support-library/refactor
     */
    "compileOnly"("androidx.core:core-ktx:1.0.2")
    "compileOnly"("androidx.appcompat:appcompat:1.0.2")
    "compileOnly"("androidx.lifecycle:lifecycle-extensions:2.0.0")
    "compileOnly"("androidx.lifecycle:lifecycle-viewmodel-ktx:2.0.0")
}