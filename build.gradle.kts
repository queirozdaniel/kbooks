plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "dev.danielqueiroz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-server-core-jvm:2.3.5")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.5")
    implementation("io.ktor:ktor-server-status-pages:2.3.5")
    implementation("ch.qos.logback:logback-classic:1.4.4")
    implementation("org.slf4j:slf4j-api:2.0.3")
    implementation("com.typesafe:config:1.4.2")
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.h2database:h2:2.1.214")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}