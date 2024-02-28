plugins {
    alias(libs.plugins.androidApp)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.nestor.spentify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nestor.spentify"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //  datastore
    implementation(libs.data.store)

    //hilt
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.hilt)
    kapt(libs.hiltCompiler)

    //navigation
    implementation(libs.navigation.compose)

    //schema module
    implementation(project(":app:lib:schema"))
    implementation(project(":app:uikit"))
    implementation(project(":app:auth"))
    implementation(project(":app:onboarding"))

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.runtimeKtx)
    implementation(libs.activity.compose)
    implementation(platform(libs.composeBom))
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.toolingPreview)
    implementation(libs.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.composeBom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation(libs.compose.debug.tooling)
    debugImplementation(libs.compose.debug.uiTestManifest)
}