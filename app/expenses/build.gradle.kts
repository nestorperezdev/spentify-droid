plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose)
    alias(libs.plugins.screenshot)
}

android {
    namespace = "com.nestor.expenses"
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
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    buildFeatures {
        compose = true
    }
    composeCompiler {
        enableStrongSkippingMode = true
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
    }
    screenshotTests {
        imageDifferenceThreshold = 0.0500f // 0.5%
    }
}

dependencies {
    //paging
    implementation(libs.paging)
    implementation(libs.paging.compose)

    //hilt
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.hilt)
    ksp(libs.hiltCompiler)

    //compose
    implementation(platform(libs.composeBom))
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.toolingPreview)
    implementation(libs.compose.material3)

    //coil
    implementation(libs.compose.coil)

    //app modules
    implementation(project(":app:database"))
    implementation(project(":app:common"))
    implementation(project(":app:dashboard"))
    implementation(project(":app:schema"))
    implementation(project(":app:uikit"))
    implementation(project(":app:category"))

    implementation(libs.androidx.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    screenshotTestImplementation(libs.compose.debug.tooling)
}