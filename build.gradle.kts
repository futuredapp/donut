import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.gradle.kotlin.dsl.register

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.detekt)
    signing
    publishing
}

tasks.register<app.futured.donut.LintCheckTask>("lintCheck")
tasks.register<app.futured.donut.DependencyUpdates>("dependencyUpdates")
tasks.register<ReportMergeTask>("detektReportMerge") {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/merged.xml"))
}

project.subprojects {
    plugins.whenPluginAdded {
        if (this is SigningPlugin) {
            extensions.findByType<SigningExtension>()?.apply {
                val hasKey = project.hasProperty("SIGNING_PRIVATE_KEY")
                val hasPassword = project.hasProperty("SIGNING_PASSWORD")

                if (hasKey && hasPassword) {
                    val key = project.properties["SIGNING_PRIVATE_KEY"].toString()
                    val password = project.properties["SIGNING_PASSWORD"].toString()
                    useInMemoryPgpKeys(key, password)
                }
            }
        }
    }
}
