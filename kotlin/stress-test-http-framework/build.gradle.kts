plugins {
    kotlin("jvm") version "2.2.20"
    application
}

group = "org.rbleggi"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    testImplementation(kotlin("test"))
}

application {
    mainClass = "com.rbleggi.stresstesthttpframework.MainKt"
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
