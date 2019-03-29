apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

dependencies {
    "api"("com.jakewharton.timber:timber:4.7.1")

    "testImplementation"(project(":army-knife-android-junit4"))
    "androidTestImplementation"(project(":army-knife-android-junit4"))
}