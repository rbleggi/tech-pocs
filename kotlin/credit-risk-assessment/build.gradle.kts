plugins {
    kotlin("jvm") version "2.1.10"
    application
}

group = "com.rbleggi"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

application {
    mainClass = "com.rbleggi.creditriskassessment.MainKt"
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}
