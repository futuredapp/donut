@Deprecated("Use version catalog instead")
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
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    }

    object Compose {
        const val runtime = "androidx.compose.runtime:runtime:${Versions.jetpackCompose}"
        const val foundation = "androidx.compose.foundation:foundation:${Versions.jetpackCompose}"
        const val animation = "androidx.compose.animation:animation:${Versions.jetpackCompose}"
        const val layout = "androidx.compose.foundation:foundation-layout:${Versions.jetpackCompose}"
        const val material = "androidx.compose.material:material:${Versions.jetpackCompose}"
        const val ui = "androidx.compose.ui:ui:${Versions.jetpackCompose}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.jetpackCompose}"
    }
}
