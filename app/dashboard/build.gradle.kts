@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.nestor.dashboard"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures { // Enables Jetpack Compose for this module
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    composeCompiler {
        enableStrongSkippingMode = true
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        
    }
}

dependencies {
    //  hilt
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.hilt)
    ksp(libs.hiltCompiler)

    implementation(libs.compose.viewmodel)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.runtimeKtx)
    implementation(libs.activity.compose)
    implementation(platform(libs.composeBom))
    implementation(project(":app:uikit"))
    implementation(project(":app:schema"))
    implementation(project(":app:database"))
    implementation(project(":app:common"))
    implementation(project(":app:auth"))
    implementation(libs.compose.graphics)
    implementation(libs.compose.material3)
    implementation(libs.compose.toolingPreview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}