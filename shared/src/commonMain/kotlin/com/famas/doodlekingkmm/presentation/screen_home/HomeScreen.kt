package com.famas.doodlekingkmm.presentation.screen_home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.famas.doodlekingkmm.core.util.getScreenModel
import com.famas.doodlekingkmm.presentation.components.NumberPicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val homeScreenModel = getScreenModel<HomeScreenVM>()
        val navigator = LocalNavigator.current

        val state = homeScreenModel.state.value
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
            rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
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
                if (scaffoldState.bottomSheetState.isCollapsed)
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
        ) { paddingValues ->

            Column(modifier = Modifier.padding(paddingValues)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.enableEditUsername) {
                        OutlinedTextField(state.username, onValueChange = {
                            homeScreenModel.onEvent(HomeScreenEvent.OnChangeUsernameTextInput(it))
                        }, modifier = Modifier.weight(1f))
                    } else {
                        Text(buildAnnotatedString {
                            append("Username: ")
                            append(state.username)
                        }, modifier = Modifier.weight(1f))
                    }

                    if (state.enableEditUsername) {
                        IconButton(onClick = {
                            homeScreenModel.onEvent(HomeScreenEvent.SetEnableEditUsername(false))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }

                    IconButton(onClick = {
                        if (state.enableEditUsername) {
                            homeScreenModel.onEvent(HomeScreenEvent.OnConfirmUsernameChange)
                        } else {
                            homeScreenModel.onEvent(HomeScreenEvent.SetEnableEditUsername(true))
                        }
                    }) {
                        Icon(
                            imageVector = if (state.enableEditUsername) Icons.Default.Done else Icons.Default.Edit,
                            contentDescription = null
                        )
                    }
                }

                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Button(onClick = {
                            homeScreenModel.onEvent(HomeScreenEvent.Refresh)
                        }) {
                            Text("Refresh")
                        }
                    }

                    items(state.rooms) { room ->
                        Card(onClick = {
                            homeScreenModel.onEvent(HomeScreenEvent.JoinRoomEvent(room, navigator))
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

                    item { Spacer(modifier = Modifier.height(200.dp)) }
                }
            }
        }
    }
}