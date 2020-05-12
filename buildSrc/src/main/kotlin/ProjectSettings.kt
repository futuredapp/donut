object ProjectSettings {
    const val applicationId = "app.futured.donut"
    const val targetSdk = 29
    const val minSdk = 21
    const val group = "app.futured.donut"

    /**
     * this version will be used only for local builds, jitpack will automatically provide TAG version
     */
    val version = System.getenv("BITRISE_GIT_TAG") ?: "0.0.1-TEST"

    object Donut {
        const val artifact = "donut"
        const val libraryDescription =
            "Donut is doughnut-like graph view capable of showing multiple datasets with assignable colors"
    }

    object DonutCompose {
        const val artifact = "donut-compose"
        const val libraryDescription =
            "Donut is doughnut-like graph view capable of showing multiple datasets with assignable colors" +
            " (Jetpack Compose version)"
    }

    object Publish {
        const val bintrayRepo = "donut"
        const val siteUrl = "https://github.com/thefuntasty/donut"
        const val gitUrl = "https://github.com/thefuntasty/donut.git"
        const val developerId = "TheFuntasty"
        const val developerName = "TheFuntasty"
        const val developerEmail = "ops@thefuntasty.com"
        const val licenseName = "MIT Licence"
        const val licenseUrl = "https://github.com/thefuntasty/donut/blob/master/LICENCE"
        val allLicenses = listOf("MIT")
    }
}
