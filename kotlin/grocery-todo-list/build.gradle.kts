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
    mainClass = "com.rbleggi.grocerytodolist.Main"
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}