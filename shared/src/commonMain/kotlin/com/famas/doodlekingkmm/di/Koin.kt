package com.famas.doodlekingkmm.di

import com.famas.doodlekingkmm.data.models.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
) = startKoin {
    appDeclaration()
    modules(mainModule)
}


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


}

