plugins {
    java
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

group = "com.rbleggi"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.rbleggi.templaterenderer.Main")
}

dependencies {
    implementation("com.itextpdf:itext-core:9.1.0")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
