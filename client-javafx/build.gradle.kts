plugins {
    kotlin("jvm")
}

val serializationVersion: String by project
val slf4jVersion: String by project

dependencies {
    implementation(project(":client"))
    implementation(project(":server"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
}