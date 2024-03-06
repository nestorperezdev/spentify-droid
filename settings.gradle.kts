pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Spentify"
include(":app")
include(":app:lib:schema")
include(":app:repository")
include(":app:uikit")
include(":app:auth")
include(":app:onboarding")
include(":app:dashboard")
include(":app:database")
