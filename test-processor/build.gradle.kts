val kspVersion: String by project

plugins {
    kotlin("jvm")
}

group = "com.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:javapoet:1.12.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation(kotlin("reflect"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

kotlin {
    jvmToolchain(21)
}