import com.android.manifmerger.XmlDocument
import com.android.manifmerger.XmlNode
import groovy.namespace.QName
import groovy.util.IndentPrinter
import groovy.util.Node
import groovy.util.NodeList
import groovy.xml.MarkupBuilder
import groovy.xml.XmlNodePrinter
import groovy.xml.XmlParser
import org.jetbrains.kotlin.backend.common.peek
import java.io.StringWriter

plugins {
    alias(libs.plugins.androidApp)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.googleServices)
    alias(libs.plugins.compose)
    alias(libs.plugins.screenshot)
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.nestor.spentify"
    compileSdk = 35
    @Suppress("UnstableApiUsage") experimentalProperties["android.experimental.enableScreenshotTest"] =
        true

    defaultConfig {
        applicationId = "com.nestor.spentify"
        minSdk = 29
        targetSdk = 35
        versionCode = 503
        versionName = "0.0.0.503"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        if (file("sign.keystore").exists()) {
            create("release") {
                val env = System.getenv()
                storeFile = file("sign.keystore")
                storePassword = env.getOrDefault("RELEASE_STORE_PASSWORD", null)
                    ?: providers.gradleProperty("RELEASE_STORE_PASSWORD").orNull ?: ""
                keyAlias = "nessdev"
                keyPassword = env.getOrDefault("RELEASE_KEY_PASSWORD", null)
                    ?: providers.gradleProperty("RELEASE_KEY_PASSWORD").orNull ?: ""
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            if (file("sign.keystore").exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    composeCompiler {
        enableStrongSkippingMode = true
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
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
    ksp(libs.hiltCompiler)

    //navigation
    implementation(libs.navigation.compose)

    //schema module
    implementation(project(":app:schema"))
    implementation(project(":app:uikit"))
    implementation(project(":app:auth"))
    implementation(project(":app:onboarding"))
    implementation(project(":app:common"))
    implementation(project(":app:dashboard"))
    implementation(project(":app:account"))
    implementation(project(":app:expenses"))

    //coil
    implementation(libs.coilBase)
    implementation(libs.coilSvg)
    implementation(libs.coil)

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.runtimeKtx)

    //compose
    implementation(libs.activity.compose)
    implementation(platform(libs.composeBom))
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material3)
    debugImplementation(libs.compose.toolingPreview)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.composeBom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation(libs.compose.debug.tooling)
    debugImplementation(libs.compose.debug.uiTestManifest)
    screenshotTestImplementation(libs.compose.debug.tooling)

    screenshotTests {
        imageDifferenceThreshold = 0.0500f // 0.5%
    }
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

tasks.register<Copy>("moveScreenshotsTestToOutput") {
    val files = subprojects.map {
        it.file("build/outputs/screenshotTest-results/preview/debug/results/TEST-results.xml")
    }.filter { it.exists() }.map { it.readText() }.map { XmlParser().parseText(it) }
    val imageFiles = files.flatMap { file ->
        (file.get("testcase") as NodeList).flatMap { testcase ->
            ((testcase as Node).get("properties") as NodeList)
                .getAt("property")
                .map { it as Node }
                .map { it.attribute("name") as String to it.attribute("value") as String }
                .filter { File(it.second).exists() }
        }
    }
    listOf("reference", "actual", "diff").forEach { scope ->
        copy {
            from(imageFiles.filter { it.first == scope }.map { it.second })
            into(file("${project.rootDir}/build/outputs/screenshotTest-results/xml/images/${scope}/"))
        }
    }
}

tasks.register("mergeScreenshotsXmlReport") {
    val files = subprojects.map {
        it.file("build/outputs/screenshotTest-results/preview/debug/results/TEST-results.xml")
    }.filter { it.exists() }.map { it.readText() }.map { XmlParser().parseText(it) }
    doLast {
        val outputDir = file("${project.rootDir}/build/outputs/screenshotTest-results/xml/")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, "merged.xml")
        outputFile.createNewFile()
        val writer = outputFile.writer()
        val xml = MarkupBuilder(writer)
        XmlNodePrinter()
        xml.mkp.xmlDeclaration(mapOf("version" to "1.0", "encoding" to "UTF-8"))
        xml.withGroovyBuilder {
            "testsuite" {
                files.forEach { file ->
                    (file.get("testcase") as NodeList).forEach { node ->
                        val testCase = node as Node
                        "testcase"(testCase.attributes()) {
                            "properties" {
                                (testCase.get("properties") as NodeList)
                                    .getAt("property")
                                    .forEach { property ->
                                        val prop = property as Node
                                        val attrs = prop.attributes() as MutableMap<String, String>
                                        val fileName = attrs["value"]
                                        if (File(fileName.toString()).exists()) {
                                            //get file name
                                            val name = fileName.toString().substringAfterLast("/")
                                            attrs["value"] = "images/${attrs["name"]}/$name"
                                        }
                                        "property"(attrs)
                                    }
                            }
                        }
                    }
                }
            }
        }
        writer.flush()
    }
}

