plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.robilko.remote"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures{
        buildConfig = true
    }
}

dependencies {
    // Modules
    implementation(project(":core:base"))
    implementation(project(":core:model"))
    implementation(project(":core:local"))

    // retrofit2
    api(libs.retrofit)
    implementation(libs.jakewharton.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp.logging)

    //    di
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)

    //    testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
}