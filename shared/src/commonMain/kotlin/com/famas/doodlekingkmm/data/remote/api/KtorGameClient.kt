package com.famas.doodlekingkmm.data.remote.api

import com.famas.doodlekingkmm.BuildKonfig
import com.famas.doodlekingkmm.data.models.BaseModel
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorGameClient(
    private val httpClient: HttpClient,
    private val json: Json
) : GameClient {
    private var webSocketSession: WebSocketSession? = null
    override suspend fun sendBaseModel(baseModel: BaseModel) {
        webSocketSession?.outgoing?.send(Frame.Text(json.encodeToString(baseModel))) ?: kotlin.run {
            Napier.d(tag = "myTag") { "Web socket is null" }
        }
    }

    override fun observeBaseModels(): Flow<BaseModel> {
        return flow {
            webSocketSession = httpClient.webSocketSession {
                url("${BuildKonfig.WEB_SOCKET_BASE_URL}ws/draw")
            }

            Napier.d(tag = "myTag") { "web socket: $webSocketSession" }

            val baseModelFlow = webSocketSession!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull {
                    val text = it.readText()
                    Napier.d(tag = "myTag") { text }

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
        Napier.d(tag = "myTag") { "Closing web socket" }
        webSocketSession?.close()
        webSocketSession = null
    }
}