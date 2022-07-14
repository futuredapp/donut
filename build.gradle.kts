import app.futured.donut.LintCheck
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Deps.gradlePlugin)
        classpath(kotlin(Deps.Kotlin.gradlePlugin, Versions.kotlin))
        classpath(Deps.Plugins.mavenPublish)
        classpath(Deps.Plugins.dokka)
    }
}

plugins {
    idea
    id(Deps.Plugins.detekt) version Versions.detekt
    id(Deps.Plugins.ktlint) version Versions.ktlint
    signing
    publishing
}

tasks {
    register<LintCheck>("lintCheck")
    register<app.futured.donut.DependencyUpdates>("dependencyUpdates")
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = Deps.Plugins.ktlint)

    ktlint {
        version.set(Versions.ktlintExtension)
        ignoreFailures.set(true)
        android.set(true)
        outputToConsole.set(true)
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
    }
}

detekt {
    version = Versions.detekt
    input = files(rootDir)
    config = files("detekt.yml")
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
