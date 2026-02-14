plugins {
    kotlin("jvm") version "2.2.20"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("FileShareMainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(25)
}
