plugins {
    kotlin("jvm")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

kotlin {
    jvmToolchain(21)
}