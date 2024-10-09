import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "app.futured.donut.compose"
    compileSdk = ProjectSettings.targetSdk

    defaultConfig {
        minSdk = ProjectSettings.minSdkLibraryCompose
        targetSdk = ProjectSettings.targetSdk
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

    packagingOptions {
        resources {
            excludes -= "/META-INF/*.kotlin_module"
        }
    }
}

dependencies {
    implementation(kotlin(Deps.Kotlin.stdlib, KotlinCompilerVersion.VERSION))
    implementation(Deps.AndroidX.ktx)
    implementation(Deps.AndroidX.appcompat)
    implementation(platform("androidx.compose:compose-bom:2024.09.03"))

    implementation(Deps.Compose.foundation)
    implementation(Deps.Compose.layout)
    implementation(Deps.Compose.animation)
    implementation(Deps.Compose.ui)
    implementation(Deps.Compose.tooling)
}
