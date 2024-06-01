plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.apolloGraphql)
}

dependencies {
    api(libs.apollo.runtime)
    api(libs.apollo.adapters)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

apollo {
    service("spentify") {
        packageName.set("com.nestor.schema")
        mapScalar("DateTime", "java.util.Date")
        introspection {
            headers.set(mapOf("Authorization" to "Bearer: ${System.getenv("INTROSPECTION_TOKEN")}"))
            endpointUrl.set("https://spentify.nestorperez.dev/graphql/")
            schemaFile.set(file("app/lib/schema/src/main/graphql/schema.sdl"))
        }
    }
}
