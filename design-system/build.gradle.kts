import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.skie)
    alias(libs.plugins.vanniktech.maven.publish)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    androidLibrary {
        namespace = "com.savantarch.design"
        compileSdk = 36
        minSdk = 29
        androidResources {
            enable = true
        }
    }

    val xcf = XCFramework("DesignSystem")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DesignSystem"
            isStatic = true
            xcf.add(this)
            freeCompilerArgs = freeCompilerArgs + "-Xbinary=bundleId=com.savantarch.designsystem"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
            }
        }

        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.material)
        }
    }
}

skie {
    analytics {
        enabled.set(false)
    }
}


mavenPublishing {
    coordinates("com.savantarch", "design-system", if (project.hasProperty("version")) project.property("version").toString() else "1.0.0-SNAPSHOT")

    pom {
        name.set("KMP Design System Core")
        description.set("Core design system themes and resources for Kotlin Multiplatform applications")
        inceptionYear.set("2024")
        url.set("https://github.com/savantarch/kmp-design-system")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("savantarch")
                name.set("Savant Arch")
                email.set("anubhav@savantarch.com")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/savantarch/kmp-design-system.git")
            developerConnection.set("scm:git:ssh://github.com/savantarch/kmp-design-system.git")
            url.set("https://github.com/savantarch/kmp-design-system")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}



