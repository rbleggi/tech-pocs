plugins {
    kotlin("jvm") version "2.2.20"
}

group = "org.rbleggi"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}
