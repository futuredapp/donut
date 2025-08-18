plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)

    id(libs.plugins.conventions.lint.get().pluginId)
}

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
