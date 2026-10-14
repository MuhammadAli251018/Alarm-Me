plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = libs.versions.packageName.get() + ".alarms.data"

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
    // Modules Depenency
    implementation(projects.core.alarms.domain)

    // Datetime
    implementation(libs.kotlinx.datetime)

    // Coroutines
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.bundles.coroutines)

    // Database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(platform(libs.koin.bom))
    testImplementation(platform(libs.kotlinx.coroutines.bom))
    testImplementation(libs.bundles.test)
}
