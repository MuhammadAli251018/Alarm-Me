plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = libs.versions.packageName.get() + ".ui"

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
    implementation(projects.core.utility.compose)
    implementation(projects.core.utility.kotlinDateTime)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    debugImplementation(libs.bundles.compose.debug)

    // Datetime
    implementation(libs.kotlinx.datetime)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Calendar
    implementation(libs.kizitonwose.calendar.compose)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(platform(libs.koin.bom))
    testImplementation(platform(libs.kotlinx.coroutines.bom))
    testImplementation(libs.bundles.test)
}
