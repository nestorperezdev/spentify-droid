plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.apolloGraphql)
}

android {
    namespace = "com.nestor.schema"
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
}

dependencies {
    api(libs.apollo.runtime)
    api(libs.apollo.adapters)
}

apollo {
    service("spentify") {
        packageName.set("com.nestor.schema")
        mapScalar("DateTime", "java.util.Date")
        introspection {
            headers.set(mapOf("Authorization" to "Bearer: ${System.getenv("INTROSPECTION_TOKEN")}"))
            endpointUrl.set(findProperty("endpoint") as String? ?: "invalid url")
            schemaFile.set(file("src/main/graphql/schema.sdl"))
        }
    }
}