apply(from = "../dsl/armyknife.gradle.kts")
apply(from = "../dsl/android-library.gradle")
apply(from = "../dsl/bintray.gradle")

dependencies {
    "api"(project(":army-knife"))

    /**
     * Google Play Services
     */
    "implementation"("com.google.android.gms:play-services-auth:16.0.1")

    /**
     * Firebase
     */
    "implementation"("com.google.firebase:firebase-core:16.0.7")
    "implementation"("com.google.firebase:firebase-auth:16.1.0")
    "implementation"("com.google.firebase:firebase-config:16.3.0")
    "implementation"("com.google.firebase:firebase-iid:17.1.0")

    "testImplementation"(project(":army-knife-android-junit4"))
    "androidTestImplementation"(project(":army-knife-android-junit4"))
}
