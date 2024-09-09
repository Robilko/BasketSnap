plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = libs.versions.namespace.core.base.get()
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
    //    core
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.kotlinx.collections.immutable)
    api(libs.kotlinx.serialization.json)

    //    di
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)

    //    navigation
    api(libs.androidx.navigation)
    api(libs.androidx.hilt.compose)
}