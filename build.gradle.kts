import java.util.Date

buildscript {
    extra["kotlin_version"] = "1.3.30"
    extra["kotlin_coroutines_version"] = "1.2.0"
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlin_version"]}")
        classpath("org.jetbrains.dokka:dokka-android-gradle-plugin:0.9.17") // kotlin-docs
        classpath("com.github.ben-manes:gradle-versions-plugin:0.21.0") // version checking plugin

        // deploy to bintray
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

subprojects {
    apply(from = rootProject.file("dsl/ktlint.gradle"))
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
    delete(rootProject.file("artifacts"))
}

// Register extras.
extra["build_date"] = Date()
extra["android_studio"] = hasProperty("devBuild")
extra["army_knife_version"] = "1.2"

if (file("private/configs.gradle.kts").isFile) {
    apply(from = "private/configs.gradle.kts")
}
