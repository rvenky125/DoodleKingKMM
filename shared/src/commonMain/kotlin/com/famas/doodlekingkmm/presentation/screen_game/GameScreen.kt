package com.famas.doodlekingkmm.presentation.screen_game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
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
import com.famas.doodlekingkmm.data.models.PhaseChange
import com.famas.doodlekingkmm.data.models.Ping
import com.famas.doodlekingkmm.data.models.PlayerData
import com.famas.doodlekingkmm.data.models.PlayerList
import com.famas.doodlekingkmm.data.models.RoundDrawInfo
import com.famas.doodlekingkmm.data.remote.api.KtorGameClient
import com.famas.doodlekingkmm.di.httpClient
import com.famas.doodlekingkmm.presentation.components.canvas.CanvasBox

class GameScreen(
    private val roomId: String
) : Screen {

    @Composable
    override fun Content() {
        val viewModel =
            rememberScreenModel { GameScreenVM(KtorGameClient(httpClient = httpClient)) }
        val state = viewModel.gameScreenState.value

        LaunchedEffect(Unit) {
            viewModel.connectToRoom(roomId)
        }

        Column {
            CanvasBox(
                canvasController = viewModel.canvasController,
                modifier = Modifier.fillMaxWidth().height(400.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.messages) {
                    when (it) {
                        is Announcement -> {
                            Text(it.message)
                        }
                        is ChatMessage -> {
                            Text(it.message)
                        }

                        is NewWords -> {
                            it.newWords.forEach {
                                Button(onClick = {
                                    viewModel.onEvent(GameScreenEvent.OnSelectWord(it))
                                }) {
                                    Text(it)
                                }
                            }
                        }

                        else -> Text(it.toString())
                    }
                }
            }
        }
    }
}