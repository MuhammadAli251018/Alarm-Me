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

// Presentation
include(":core:mvi")
include(":core:navigation")

// UI
include(":core:ui:theme")
include(":core:ui:components")
include(":core:ui:screen:date-picker")
include(":core:ui:screen:time-picker")
include(":core:ui:screen:ringtone-picker")
include(":core:ui:screen:snooze-picker")
include(":core:ui:screen:label-picker")

// Utility
include(":core:utility:compose")
include(":core:utility:kotlin-date-time")

// Preferences
include(":core:preferences")
include(":core:app-settings")


// Core Alarms
include(":core:alarms:domain")
include(":core:alarms:scheduler")
include(":core:alarms:data")
include(":core:alarms:data-mocks")

// Dev Tools
include(":core:feature-flags")

// Features
include(":feature:alarm-list")
include(":feature:alarm-details")
include(":feature:alarm-ringing")
include(":feature:settings")
include(":feature:debug-panel")
