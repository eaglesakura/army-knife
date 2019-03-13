import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    google()
    jcenter()
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.3.21"
}

dependencies {
    "implementation"(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}