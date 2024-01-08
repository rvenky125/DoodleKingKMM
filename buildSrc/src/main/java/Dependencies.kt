object Versions {
    const val koin = "3.5.0"
    const val koin_compose = "1.1.0"
    const val voyagerVersion = "1.0.0-rc05"
    const val napierVersion = "2.6.1"
    const val ktorVersion = "2.2.4"
    const val multiplatformSettings = "1.0.0"
    const val viewmodelKtx = "2.6.2"
    const val coroutinesCore = "1.6.4"
}

object Deps {

    object Koin {
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val test = "io.insert-koin:koin-test:${Versions.koin}"
        const val android = "io.insert-koin:koin-android:${Versions.koin}"
        const val compose = "io.insert-koin:koin-compose:${Versions.koin_compose}"
    }

    object Voyager {
        // Navigator
        const val navigator = "cafe.adriel.voyager:voyager-navigator:${Versions.voyagerVersion}"
        const val transitions = "cafe.adriel.voyager:voyager-transitions:${Versions.voyagerVersion}"
        const val koin = "cafe.adriel.voyager:voyager-koin:${Versions.voyagerVersion}"
    }

    object Napier {
        const val napier = "io.github.aakira:napier:${Versions.napierVersion}"
    }

    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Versions.ktorVersion}"
        const val logging = "io.ktor:ktor-client-logging:${Versions.ktorVersion}"
        const val webSockets = "io.ktor:ktor-client-websockets:${Versions.ktorVersion}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktorVersion}"
        const val kotlinxJson = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktorVersion}"
        const val cioClient = "io.ktor:ktor-client-cio:${Versions.ktorVersion}"
    }

    object MultiplatformSettings {
        const val noArg = "com.russhwolf:multiplatform-settings-no-arg:${Versions.multiplatformSettings}"
    }

    object AndroidX {
        const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodelKtx}"
    }

    object Kotlin {
        const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    }
}