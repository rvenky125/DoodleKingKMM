import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

val ktorVersion = "2.2.4"

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig") version "0.15.1"
}

fun composeDependency(groupWithArtifact: String) = "$groupWithArtifact:1.3.0"

kotlin {
    android {
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
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.material)
                api(compose.materialIconsExtended)
                api(compose.animation)
                api(compose.animationGraphics)
                api(compose.ui)

                api("io.ktor:ktor-client-core:$ktorVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                api("io.ktor:ktor-client-logging:$ktorVersion")
                api("io.ktor:ktor-client-websockets:$ktorVersion")
                api("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                api("io.ktor:ktor-client-cio:$ktorVersion")
                api(Deps.Koin.core)
                api(Deps.Koin.compose)

                api(Deps.Voyager.navigator)
                api(Deps.Voyager.transitions)
                api(Deps.Voyager.koin)
                implementation(Deps.Napier.napier)
                implementation("com.russhwolf:multiplatform-settings-no-arg:1.0.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
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
    defaultConfig {
        minSdk = 24
        targetSdk = 34
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