plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id(libs.plugins.conventions.lint.get().pluginId)
}

kotlin {
    jvmToolchain(ProjectSettings.Kotlin.JvmToolchainVersion)
}

android {
    namespace = "app.futured.donutsample"
    compileSdk = ProjectSettings.targetSdk

    defaultConfig {
        applicationId = ProjectSettings.applicationId
        minSdk = ProjectSettings.minSdkSample
        targetSdk = ProjectSettings.targetSdk
        versionCode = 1
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":library"))
    implementation(project(":library-compose"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
}
