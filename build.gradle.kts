    // Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.javapoet) // fixes a compilation error (library version conflict with dagger-hilt)
    }
}

plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.jetbrains.kotlin.android).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.dagger.hilt.android).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.kotlin.parcelize).apply(false)
}