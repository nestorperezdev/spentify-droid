// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApp) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.google.googleServices) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.screenshot) apply false
}

buildscript {
    dependencies {
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.5")
    }
}