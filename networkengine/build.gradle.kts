plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "dev.neoancient.sailandoar"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion: String by project
val serializationVersion: String by project
val slf4jVersion: String by project
val junitVersion: String by project
val mockitoVersion: String by project
val commonmarkVersion: String = "0.17.0"

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")
    implementation("io.ktor:ktor-client-websockets:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    implementation("org.commonmark:commonmark:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-autolink:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-gfm-strikethrough:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-ins:$commonmarkVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
}

kotlin {
    explicitApi()
}
