@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.nestor.common"
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
}

dependencies {
    //jwt
    implementation(libs.auth0.jwt.decode)

    //  hilt
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.hilt)
    ksp(libs.hiltCompiler)

    implementation(project(":app:schema"))
    implementation(project(":app:database"))
    implementation(project(":app:uikit"))

    //compose
    implementation(platform(libs.composeBom))
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.toolingPreview)
    implementation(libs.compose.material3)

    implementation(libs.androidx.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}