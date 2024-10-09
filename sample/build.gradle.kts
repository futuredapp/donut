import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
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

    sourceSets {
        getByName("main").java.srcDir("src/main/kotlin")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.jetpackComposeCompiler
    }
}

dependencies {
    implementation(kotlin(Deps.Kotlin.stdlib, KotlinCompilerVersion.VERSION))
    implementation(project(":library"))
    implementation(project(":library-compose"))

    implementation(Deps.AndroidX.ktx)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.constraintLayout)

    implementation(Deps.Compose.runtime)
    implementation(Deps.Compose.foundation)
    implementation(Deps.Compose.layout)
    implementation(Deps.Compose.material)
    implementation(Deps.Compose.animation)
    implementation(Deps.Compose.ui)
    implementation(Deps.Compose.tooling)
}
