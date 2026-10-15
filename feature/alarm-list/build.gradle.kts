plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = libs.versions.packageName.get() + ".feature.alarmList"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    // Modules Depenency
    implementation(projects.core.ui.components)
    implementation(projects.core.mvi)
    implementation(projects.core.navigation)
    implementation(projects.core.utility.compose)
    implementation(projects.core.utility.kotlinDateTime)
    implementation(projects.core.alarms.domain)
    implementation(projects.core.alarms.scheduler)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.compose.app)
    debugImplementation(libs.bundles.compose.debug)

    // Parcelize
    implementation(libs.kotlin.parcelize.runtime)

    // Coroutines
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.bundles.coroutines)

    // Datetime
    implementation(libs.kotlinx.datetime)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    // Voyager
    implementation(libs.bundles.voyager)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(platform(libs.koin.bom))
    testImplementation(libs.bundles.test)
}
