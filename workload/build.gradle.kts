val kspVersion: String by project

plugins {
    id("com.google.devtools.ksp")
    kotlin("jvm")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":test-processor"))
    ksp(project(":test-processor"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
}

kotlin {
    jvmToolchain(21)
}