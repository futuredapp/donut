pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.buildFileName = "build.gradle.kts"

include(":library")
include(":library-compose")
include(":sample")

includeBuild("convention-plugins")