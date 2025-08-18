package app.futured.donut

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.configure

/**
 * This task performs necessary static analysis checks for all relevant
 * gradle modules (eg. subprojects) and generates reports that can be then picked up
 * by CI.
 */
open class LintCheckTask : DefaultTask() {

    init {
        group = ProjectSettings.Gradle.TaskGroup

        /*
        These tasks runs for each module that has applied ktlint or detekt plugins.

        The filtering is needed due to the fact that some gradle subprojects (such as :shared)
        do not have any gradle configuration, nor source files -- acting as an "umbrella" module.
        */
        configure<ExtraPropertiesExtension> {
            project.subprojects
                .filter { it.plugins.hasPlugin("org.jlleitschuh.gradle.ktlint") }
                .forEach {
                    dependsOn("${it.path}:ktlintCheck")
                }

            project.subprojects
                .filter { it.plugins.hasPlugin("io.gitlab.arturbosch.detekt") }
                .forEach {
                    dependsOn("${it.path}:detekt")
                }

            project.subprojects
                .filter { it.plugins.hasPlugin("com.android.library") || it.plugins.hasPlugin("com.android.application") }
                .forEach {
                    dependsOn("${it.path}:lintRelease")
                }
        }
    }
}

