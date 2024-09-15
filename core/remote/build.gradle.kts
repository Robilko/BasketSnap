import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "ru.robilko.remote"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes{
        release {
            buildConfigField(
                "String",
                "RAPID_API_KEY",
                "\"${localProperties["rapidApiKey"]}\""
            )
        }
        debug {
            buildConfigField(
                "String",
                "RAPID_API_KEY",
                "\"${localProperties["rapidApiKey"]}\""
            )
        }
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Modules
    implementation(projects.core.base)
    implementation(projects.core.model)
    implementation(projects.core.local)

    // retrofit2
    api(libs.retrofit)
    implementation(libs.jakewharton.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp.logging)

    //    di
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)

    //    testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}