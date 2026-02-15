plugins {
    kotlin("jvm") version "2.2.20"
    application
}

group = "com.rbleggi"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.8.0")
}

application {
    mainClass = "com.rbleggi.loggerrouter.MainKt"
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(25)
}

