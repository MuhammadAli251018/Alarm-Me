enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AlarmMe"
include(":app")
include(":core:ui")
include(":core:mvi")
include(":core:navigation")
include(":core:utility:compose")
include(":core:utility:kotlin-date-time")
include(":core:ringtone-picker")
include(":core:alarms:domain")
include(":core:alarms:data-mocks")
include(":feature:alarm-list")
include(":feature:alarm-details")
