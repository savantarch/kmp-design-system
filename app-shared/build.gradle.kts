import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    alias(libs.plugins.android.kotlin.multiplatform.library)
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.skie)
    id("com.savantarch.designsystem")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    androidLibrary {
        namespace = "com.savantarch.shared"
        compileSdk = 36
        minSdk = 29
        androidResources {
            enable = true
        }
    }

    val xcf = XCFramework("Shared")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            export(project(":design-system"))
            xcf.add(this)
            freeCompilerArgs = freeCompilerArgs + "-Xbinary=bundleId=com.savantarch.shared"
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":design-system"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.coil.compose)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

skie {
    analytics {
        enabled.set(false)
    }
}

designSystem {
    xcassetsDir.set(layout.projectDirectory.dir("src/iosMain/resources/Assets.xcassets"))
    resourcesDir.set(layout.projectDirectory.dir("src/iosMain/resources"))
}
