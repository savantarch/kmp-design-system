package com.savantarch.design

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy

interface DesignSystemExtension {
    val xcassetsDir: DirectoryProperty
    val resourcesDir: DirectoryProperty
    val minimumDeploymentTarget: Property<String>
    val targetDevices: ListProperty<String>
}

class DesignSystemPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create(
                "designSystem",
                DesignSystemExtension::class.java
            ).apply {
                xcassetsDir.convention(project.layout.projectDirectory.dir("src/iosMain/resources/Assets.xcassets"))
                resourcesDir.convention(project.layout.projectDirectory.dir("src/iosMain/resources"))
                minimumDeploymentTarget.convention("15.0")
                targetDevices.convention(listOf("iphone", "ipad"))
            }

        val compileAssetCatalog = project.tasks.register("compileAssetCatalog") { task ->
            task.description = "Compile iOS Asset Catalogs using actool"
            task.onlyIf {
                val isMac = System.getProperty("os.name").lowercase().contains("mac")
                val assetsExist = extension.xcassetsDir.orNull?.asFile?.exists() == true
                isMac && assetsExist
            }

            task.inputs.dir(extension.xcassetsDir).optional()
            val outputDir = project.layout.buildDirectory.dir("ios/resources/design-system.bundle")
            task.outputs.dir(outputDir)

            task.doLast {
                val xcassets = extension.xcassetsDir.get().asFile
                if (!xcassets.exists()) {
                    project.logger.warn("Source Assets.xcassets directory not found: ${xcassets.absolutePath}")
                    return@doLast
                }

                val outDir = outputDir.get().asFile
                outDir.mkdirs()

                val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
                val platform = if (sdkName.startsWith("iphoneos")) {
                    "iphoneos"
                } else {
                    "iphonesimulator"
                }

                project.logger.lifecycle("Compiling asset catalog for platform: $platform (from SDK_NAME: $sdkName) into bundle: design-system")

                val deviceArgs =
                    extension.targetDevices.get().flatMap { listOf("--target-device", it) }
                val execArgs = mutableListOf(
                    "xcrun", "actool",
                    xcassets.absolutePath,
                    "--compile", outDir.absolutePath,
                    "--platform", platform,
                    "--minimum-deployment-target", extension.minimumDeploymentTarget.get()
                ).apply {
                    addAll(deviceArgs)
                }

                project.exec { execSpec ->
                    execSpec.commandLine(execArgs)
                }
            }
        }

        val createIosBundle = project.tasks.register(
            "createIosBundle",
            Copy::class.java
        ) { task ->
            task.description = "Create iOS resource bundle"
            task.dependsOn(compileAssetCatalog)
            task.onlyIf {
                extension.resourcesDir.orNull?.asFile?.exists() == true
            }

            task.from(extension.resourcesDir) { copySpec ->
                copySpec.exclude("Assets.xcassets/**")
            }
            val outputDir = project.layout.buildDirectory.dir("ios/resources/design-system.bundle")
            task.into(outputDir)
        }

        project.tasks.matching {
            it.name.contains("ProcessResources")
                    || (it.name.startsWith("link") && it.name.contains("Framework"))
                    || it.name.startsWith("compileKotlinIos")
        }.configureEach { task ->
            task.dependsOn(createIosBundle)
        }
    }
}
