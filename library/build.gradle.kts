plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)

    id(libs.plugins.conventions.lint.get().pluginId)
}

group = "app.futured.donut"

kotlin {
    jvmToolchain(ProjectSettings.Kotlin.JvmToolchainVersion)
}


android {
    namespace = "app.futured.donut"
    compileSdk = ProjectSettings.targetSdk

    defaultConfig {
        minSdk = ProjectSettings.minSdkLibrary
    }

    testOptions {
        targetSdk = ProjectSettings.targetSdk
    }

    lint {
        targetSdk = ProjectSettings.targetSdk
    }
}

dependencies {
    implementation(libs.androidx.ktx)
}

mavenPublishing {
    coordinates(
        groupId = "app.futured.donut",
        artifactId = "donut"
    )

    pom {
        name = "Donut"
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
