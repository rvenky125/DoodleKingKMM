import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.ExperimentalComposeLibrary
import java.util.Properties

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
}

fun composeDependency(groupWithArtifact: String) = "$groupWithArtifact:1.3.0"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Kotlin.coroutines_core)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)
                implementation(compose.animationGraphics)
                implementation(compose.ui)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(Deps.Ktor.core)
                implementation(Deps.Ktor.logging)
                implementation(Deps.Ktor.webSockets)
                implementation(Deps.Ktor.contentNegotiation)
                implementation(Deps.Ktor.kotlinxJson)
                implementation(Deps.Ktor.cioClient)

                implementation(Deps.Koin.core)
                implementation(Deps.Koin.compose)

                implementation(Deps.Voyager.navigator)
                implementation(Deps.Voyager.transitions)
                implementation(Deps.Voyager.koin)
                implementation(Deps.Napier.napier)
                implementation(Deps.MultiplatformSettings.noArg)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.AndroidX.viewmodelKtx)
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.famas.doodlekingkmm"
    compileSdk = 34

    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

buildkonfig {
    packageName = "com.famas.doodlekingkmm"

    val localProps = Properties()
    localProps.load(project.rootProject.file("local.properties").inputStream())
    val baseUrl = localProps.getProperty("BASE_URL")
    val webSocketUrl = localProps.getProperty("WEB_SOCKET_BASE_URL")

    defaultConfigs {
        buildConfigField(STRING, "BASE_URL", baseUrl)
        buildConfigField(STRING, "WEB_SOCKET_BASE_URL", webSocketUrl)
    }
}