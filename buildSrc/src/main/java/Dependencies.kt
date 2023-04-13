object Versions {
    const val koin = "3.4.0"
    const val voyagerVersion = "1.0.0-rc04"
    const val napierVersion = "2.6.1"
}

object Deps {

    object Koin {
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val test = "io.insert-koin:koin-test:${Versions.koin}"
        const val android = "io.insert-koin:koin-android:${Versions.koin}"
        const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
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
}