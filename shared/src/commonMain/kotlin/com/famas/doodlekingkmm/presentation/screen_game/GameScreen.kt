package com.famas.doodlekingkmm.presentation.screen_game

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.famas.doodlekingkmm.core.util.asFormattedDate
import com.famas.doodlekingkmm.core.util.getScreenModel
import com.famas.doodlekingkmm.data.models.Announcement
import com.famas.doodlekingkmm.data.models.ChatMessage
import com.famas.doodlekingkmm.presentation.components.canvas.CanvasBox
import com.famas.doodlekingkmm.presentation.screen_game.components.ChatItem
import com.famas.doodlekingkmm.presentation.screen_game.components.PlayerScores
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameScreen(
    private val roomId: String
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<GameScreenVM>()
        val state = viewModel.gameScreenState.value
        val navigator = LocalNavigator.current
        val focusManager = LocalFocusManager.current

        val animatedProgress = remember {
            Animatable(1f)
        }

        val drawerState = rememberDrawerState(DrawerValue.Closed)

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            delay(1000)
            viewModel.connectToRoom(roomId, navigator)
        }

        LaunchedEffect(state.totalTime, state.time) {
            if (state.totalTime != 0L || state.time != 0L) {
                animatedProgress.animateTo(state.time.toFloat() / state.totalTime.toFloat())
            }
        }

        ModalNavigationDrawer(drawerContent = {
            PlayerScores(state.playersList)
        }, drawerState = drawerState, modifier = Modifier.fillMaxSize()) {
            if (state.showChooseWordsView) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    state.newWords.forEach {
                        Button(onClick = {
                            viewModel.onEvent(GameScreenEvent.OnSelectWord(it))
                        }, modifier = Modifier.padding(bottom = 10.dp)) {
                            Text(it)
                        }
                    }
                    Text(
                        (state.time / 1000L).toString(),
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            } else {
                Column {
                    CanvasBox(
                        canvasController = viewModel.canvasController,
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.55f).onPlaced {
                            viewModel.onEvent(
                                GameScreenEvent.OnLayout(
                                    it.size.width,
                                    it.size.height
                                )
                            )
                        },
                        onGoBack = {
                            navigator?.pop()
                        },
                        drawingEnabled = state.drawingPlayer == state.username
                    )

                    Text(
                        state.statusText ?: "",
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    LinearProgressIndicator(
                        progress = animatedProgress.value,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    Column(modifier = Modifier.pointerInput(Unit) { detectTapGestures { } }) {
                        Row(modifier = Modifier.weight(1f)) {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(Icons.Default.Person, null)
                            }
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.End,
                                reverseLayout = true
                            ) {
                                items(state.messages) {
                                    when (it) {
                                        is Announcement -> {
                                            Surface(
                                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                                modifier = Modifier.padding(vertical = 10.dp)
                                                    .padding(horizontal = 16.dp),
                                                shape = RoundedCornerShape(25)
                                            ) {
                                                Column(modifier = Modifier.padding(10.dp)) {
                                                    Text(
                                                        it.message,
                                                        modifier = Modifier.padding(vertical = 5.dp)
                                                    )
                                                    Text(
                                                        it.timestamp.asFormattedDate(),
                                                        modifier = Modifier.padding(vertical = 5.dp),
                                                        style = MaterialTheme.typography.labelSmall
                                                    )
                                                }
                                            }
                                        }

                                        is ChatMessage -> {
                                            ChatItem(
                                                message = it.message,
                                                name = it.from,
                                                timestamp = it.timestamp,
                                                modifier = Modifier.padding(
                                                    horizontal = 16.dp,
                                                    vertical = 4.dp
                                                ).padding(bottom = 16.dp)
                                            )
                                        }

                                        else -> Text(
                                            it.toString(),
                                            modifier = Modifier.padding(vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (state.drawingPlayer != state.username) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    state.textInputValue,
                                    onValueChange = {
                                        viewModel.onEvent(GameScreenEvent.OnChangeTextInputValue(it))
                                    },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(
                                        autoCorrect = false,
                                        imeAction = ImeAction.Done
                                    ),
                                    maxLines = 1
                                )

                                IconButton(onClick = {
                                    viewModel.onEvent(GameScreenEvent.OnSendMessage)
                                    focusManager.clearFocus()

                                }) {
                                    Icon(imageVector = Icons.Default.Send, null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}