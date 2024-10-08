plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.robilko.settings"

    defaultConfig{
        buildConfigField(
            "String",
            "APP_VERSION_NAME",
            "\"${libs.versions.versionName.get()}\""
        )
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
}

dependencies {
    //    modules
    implementation(projects.core.base)
    implementation(projects.core.ui)
    implementation(projects.core.local)

    //    di
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
}