package app.futured.donut

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.configure

open class LintCheck : DefaultTask() {

    init {
        group = "futured"

        configure<ExtraPropertiesExtension> {
            dependsOn("detekt")
            project.subprojects.forEach {
                dependsOn("${it.name}:ktlintCheck")
            }
        }
    }
}
