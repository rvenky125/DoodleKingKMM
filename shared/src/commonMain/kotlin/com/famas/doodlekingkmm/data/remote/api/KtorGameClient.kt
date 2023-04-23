package com.famas.doodlekingkmm.data.remote.api

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.di.json
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorGameClient(
    private val httpClient: HttpClient
): GameClient {
    private var webSocketSession: WebSocketSession? = null
    override suspend fun sendBaseModel(baseModel: BaseModel) {
        webSocketSession?.outgoing?.send(Frame.Text(json.encodeToString(baseModel)))
    }

    override fun observeBaseModels(clientId: String): Flow<BaseModel> {
        return flow {
            webSocketSession = httpClient.webSocketSession {
                url("ws://192.168.32.70:8080/ws/draw?client_id=$clientId")
            }
            val baseModelFlow = webSocketSession!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull {
                    val text = it.readText()

                    if (text == "{}") {
                        return@mapNotNull null
                    } else if (!text.contains("type")) {
                        return@mapNotNull null
                    }

                    json.decodeFromString<BaseModel>(it.readText())
                }

            emitAll(baseModelFlow)
        }
    }

    override suspend fun close() {
        Napier.d { "Closing web socket" }
        webSocketSession?.close()
        webSocketSession = null
    }
}