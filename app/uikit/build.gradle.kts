@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose)
    alias(libs.plugins.screenshot)
}

android {
    namespace = "com.nestor.uikit"
    compileSdk = 34
    @Suppress("UnstableApiUsage")
    experimentalProperties["android.experimental.enableScreenshotTest"] = true

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
    //shimmer loading
    api(libs.compose.shimmerLoading)

    implementation(libs.navigation.compose)
    implementation(libs.hilt)
    ksp(libs.hiltCompiler)
    implementation(libs.androidx.ktx)
    implementation(platform(libs.composeBom))
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.toolingPreview)
    implementation(libs.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    debugImplementation(libs.compose.debug.tooling)
    screenshotTestImplementation(libs.compose.debug.tooling)
}