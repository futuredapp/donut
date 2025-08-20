plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.ktlint)
    implementation(libs.gradlePlugin.detekt)
    implementation(libs.gradlePlugin.ksp)
}
