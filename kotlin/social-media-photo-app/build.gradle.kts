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
    testImplementation(kotlin("test"))
}

application {
    mainClass = "com.rbleggi.socialmedia.MainKt"
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
