pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.settings") version "8.5.2"
}

android {
    compileSdk = 34
    minSdk = 28
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "BasketSnap"
include(":app")
include(":core:base")
include(":core:ui")
include(":core:remote")
include(":core:local")
include(":core:model")
include(":base:favourites")
include(":feature:home")
include(":feature:favourites")
include(":feature:settings")
include(":feature:leagues")
include(":feature:league_details")
include(":feature:teams")
include(":feature:team_details")
include(":feature:games")
