package com.famas.doodlekingkmm.di

import com.famas.doodlekingkmm.BuildKonfig
import com.famas.doodlekingkmm.core.util.Constants
import com.famas.doodlekingkmm.data.models.*
import com.famas.doodlekingkmm.data.remote.api.GameClient
import com.famas.doodlekingkmm.data.remote.api.HomeScreenApi
import com.famas.doodlekingkmm.data.remote.api.HomeScreenApiImpl
import com.famas.doodlekingkmm.data.remote.api.KtorGameClient
import com.famas.doodlekingkmm.data.repositories.GameScreenRepoImpl
import com.famas.doodlekingkmm.data.repositories.HomeScreenRepoImpl
import com.famas.doodlekingkmm.domain.repositories.GameScreenRepo
import com.famas.doodlekingkmm.domain.repositories.HomeScreenRepo
import com.famas.doodlekingkmm.presentation.screen_game.GameScreenVM
import com.famas.doodlekingkmm.presentation.screen_home.HomeScreenVM
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun getAllModules() = listOf(mainModule, platformModule)


val mainModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
                polymorphic(BaseModel::class) {
                    subclass(JoinRoom::class, JoinRoom.serializer())
                    subclass(Announcement::class, Announcement.serializer())
                    subclass(DrawData::class, DrawData.serializer())
                    subclass(ChatMessage::class, ChatMessage.serializer())
                    subclass(GameError::class, GameError.serializer())
                    subclass(ChosenWord::class, ChosenWord.serializer())
                    subclass(PhaseChange::class, PhaseChange.serializer())
                    subclass(GameState::class, GameState.serializer())
                    subclass(GameError::class, GameError.serializer())
                    subclass(NewWords::class, NewWords.serializer())
                    subclass(Ping::class, Ping.serializer())
                    subclass(PlayerData::class, PlayerData.serializer())
                    subclass(PlayerList::class, PlayerList.serializer())
                }
            }
        }
    }

    single {
        HttpClient(CIO) {
            defaultRequest {
                url(BuildKonfig.BASE_URL)
                contentType(ContentType.Application.Json)
            }

            install(Logging) {

            }

            install(ContentNegotiation) {
                json(get())
            }

            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(get<Json>())
            }
        }
    }

    single<HomeScreenApi> {
        HomeScreenApiImpl(get())
    }

    single<HomeScreenRepo> {
        HomeScreenRepoImpl(get())
    }

    single<GameClient> {
        KtorGameClient(get(), get())
    }

    single<GameScreenRepo> {
        GameScreenRepoImpl(get())
    }

    factory { HomeScreenVM(get()) }

    factory { GameScreenVM(get()) }
}