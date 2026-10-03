plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = libs.versions.packageName.get() + ".navigation"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    // Alarms domain — AlarmId appears in this module's public destination parameters.
    api(projects.core.alarms.domain)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    // Pacelize
    implementation(libs.kotlin.parcelize.runtime)

    // Voyager
    implementation(libs.bundles.voyager)

    // Coroutines
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.bundles.coroutines)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(platform(libs.koin.bom))
    testImplementation(platform(libs.kotlinx.coroutines.bom))
    testImplementation(libs.bundles.test)
}
