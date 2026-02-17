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
    mainClass = "com.rbleggi.anomalydetection.MainKt"
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.release.set(23)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_23)
    }
}
