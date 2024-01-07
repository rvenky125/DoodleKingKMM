![doodle_king_banner](https://github.com/rvenky125/DoodleKingKMM/assets/58197145/9eb8040d-0d1f-40af-aad3-19c589da9241)
## Doodle King KMM app for Android and iOS
This project is the implementation of how one can adopt the Kmp (Kotlin multiplatform) for building Android and ios apps.
I rebuilt Philipp Lackner's doodle king api for backend. I have used the **Compose Multiplatform** to build the
UI of both platforms. If you are new to Compose multiplatform please go through: https://www.jetbrains.com/lp/compose-multiplatform/


## How to build the project
To build the project you need some basic understanding of how the KMP projects are built.
For that, you have look at: https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html<br>

To build the project first you need to clone and run [Doodle king Ktor api](https://github.com/rvenky125/DoodleKingKtor)
<br>
<br>
After that find the file `local.properties` file in the root folder of the KMM project and add below key value pairs.
````text
BASE_URL = http://[IP_ADDRESS]:7000/
WEB_SOCKET_BASE_URL = ws://[IP_ADDRESS]:7000/
````

## Demo Video

## What's Next
- Some more UI improvement.
- Add Authentication.
- Password for joining room.
- Improving overall Architecture.

## Technologies in this project
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android’s modern toolkit for building native UI.
- [Jetbrain's compose multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) - UI development for Android, iOS, Web, Desktop and many more.
- [Kotlin multiplatform](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html) - Kotlin-based development for frontend applications for Android,iOS, Web and desktop.
- [Voyager](https://github.com/adrielcafe/voyager) - Navigation library for jetpack compose and compose multiplatform.
- [Ktor](https://github.com/ktorio/ktor)) - A kotlin based networking library.
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) - Serialization library which can be used in KMM projects.
- [Koin](https://github.com/InsertKoinIO/koin) - Libray for dependency injection.
- [Multiplatform Setting](https://github.com/russhwolf/multiplatform-settings) - Key-Value pair local storage.
- [Build Konfig](https://github.com/yshrsmz/BuildKonfig) - Libray to generate build config object. I used it to store env variables.
- [Napier](https://github.com/AAkira/Napier.git) - Library for logging.


For more information about used dependencies, see [this](/buildSrc/src/main/java/Dependencies.kt) file.

## Questions

If you have any questions regarding the codebase, or you would like to discuss anything about the project hit/connect me up on :

- [Twitter](https://twitter.com/r__venky).
- [LinkedIn](https://www.linkedin.com/in/venkatesh-paithireddy-861344197)
