@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.screenshot)
}

android {
    namespace = "com.nestor.auth"
    compileSdk = 34
    @Suppress("UnstableApiUsage")
    experimentalProperties["android.experimental.enableScreenshotTest"] = true

    screenshotTests {
        imageDifferenceThreshold = 0.0500f // 0.5%
    }

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
    api(project(":app:database"))
    implementation(project(":app:common"))
    implementation(project(":app:schema"))
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material3)
    implementation(libs.compose.toolingPreview)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    debugImplementation(libs.compose.debug.tooling)
    debugImplementation(libs.compose.debug.uiTestManifest)
    screenshotTestImplementation(libs.compose.debug.tooling)
}