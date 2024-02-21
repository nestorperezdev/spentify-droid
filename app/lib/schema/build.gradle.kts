plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.apolloGraphql)
}

dependencies {
    api(libs.apollo.runtime)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

apollo {
    service("service") {
        packageName.set("com.nestor.schema")
    }
}