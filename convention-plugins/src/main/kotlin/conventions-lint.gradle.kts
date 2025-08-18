import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.gradle.accessors.dm.LibrariesForLibs
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

/*
This is a Precompiled Script Plugin that unifies lint
configuration across multiple Gradle modules in the project.

https://docs.gradle.org/current/userguide/custom_plugins.html#sec:precompiled_plugins
 */

// As of now, we cannot use Gradle version catalogs with Precompiled Script plugins: https://github.com/gradle/gradle/issues/15383
plugins {
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
}

val libs = the<LibrariesForLibs>()

// Ktlint
project.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set(libs.versions.ktlint)
    ignoreFailures.set(true)
    android.set(true)
    outputToConsole.set(true)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }

    // filtering does not work, see below 👇
}

// Detekt
project.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
    ignoreFailures = false
    source.setFrom(files(
        "src/main/kotlin",
        "src/commonMain/kotlin",
        "src/jvmMain/kotlin",
        "src/androidMain/kotlin",
        "src/appleMain/kotlin",
        "src/iosMain/kotlin",
    ))
    config.setFrom(files("$rootDir/detekt.yml"))
    buildUponDefaultConfig = true
}

tasks.withType<Detekt> {
    reports {
        sarif.required.set(false)
        md.required.set(false)
        txt.required.set(true)
        xml.required.set(true)
    }

    finalizedBy(rootProject.tasks.getByName("detektReportMerge"))
}

/**
 * Configure task [runKtlintCheckOverCommonMainSourceSet] to run After KSP generation task
 * Setup this only if project has configured ksp and has [kspCommonMainKotlinMetadata] in it's tasks
 *
 * This is probably a bug on ktlint-gradle side. The filtering in KtlintExtension does not work so ktlint wants to also check generated files.
 * I managed to get everything working only by combining .editorconfig ktlint = disable trick with combination of this ugly monstrosity.
 *
 * Number of lost souls: 1
 * (Increase this number if you lost several hours of your life trying to resolve this issue)
 *
 * Refs:
 * - https://github.com/JLLeitschuh/ktlint-gradle/issues/746
 * - https://github.com/JLLeitschuh/ktlint-gradle/issues/751
 */
tasks.matching { it.name == "runKtlintCheckOverCommonMainSourceSet" }.configureEach {
    if (project.tasks.findByName("kspCommonMainKotlinMetadata") != null) {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

/**
 * This is ktlint-gradle bug workaround. Ktlint should **not** have to require formatting generated files, the filtering does not work
 * and suggested workarounds do not work as well. So here we are, generating sources using KSP so ktlint can be satisfied 🤠.
 *
 * Number of lost souls: 1
 * (Increase this number if you lost several hours of your life trying to resolve this issue)
 *
 * Refs:
 * - https://github.com/JLLeitschuh/ktlint-gradle/issues/724
 */
tasks.matching { it.name == "runKtlintFormatOverCommonMainSourceSet" }.configureEach {
    if (project.tasks.findByName("kspCommonMainKotlinMetadata") != null) {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

rootProject.tasks.named("detektReportMerge", ReportMergeTask::class.java) {
    input.from(tasks.withType<Detekt>().map { it.xmlReportFile })
}
