plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.kotlin.compose)

    id(libs.plugins.conventions.lint.get().pluginId)
}

group = "app.futured.donut"

kotlin {
    jvmToolchain(ProjectSettings.Kotlin.JvmToolchainVersion)

    androidTarget {
        publishLibraryVariants("release")

        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
                }
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.animation)
                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "app.futured.donut.compose"
    compileSdk = ProjectSettings.targetSdk

    defaultConfig {
        minSdk = ProjectSettings.minSdkLibraryCompose
    }

    testOptions {
        targetSdk = ProjectSettings.targetSdk
    }

    lint {
        targetSdk = ProjectSettings.targetSdk
    }
}

mavenPublishing {
    coordinates(
        groupId = "app.futured.donut",
        artifactId = "donut-compose"
    )

    pom {
        name = "Donut Compose"
        description = "Doughnut-like graph view capable of displaying multiple datasets with assignable colors"
        url = "https://github.com/futuredapp/donut"

        licenses {
            license {
                name = "MIT"
                url = "https://github.com/futuredapp/donut/blob/master/LICENSE"
            }
        }
        scm {
            connection = "scm:git:git://github.com/futuredapp/donut.git"
            developerConnection = "scm:git:ssh://github.com/futuredapp/donut.git"
            url = "https://github.com/futuredapp/donut"
        }
        developers {
            developer {
                id = "futured"
                name = "Futured"
                url = "https://futured.app"
            }
        }
    }
}
