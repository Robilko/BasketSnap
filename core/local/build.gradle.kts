plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ru.robilko.local"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    // Modules
    implementation(projects.core.base)
    implementation(projects.core.model)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)


    //    di
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
}