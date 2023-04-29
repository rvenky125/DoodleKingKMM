package com.famas.doodlekingkmm.presentation.screen_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.navigator.Navigator
import com.famas.doodlekingkmm.core.util.Constants
import com.famas.doodlekingkmm.core.util.settings
import com.famas.doodlekingkmm.data.remote.requests.CreateRoomRequest
import com.famas.doodlekingkmm.data.remote.requests.JoinRoomRequest
import com.famas.doodlekingkmm.data.remote.responses.RoomResponse
import com.famas.doodlekingkmm.domain.Response
import com.famas.doodlekingkmm.domain.repositories.HomeScreenRepo
import com.famas.doodlekingkmm.presentation.screen_game.GameScreen
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenVM(private val homeScreenRepo: HomeScreenRepo) : ScreenModel {
    var state by mutableStateOf(HomeScreenState())
        private set

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    var username = settings.getString(Constants.USERNAME_PREF_KEY, "")

    fun onEvent(event: HomeScreenEvent) {
        coroutineScope.launch {
            when (event) {
                HomeScreenEvent.CreateRoom -> {
                    createRoom()
                }
                is HomeScreenEvent.JoinRoomEvent -> {
                    onJoinRoom(event.roomResponse, navigator = event.navigator)
                }
                is HomeScreenEvent.OnChangeRoomName -> {
                    state = state.copy(roomName = event.value)
                }
                HomeScreenEvent.SyncRooms -> {
                    syncRooms()
                }
                is HomeScreenEvent.OnChangeRoomCount -> {
                    state = state.copy(maxPlayers = event.count)
                }
                HomeScreenEvent.Refresh -> {
                    syncRooms()
                }
                is HomeScreenEvent.OnChangeUsernameTextInput -> {
                    state = state.copy(username = event.value)
                }
                HomeScreenEvent.OnConfirmUsernameChange -> {
                    settings.putString(Constants.USERNAME_PREF_KEY, state.username)
                    state = state.copy(
                        username = settings.getString(Constants.USERNAME_PREF_KEY, ""),
                        enableEditUsername = false
                    )
                }

                is HomeScreenEvent.SetEnableEditUsername -> {
                    state = state.copy(
                        enableEditUsername = event.enabled
                    )
                }
            }
        }
    }

    private fun syncRooms() {
        homeScreenRepo.getAllRooms().onEach { response ->
            when (response) {
                is Response.Loading -> state = state.copy(loading = response.isLoading)
                is Response.Success -> {
                    state = state.copy(rooms = response.data ?: emptyList())
                }
                is Response.Failure -> {
                    _message.emit(response.message)
                }
            }
        }.launchIn(coroutineScope)
    }

    private fun onJoinRoom(room: RoomResponse, navigator: Navigator?) {
        homeScreenRepo.joinRoom(JoinRoomRequest(state.username, room.roomId)).onEach { response ->
            when (response) {
                is Response.Loading -> state = state.copy(loading = response.isLoading)
                is Response.Success -> {
                    navigator?.push(GameScreen(roomId = room.roomId))
                }
                is Response.Failure -> {
                    _message.emit(response.message)
                }
            }
        }.launchIn(coroutineScope)
    }

    private fun createRoom() {
        Napier.d(tag = "myTag") {
            "Create room"
        }
        homeScreenRepo.createRoom(
            CreateRoomRequest(
                name = state.roomName,
                maxPlayers = state.maxPlayers
            )
        ).onEach { response ->
            Napier.d(tag = "myTag") {
                response.toString()
            }
            when (response) {
                is Response.Loading -> state = state.copy(loading = response.isLoading)
                is Response.Success -> {
                    syncRooms()
                    _message.emit(response.message ?: "Created room successfully")
                }
                is Response.Failure -> {
                    _message.emit(response.message)
                }
            }
        }.launchIn(coroutineScope)
        state = state.copy(roomName = "", maxPlayers = 2)
    }

    init {
        syncRooms()
        state = state.copy(username = settings.getString(Constants.USERNAME_PREF_KEY, ""))
    }
}