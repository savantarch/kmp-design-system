plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.plugin.publish)
}

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    website = "https://github.com/savantarch/kmp-design-system"
    vcsUrl = "https://github.com/savantarch/kmp-design-system.git"
    plugins {
        create("designSystemPlugin") {
            id = "com.savantarch.designsystem"
            implementationClass = "com.savantarch.design.DesignSystemPlugin"
            displayName = "Design System Gradle Plugin"
            description = "Gradle plugin for compiling and generating KMP Design System assets"
            tags = listOf("kmp", "design-system")
        }
    }
}

dependencies {
    compileOnly(gradleApi())
}

group = "com.savantarch"
version = if (project.hasProperty("version")) {
    project.property("version").toString()
} else {
    "1.0.0-SNAPSHOT"
}

