apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

dependencies {
    "implementation"("androidx.annotation:annotation:1.0.2")
    "api"("com.google.android.material:material:1.0.0")
}
