object Deps {
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"

    object Plugins {
        const val detekt = "io.gitlab.arturbosch.detekt"
        const val ktlint = "org.jlleitschuh.gradle.ktlint"
        const val mavenPublish = "com.vanniktech:gradle-maven-publish-plugin:${Versions.mavenPublish}"
        const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}"
    }

    object Kotlin {
        const val gradlePlugin = "gradle-plugin"
        const val stdlib = "stdlib-jdk7"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.androidx}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    }

    object JetpackCompose {
        const val framework = "androidx.ui:ui-framework:${Versions.jetpackCompose}"
        const val foundation = "androidx.ui:ui-foundation:${Versions.jetpackCompose}"
        const val layout = "androidx.ui:ui-layout:${Versions.jetpackCompose}"
        const val material = "androidx.ui:ui-material:${Versions.jetpackCompose}"
        const val animation = "androidx.ui:ui-animation:${Versions.jetpackCompose}"
        const val tooling = "androidx.ui:ui-tooling:${Versions.jetpackCompose}"
    }
}
