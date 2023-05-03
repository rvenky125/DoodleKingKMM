package com.famas.doodlekingkmm.presentation.screen_game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.navigator.Navigator
import com.famas.doodlekingkmm.core.util.Constants
import com.famas.doodlekingkmm.core.util.randomUUID
import com.famas.doodlekingkmm.core.util.settings
import com.famas.doodlekingkmm.data.models.Announcement
import com.famas.doodlekingkmm.data.models.ChatMessage
import com.famas.doodlekingkmm.data.models.ChosenWord
import com.famas.doodlekingkmm.data.models.DisconnectRequest
import com.famas.doodlekingkmm.data.models.DrawAction
import com.famas.doodlekingkmm.data.models.DrawData
import com.famas.doodlekingkmm.data.models.GameError
import com.famas.doodlekingkmm.data.models.GameState
import com.famas.doodlekingkmm.data.models.JoinRoom
import com.famas.doodlekingkmm.data.models.NewWords
import com.famas.doodlekingkmm.data.models.Phase
import com.famas.doodlekingkmm.data.models.PhaseChange
import com.famas.doodlekingkmm.data.models.Ping
import com.famas.doodlekingkmm.data.models.PlayerData
import com.famas.doodlekingkmm.data.models.PlayerList
import com.famas.doodlekingkmm.data.models.RoundDrawInfo
import com.famas.doodlekingkmm.data.remote.api.GameClient
import com.famas.doodlekingkmm.presentation.components.canvas.CanvasController
import com.famas.doodlekingkmm.presentation.components.canvas.PathEvent
import io.github.aakira.napier.Napier
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameScreenVM(
    private val gameClient: GameClient
) : ScreenModel {
    val canvasController = CanvasController()

    var roomId: String? = null
    private val uuid = randomUUID()

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
                val messageToSend = gameScreenState.value.textInputValue

                if (messageToSend.isEmpty()) {
                    return
                }

                coroutineScope.launch {
                    roomId?.let {
                        gameScreenState.value.username?.let { user ->
                            gameClient.sendBaseModel(
                                ChatMessage(
                                    from = user,
                                    roomId = it,
                                    message = messageToSend,
                                    timestamp = GMTDate().timestamp
                                )
                            )
                        }
                    }
                }
                _gameScreenState.value = gameScreenState.value.copy(
                    textInputValue = ""
                )
            }
        }
    }

    fun connectToRoom(roomId: String, navigator: Navigator?) {
        this.roomId = roomId
        _gameScreenState.value.username?.let { user ->
            coroutineScope.launch {
                gameClient.sendBaseModel(JoinRoom(user, roomId))
            }
        } ?: run {
            // TODO: Need navigate the user to previous screen
            navigator?.pop()
        }
    }

    private fun startConnection() {
        gameClient.observeBaseModels(uuid).onEach {
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
                            currentWord = it.chosenWord
                        )
                    }

                    is DrawData -> {
                        canvasController.updateDrawDataManually(
                            PathEvent(
                                offset = Offset(it.x, it.y), type = it.pathEvent
                            )
                        )
                    }

                    is GameError -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            messages = listOf(it) + gameScreenState.value.messages
                        )
                    }

                    is GameState -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            statusText = "${it.drawingPlayer} Drawing ${it.word}",
                            currentWord = it.word
                        )
                    }

                    is NewWords -> {
                        _gameScreenState.value = gameScreenState.value.copy(
                            newWords = it.newWords, showChooseWordsView = true
                        )
                    }

                    is PhaseChange -> {
                        if (it.phase == Phase.NEW_ROUND) {
                            canvasController.reset()
                        }
                        val statusText =
                            getStatusTextFromPhase(phaseChange = it).ifEmpty { gameScreenState.value.statusText }
                        if (it.phase != null) {
                            _gameScreenState.value = gameScreenState.value.copy(
                                currentPhase = it.phase,
                                totalTime = it.time,
                                time = it.time,
                                drawingPlayer = it.drawingPlayer,
                                statusText = statusText
                            )
                        } else {
                            _gameScreenState.value = gameScreenState.value.copy(
                                time = it.time,
                                drawingPlayer = it.drawingPlayer,
                                statusText = statusText
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
                            playerData = it
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

                _gameScreenState.value = gameScreenState.value.copy(
                    showChooseWordsView = gameScreenState.value.newWords.isNotEmpty() && gameScreenState.value.drawingPlayer == gameScreenState.value.username && gameScreenState.value.currentPhase == Phase.NEW_ROUND
                )
            }.launchIn(coroutineScope)
    }

    private fun getStatusTextFromPhase(phaseChange: PhaseChange): String {
        return when (phaseChange.phase) {
            Phase.WAITING_FOR_PLAYERS -> "Waiting for players"
            Phase.WAITING_FOR_START -> "Waiting for start"
            Phase.NEW_ROUND -> "Starting new round"
            Phase.GAME_RUNNING -> "${phaseChange.drawingPlayer} drawing \"${gameScreenState.value.currentWord}\""
            Phase.SHOW_WORD -> gameScreenState.value.currentWord
            else -> ""
        }
    }

    init {
        startConnection()
        _gameScreenState.value = gameScreenState.value.copy(
            username = settings.getStringOrNull(Constants.USERNAME_PREF_KEY)
        )

        canvasController.pathEventsFlow.onEach {
            if (gameScreenState.value.drawingPlayer == gameScreenState.value.username) {
                roomId?.let { id ->
                    val drawData =
                        DrawData(roomId = id, x = it.offset.x, y = it.offset.y, pathEvent = it.type)
                    gameClient.sendBaseModel(drawData)
                }
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