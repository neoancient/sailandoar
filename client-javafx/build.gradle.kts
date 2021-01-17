plugins {
    kotlin("jvm")
}

val serializationVersion: String by project

dependencies {
    implementation(project(":client"))
    implementation(project(":server"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    implementation("no.tornado:tornadofx:1.7.20")
}