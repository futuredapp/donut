import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.jetpackCompose
        kotlinCompilerVersion = Versions.kotlin
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
