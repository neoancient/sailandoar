plugins {
    kotlin("jvm")
}

val serializationVersion: String by project

dependencies {
    implementation(project(":core"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    implementation("no.tornado:tornadofx:1.7.17")
}
