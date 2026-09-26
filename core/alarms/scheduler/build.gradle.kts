plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = libs.versions.packageName.get() + ".alarms.scheduler"

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
    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    // Datetime
    implementation(libs.kotlinx.datetime)

    // Coroutines
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(platform(libs.koin.bom))
    testImplementation(platform(libs.kotlinx.coroutines.bom))
    testImplementation(libs.bundles.test)
}
