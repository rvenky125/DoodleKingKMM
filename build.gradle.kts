plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application") version("8.1.4") apply false
    id("com.android.library") version("8.1.4") apply false
    kotlin("android") version("1.9.21") apply false
    kotlin("multiplatform") version("1.9.21") apply false
    kotlin("plugin.serialization") version("1.8.0") apply false
    id("org.jetbrains.compose") version "1.5.11" apply false
    id("com.codingfeline.buildkonfig") version("0.15.1") apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}


