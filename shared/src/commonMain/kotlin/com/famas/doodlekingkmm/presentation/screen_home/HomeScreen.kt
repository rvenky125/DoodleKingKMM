package com.famas.doodlekingkmm.presentation.screen_home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.famas.doodlekingkmm.data.remote.api.HomeScreenApiImpl
import com.famas.doodlekingkmm.data.repositories.HomeScreenRepoImpl
import com.famas.doodlekingkmm.di.httpClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
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


        Scaffold(topBar = {
            TopAppBar(title = {
                Text("Home Screen")
            })
        }, snackbarHost = {
            SnackbarHost(snackbarHostState)
        }) {
            LazyColumn(modifier = Modifier.padding(it).padding(horizontal = 16.dp)) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                items(state.rooms) { room ->
                    Card(onClick = {
                        homeScreenModel.onJoinRoom(room)
                    }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)) {
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