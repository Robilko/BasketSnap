import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.triplet.play)
}

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "ru.robilko.basket_snap"

    signingConfigs {
        create("release") {
            storeFile = file(localProperties["keystorePath"] as String)
            storePassword = localProperties["keystorePassword"] as String
            keyAlias = localProperties["keyAlias"] as String
            keyPassword = localProperties["keyPassword"] as String
        }
    }

    defaultConfig {
        applicationId = "ru.robilko.basket_snap"
        targetSdk = 34
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
        ksp { arg("room.schemaLocation", "$projectDir/schemas") }

        playConfigs {
            create("release") {
                enabled.set(true)
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk { debugSymbolLevel = "FULL" }
        }
        debug {
            resValue("string", "app_name", "BasketSnap (Debug)")
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

play {
    enabled.set(false)
    track.set("production")
    userFraction.set(1.0) // 100%
    defaultToAppBundles.set(true)
    releaseStatus.set(ReleaseStatus.DRAFT)
    serviceAccountCredentials.set(file(localProperties["credentialsGoogleCloudPath"] as String))
}

dependencies {
    //    modules
    implementation(projects.core.base)
    implementation(projects.core.ui)
    implementation(projects.core.remote)
    implementation(projects.core.local)
    implementation(projects.core.model)
    implementation(projects.base.favourites)
    implementation(projects.base.games)
    implementation(projects.base.seasons)
    implementation(projects.feature.home)
    implementation(projects.feature.favourites)
    implementation(projects.feature.settings)
    implementation(projects.feature.leagues)
    implementation(projects.feature.leagueDetails)
    implementation(projects.feature.teams)
    implementation(projects.feature.teamDetails)
    implementation(projects.feature.games)

    //    splashscreen
    implementation(libs.androidx.core.splashscreen)

    //    di
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)

    // ui tests
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
}

tasks.register("bundleAndPublishGoogleRelease") {
    dependsOn("bundleRelease")
    finalizedBy("publishReleaseBundle")
}