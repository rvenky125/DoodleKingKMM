package com.famas.doodlekingkmm.presentation.screen_game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.benasher44.uuid.Uuid
import com.famas.doodlekingkmm.core.util.Constants
import com.famas.doodlekingkmm.core.util.settings
import com.famas.doodlekingkmm.data.models.Announcement
import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.ChatMessage
import com.famas.doodlekingkmm.data.models.ChosenWord
import com.famas.doodlekingkmm.data.models.DisconnectRequest
import com.famas.doodlekingkmm.data.models.DrawAction
import com.famas.doodlekingkmm.data.models.DrawData
import com.famas.doodlekingkmm.data.models.GameError
import com.famas.doodlekingkmm.data.models.GameState
import com.famas.doodlekingkmm.data.models.JoinRoom
import com.famas.doodlekingkmm.data.models.NewWords
import com.famas.doodlekingkmm.data.models.OffsetData
import com.famas.doodlekingkmm.data.models.Path
import com.famas.doodlekingkmm.data.models.PhaseChange
import com.famas.doodlekingkmm.data.models.Ping
import com.famas.doodlekingkmm.data.models.PlayerData
import com.famas.doodlekingkmm.data.models.PlayerList
import com.famas.doodlekingkmm.data.models.RoundDrawInfo
import com.famas.doodlekingkmm.data.remote.api.GameClient
import com.famas.doodlekingkmm.di.httpClient
import com.famas.doodlekingkmm.presentation.components.canvas.CanvasController
import com.famas.doodlekingkmm.presentation.components.canvas.CanvasData
import com.famas.doodlekingkmm.presentation.components.canvas.PathWrapper
import io.github.aakira.napier.Napier
import io.ktor.util.date.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameScreenVM(
    private val gameClient: GameClient
) : ScreenModel {
    val canvasController = CanvasController()

    var roomId: String? = null
    private val uuid = Uuid(10L, 10L).toString()

    private val _gameScreenState = mutableStateOf(GameScreenState())
    val gameScreenState: State<GameScreenState> = _gameScreenState

    fun onEvent(event: GameScreenEvent) {
        when (event) {
            is GameScreenEvent.OnSelectWord -> {
                coroutineScope.launch {
                    roomId?.let {
                        gameClient.sendBaseModel(ChosenWord(event.word, it))
                    }
                }
            }

            is GameScreenEvent.OnChangeTextInputValue -> {
                _gameScreenState.value = gameScreenState.value.copy(
                    textInputValue = event.textInputValue
                )
            }
            GameScreenEvent.OnSendMessage -> {
                coroutineScope.launch {
                    roomId?.let {
                        gameClient.sendBaseModel(ChatMessage(uuid, it, gameScreenState.value.textInputValue, 0L))
                    }
                }
            }
        }
    }

    fun drawData(drawData: DrawData) {
        val snapShotStateList = SnapshotStateList<Offset>()
        snapShotStateList.addAll(drawData.path.points.map { Offset(it.x, it.y) })
        canvasController.importPath(
            CanvasData(
                path = listOf(
                    PathWrapper(
                        snapShotStateList,
                        strokeColor = Color.Black
                    )
                )
            )
        )
    }

    fun connectToRoom(roomId: String) {
        this.roomId = roomId
        _gameScreenState.value.username?.let { user ->
            coroutineScope.launch {
                gameClient.sendBaseModel(JoinRoom(user, roomId))
            }
        } ?: kotlin.run {
            TODO() //Need navigate the user to previous screen
        }
    }

    private fun startConnection() {
        gameClient.observeBaseModels(uuid)
            .onEach {
                when (it) {
                    is ChatMessage -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    is Announcement -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    is ChosenWord -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    is DisconnectRequest -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    is DrawAction -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    is DrawData -> {
                        drawData(it)
                    }

                    is GameError -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    is GameState -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    is NewWords -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            newWords = it.newWords
                        )
                    }

                    is PhaseChange -> {
                        if (it.phase != null) {
                            _gameScreenState.value = gameScreenState.value.copy(
                                currentPhase = it.phase,
                                totalTime = it.time,
                                time = it.time,
                                drawingPlayer = it.drawingPlayer
                            )
                        } else {
                            _gameScreenState.value = gameScreenState.value.copy(
                                time = it.time,
                                drawingPlayer = it.drawingPlayer
                            )
                        }
                    }

                    is Ping -> {
                        coroutineScope.launch {
                            gameClient.sendBaseModel(Ping())
                        }
                    }

                    is PlayerData -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    is PlayerList -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            playersList = it.players
                        )
                    }

                    is RoundDrawInfo -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    else -> {}
                }
                Napier.d { it.toString() }
            }.launchIn(coroutineScope)
    }

    init {
        startConnection()

        _gameScreenState.value = gameScreenState.value.copy(
            username = settings.getStringOrNull(Constants.USERNAME_PREF_KEY)
        )

        canvasController.pathList.asFlow().onEach { pathWrapper ->
            roomId?.let { id ->
                Napier.d {
                    "Sending draw data BaseModel: ${
                        DrawData(
                            roomId = id,
                            path = Path(
                                points = pathWrapper.points.map { OffsetData(it.x, it.y) },
                                stroke = pathWrapper.strokeWidth,
                                color = pathWrapper.strokeColor.hashCode().toString()
                            )
                        )
                    }"
                }
                gameClient.sendBaseModel(
                    DrawData(
                        roomId = id,
                        path = Path(
                            points = pathWrapper.points.map { OffsetData(it.x, it.y) },
                            stroke = pathWrapper.strokeWidth,
                            color = pathWrapper.strokeColor.hashCode().toString()
                        )
                    )
                )
            }
        }.launchIn(coroutineScope)
    }

    override fun onDispose() {
        super.onDispose()

        coroutineScope.launch {
            gameClient.sendBaseModel(DisconnectRequest())
            gameClient.close()
        }
    }
}