plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = libs.versions.namespace.feature.leagueDetails.get()
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
}

dependencies {
    //    modules
    implementation(projects.core.base)
    implementation(projects.core.ui)
    implementation(projects.core.remote)
    implementation(projects.core.local)
    implementation(projects.core.model)
    implementation(projects.base.favourites)

    //    di
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)

    //  unit tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}