package com.famas.doodlekingkmm.di

import com.famas.doodlekingkmm.BuildKonfig
import com.famas.doodlekingkmm.data.models.Announcement
import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.ChatMessage
import com.famas.doodlekingkmm.data.models.ChosenWord
import com.famas.doodlekingkmm.data.models.DrawData
import com.famas.doodlekingkmm.data.models.GameError
import com.famas.doodlekingkmm.data.models.GameState
import com.famas.doodlekingkmm.data.models.JoinRoom
import com.famas.doodlekingkmm.data.models.NewWords
import com.famas.doodlekingkmm.data.models.PhaseChange
import com.famas.doodlekingkmm.data.models.Ping
import com.famas.doodlekingkmm.data.models.PlayerData
import com.famas.doodlekingkmm.data.models.PlayerList
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
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
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