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
}

application {
    mainClass = "com.rbleggi.logisticpricing.MainKt"
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
    }
}
tasks.withType<JavaCompile> {
    options.release.set(24)
}
