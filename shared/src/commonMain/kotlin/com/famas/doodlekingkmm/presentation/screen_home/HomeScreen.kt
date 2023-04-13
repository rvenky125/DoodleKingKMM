package com.famas.doodlekingkmm.presentation.screen_home

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.famas.doodlekingkmm.data.remote.api.HomeScreenApiImpl
import com.famas.doodlekingkmm.data.repositories.HomeScreenRepoImpl
import com.famas.doodlekingkmm.di.httpClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.FabPosition
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import com.famas.doodlekingkmm.presentation.components.NumberPicker
import io.github.aakira.napier.Napier

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val homeScreenModel = rememberScreenModel {
            HomeScreenVM(
                HomeScreenRepoImpl(
                    HomeScreenApiImpl(
                        httpClient
                    )
                )
            )
        }
        val navigator = LocalNavigator.current

        val state = homeScreenModel.state
        val snackbarHostState = remember {
            SnackbarHostState()
        }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            homeScreenModel.message.collectLatest {

                if (it.isNotBlank()) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(it)
                    }
                }
            }
        }

        val bottomSheetState =
            remember { BottomSheetState(initialValue = BottomSheetValue.Collapsed) }
        val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(title = {
                    Text("Home Screen")
                })
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            floatingActionButton = {
                if (!scaffoldState.bottomSheetState.isAnimationRunning && scaffoldState.bottomSheetState.isCollapsed)
                    FloatingActionButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }, modifier = Modifier.padding(bottom = 100.dp)) {
                        Text("Create Room", modifier = Modifier.padding(horizontal = 15.dp))
                    }

            },
            sheetContent = {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    OutlinedTextField(state.roomName, onValueChange = {
                        homeScreenModel.onEvent(HomeScreenEvent.OnChangeRoomName(it))
                    }, placeholder = {
                        Text("Room Name")
                    }, modifier = Modifier.fillMaxWidth())
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Room Count")
                        NumberPicker(
                            value = state.maxPlayers,
                            onValueChange = {
                                homeScreenModel.onEvent(
                                    HomeScreenEvent.OnChangeRoomCount(it)
                                )
                            },
                            minValue = 2,
                            maxValue = 8
                        )
                    }
                    Button(onClick = {
                        homeScreenModel.onEvent(HomeScreenEvent.CreateRoom)
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }, modifier = Modifier.align(Alignment.End).padding(top = 16.dp)) {
                        Text("Create")
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            sheetPeekHeight = 0.dp,
            sheetElevation = 16.dp,
            sheetShape = RoundedCornerShape(16f)
        ) {
            LazyColumn(modifier = Modifier.padding(it).padding(horizontal = 16.dp)) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                items(state.rooms) { room ->
                    Card(onClick = {
                        homeScreenModel.onEvent(HomeScreenEvent.JoinRoom(room))
                    }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Column(
                            modifier = Modifier.padding(
                                vertical = 10.dp,
                                horizontal = 15.dp
                            )
                        ) {
                            Text(room.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Max Players: " + room.maxPlayers,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Joined players: " + room.playerCount,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}