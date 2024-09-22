plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.robilko.base_games"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    //   Modules
    implementation(projects.core.base)
    implementation(projects.core.remote)
    implementation(projects.core.model)
    implementation(projects.core.ui)

    //    di
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
}