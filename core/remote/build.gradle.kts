plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = libs.versions.namespace.core.remote.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = libs.versions.testInstrumentationRunner.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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