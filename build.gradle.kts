import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
}

group = "me.professional"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("com.apporiented:hierarchical-clustering:1.1.0"))
    implementation("com.apporiented:hierarchical-clustering:1.1.0")
    implementation("com.github.haifengl:smile-kotlin:2.6.0")
    implementation("com.github.haifengl:smile-plot:2.6.0")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}