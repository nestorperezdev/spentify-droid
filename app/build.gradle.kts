import org.apache.commons.logging.LogFactory.release

plugins {
    alias(libs.plugins.androidApp)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.googleServices)
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.nestor.spentify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nestor.spentify"
        minSdk = 29
        targetSdk = 34
        versionCode = 501
        versionName = "0.0.0.501"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val env = System.getenv()
            storeFile = file("sign.keystore")
            storePassword =
                env.getOrDefault("RELEASE_STORE_PASSWORD", null)
                    ?: providers.gradleProperty("RELEASE_STORE_PASSWORD").orNull
                            ?: ""
            keyAlias = "nessdev"
            keyPassword =
                env.getOrDefault("RELEASE_KEY_PASSWORD", null)
                    ?: providers.gradleProperty("RELEASE_KEY_PASSWORD").orNull
                            ?: ""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            versionNameSuffix = "-debug"
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
    //firebase
    implementation(platform(libs.firebaseBom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

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
    implementation(project(":app:common"))
    implementation(project(":app:dashboard"))
    implementation(project(":app:account"))
    implementation(project(":app:expenses"))

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

tasks.create("bumpVersionCode") {
    description = "Bump the versionCode by 1 and write it to the gradle file."
    fun parseVersionName(versionName: String): Triple<Int, Int, Int> {
        val (major, minor, patch) = versionName.split(".").map { it.toInt() }
        return Triple(major, minor, patch)
    }
    doLast {
        val versionCode = (android.defaultConfig.versionCode!!) + 1
        val versionName = parseVersionName(android.defaultConfig.versionName!!)
        //write back to the gradle file
        val gradleFile = file("build.gradle.kts")
        gradleFile.writeText(
            gradleFile.readText()
                .replace("versionCode = ${versionCode - 1}", "versionCode = $versionCode")
        )
        gradleFile.writeText(
            gradleFile.readText().replace(
                "versionName = \"${versionName.first}.${versionName.second}.${versionName.third}.${versionCode - 1}\"",
                "versionName = \"${versionName.first}.${versionName.second}.${versionName.third}.$versionCode\""
            )
        )
    }
}