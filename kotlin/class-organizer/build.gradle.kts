plugins {
    kotlin("jvm") version "2.2.20"
    application
}

group = "org.rbleggi"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass = "com.rbleggi.classorganizer.Main"
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}