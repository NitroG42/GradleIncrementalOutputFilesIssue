import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

group = "com.nitro"
version = "1.0-SNAPSHOT"

gradlePlugin {
    plugins {
        create("test-plugin") {
            id = "com.nitro.test"
            implementationClass = "com.nitro.test.TestPlugin"
            version = "0.1"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(gradleTestKit())
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.majorVersion
}