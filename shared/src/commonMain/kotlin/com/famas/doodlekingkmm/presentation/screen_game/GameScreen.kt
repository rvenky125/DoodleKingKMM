package com.famas.doodlekingkmm.presentation.screen_game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.famas.doodlekingkmm.data.models.Announcement
import com.famas.doodlekingkmm.data.models.ChatMessage
import com.famas.doodlekingkmm.data.models.Phase
import com.famas.doodlekingkmm.data.remote.api.KtorGameClient
import com.famas.doodlekingkmm.di.httpClient
import com.famas.doodlekingkmm.presentation.components.canvas.CanvasBox
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay

class GameScreen(
    private val roomId: String
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel =
            rememberScreenModel { GameScreenVM(KtorGameClient(httpClient = httpClient)) }
        val state = viewModel.gameScreenState.value
        val navigator = LocalNavigator.current

        val animatedProgress = remember {
            Animatable(1f)
        }


        LaunchedEffect(Unit) {
            delay(1000)
            viewModel.connectToRoom(roomId, navigator)
        }

        LaunchedEffect(state.totalTime, state.time) {
            if (state.totalTime != 0L || state.time != 0L) {
                animatedProgress.animateTo(state.time.toFloat() / state.totalTime.toFloat())
            }
        }

        Column {
            CanvasBox(
                canvasController = viewModel.canvasController,
                modifier = Modifier.fillMaxWidth().height(400.dp)
            )

            if (state.newWords.isNotEmpty()) {
                Row {
                    state.newWords.forEach {
                        Button(onClick = {
                            viewModel.onEvent(GameScreenEvent.OnSelectWord(it))
                        }) {
                            Text(it)
                        }
                    }
                }
            }
            LinearProgressIndicator(progress = animatedProgress.value, modifier = Modifier.fillMaxWidth())
            state.statusText?.let { Text(it) }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.messages) {
                    when (it) {
                        is Announcement -> {
                            Text(it.message)
                        }

                        is ChatMessage -> {
                            Text(it.message)
                        }

                        else -> Text(it.toString())
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                if (state.drawingPlayer != state.username) {
                    OutlinedTextField(state.textInputValue, onValueChange = {
                        viewModel.onEvent(GameScreenEvent.OnChangeTextInputValue(it))
                    }, modifier = Modifier.weight(1f))
                }

                IconButton(onClick = {
                    viewModel.onEvent(GameScreenEvent.OnSendMessage)
                }) {
                    Icon(imageVector = Icons.Default.Send, null)
                }
            }
        }
    }
}