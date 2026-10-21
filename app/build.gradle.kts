import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

object AppConfig {
    const val MAJOR_VERSION = 0
    const val MINOR_VERSION = 1
    const val PATCH_VERSION = 0
    const val VERSION_TAG = "beta"
    const val VERSION_CODE = 1

    fun getVersionName() =
        if (VERSION_TAG.isNotEmpty())
            "$MAJOR_VERSION.$MINOR_VERSION.$PATCH_VERSION-$VERSION_TAG"
        else
            "$MAJOR_VERSION.$MINOR_VERSION.$PATCH_VERSION"
}

fun gitHash(): String =
    providers.exec { commandLine("git", "rev-parse", "--short", "HEAD") }
        .standardOutput.asText.get().trim()

object SigningKeys {
    const val CONFIG_NAME = "release"
    const val KEYSTORE_PATH = "ANDROID_KEYSTORE_PATH"
    const val KEYSTORE_PASSWORD = "ANDROID_KEYSTORE_PASSWORD"
    const val KEY_ALIAS = "ANDROID_KEY_ALIAS"
    const val KEY_PASSWORD = "ANDROID_KEY_PASSWORD"
}

val localProperties: Properties? =
    providers.fileContents(rootProject.layout.projectDirectory.file("local.properties"))
        .asText.orNull
        ?.let { text -> Properties().apply { load(text.reader()) } }

fun signingValue(name: String): String? =
    (localProperties?.getProperty(name) ?: providers.environmentVariable(name).orNull)
        ?.takeIf { it.isNotBlank() }

android {
    namespace = libs.versions.packageName.get()
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = libs.versions.packageName.get()
        minSdk = 24
        targetSdk = 37
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.getVersionName()

        buildConfigField("String", "GIT_HASH_NAME", "\"${gitHash()}\"")
    }

    signingConfigs {
        val keystorePath = signingValue(SigningKeys.KEYSTORE_PATH)
        val keystorePassword = signingValue(SigningKeys.KEYSTORE_PASSWORD)
        val releaseKeyAlias = signingValue(SigningKeys.KEY_ALIAS)

        if (keystorePath != null && keystorePassword != null && releaseKeyAlias != null) {
            create(SigningKeys.CONFIG_NAME) {
                // Absolute paths pass through; relative ones resolve against the repository root.
                storeFile = rootProject.file(keystorePath)
                storePassword = keystorePassword
                keyAlias = releaseKeyAlias
                keyPassword = signingValue(SigningKeys.KEY_PASSWORD) ?: keystorePassword
            }
        }
    }

    buildTypes {
        release {
            // Absent on a contributor machine without the keystore: release stays buildable, unsigned.
            signingConfigs.findByName(SigningKeys.CONFIG_NAME)?.let { signingConfig = it }

            optimization {
                enable = true
            }
        }
    }

    flavorDimensions += listOf("ENVIRONMENT")

    productFlavors {
        create("dev") {
            dimension = "ENVIRONMENT"
            applicationIdSuffix = ".dev"
        }

        create("prod") {
            dimension = "ENVIRONMENT"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    // Modules Depenency
    implementation(projects.core.featureFlags)
    implementation(projects.core.appSettings)
    implementation(projects.core.ui.components)
    implementation(projects.core.navigation)
    implementation(projects.core.ui.screen.ringtonePicker)
    implementation(projects.core.alarms.domain)
    implementation(projects.core.alarms.data)
    implementation(projects.core.alarms.dataMocks)
    implementation(projects.core.alarms.scheduler)
    implementation(projects.core.preferences)
    implementation(projects.core.utility.kotlinDateTime)
    implementation(projects.feature.alarmList)
    implementation(projects.feature.alarmDetails)
    implementation(projects.feature.settings)
    implementation(projects.feature.debugPanel)
    implementation(projects.feature.alarmRinging)

    implementation(libs.kotlinx.datetime)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.compose.app)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.bundles.coroutines)

    implementation(libs.bundles.voyager)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)

    debugImplementation(libs.bundles.compose.debug)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(platform(libs.koin.bom))
    testImplementation(platform(libs.kotlinx.coroutines.bom))
    testImplementation(libs.bundles.test)
}
