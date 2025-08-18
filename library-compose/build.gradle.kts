plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.kotlin.compose)

    id(libs.plugins.conventions.lint.get().pluginId)
}

kotlin {
    jvmToolchain(ProjectSettings.Kotlin.JvmToolchainVersion)
}

android {
    namespace = "app.futured.donut.compose"
    compileSdk = ProjectSettings.targetSdk

    defaultConfig {
        minSdk = ProjectSettings.minSdkLibraryCompose
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes -= "/META-INF/*.kotlin_module"
        }
    }

    testOptions {
        targetSdk = ProjectSettings.targetSdk
    }

    lint {
        targetSdk = ProjectSettings.targetSdk
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
}
