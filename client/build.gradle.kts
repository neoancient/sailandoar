plugins {
    kotlin("jvm")
}

val ktorVersion: String by project
val serializationVersion: String by project
val slf4jVersion: String by project
val junitVersion: String by project
val mockitoVersion: String by project

dependencies {
    implementation(project(":core"))
    implementation(project(":connect"))
    implementation("io.ktor:ktor-client-websockets:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
}
