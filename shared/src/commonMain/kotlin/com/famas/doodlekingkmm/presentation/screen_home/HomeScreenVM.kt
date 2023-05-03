package com.famas.doodlekingkmm.presentation.screen_home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.navigator.Navigator
import com.famas.doodlekingkmm.core.util.Constants
import com.famas.doodlekingkmm.core.util.settings
import com.famas.doodlekingkmm.data.remote.requests.CreateRoomRequest
import com.famas.doodlekingkmm.data.remote.requests.JoinRoomRequest
import com.famas.doodlekingkmm.domain.Response
import com.famas.doodlekingkmm.domain.repositories.HomeScreenRepo
import com.famas.doodlekingkmm.presentation.screen_game.GameScreen
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenVM(private val homeScreenRepo: HomeScreenRepo) : ScreenModel {

    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    fun onEvent(event: HomeScreenEvent) {
        coroutineScope.launch {
            when (event) {
                HomeScreenEvent.CreateRoom -> {
                    if (state.value.roomName.isNotBlank()) createRoom()
                }

                is HomeScreenEvent.JoinRoomEvent -> {
                    onJoinRoom(event.room.roomId, navigator = event.navigator)
                }

                is HomeScreenEvent.OnChangeRoomName -> {
                    _state.value = _state.value.copy(roomName = event.value)
                }

                HomeScreenEvent.SyncRooms -> {
                    syncRooms()
                }

                is HomeScreenEvent.OnChangeRoomCount -> {
                    _state.value = _state.value.copy(maxPlayers = event.count)
                }

                HomeScreenEvent.Refresh -> {
                    syncRooms()
                }

                is HomeScreenEvent.OnChangeUsernameTextInput -> {
                    _state.value = _state.value.copy(username = event.value)
                }

                HomeScreenEvent.OnConfirmUsernameChange -> {
                    settings.putString(Constants.USERNAME_PREF_KEY, state.value.username)
                    _state.value = _state.value.copy(
                        username = settings.getString(Constants.USERNAME_PREF_KEY, ""),
                        enableEditUsername = false
                    )
                }

                is HomeScreenEvent.SetEnableEditUsername -> {
                    _state.value = _state.value.copy(
                        enableEditUsername = event.enabled,
                        username = settings.getString(Constants.USERNAME_PREF_KEY, "")
                    )
                }
            }
        }
    }

    private fun syncRooms() {
        homeScreenRepo.getAllRooms().onEach { response ->
            when (response) {
                is Response.Loading -> _state.value =
                    _state.value.copy(loading = response.isLoading)

                is Response.Success -> {
                    _state.value = _state.value.copy(rooms = response.data ?: emptyList())
                }

                is Response.Failure -> {
                    _message.emit(response.message)
                }
            }
        }.launchIn(coroutineScope)
    }

    private fun onJoinRoom(roomId: String, navigator: Navigator?) {
        homeScreenRepo.joinRoom(JoinRoomRequest(state.value.username, roomId)).onEach { response ->
            when (response) {
                is Response.Loading -> _state.value =
                    _state.value.copy(loading = response.isLoading)

                is Response.Success -> {
                    navigator?.push(GameScreen(roomId = roomId))
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
                name = state.value.roomName,
                maxPlayers = state.value.maxPlayers
            )
        ).onEach { response ->
            Napier.d(tag = "myTag") {
                response.toString()
            }
            when (response) {
                is Response.Loading -> _state.value = _state.value
                is Response.Success -> {
                    syncRooms()
                    _message.emit(response.message ?: "Created room successfully")
                }

                is Response.Failure -> {
                    _message.emit(response.message)
                }
            }
        }.launchIn(coroutineScope)
        _state.value = _state.value.copy(roomName = "", maxPlayers = 2)
    }

    init {
        syncRooms()
        _state.value =
            _state.value.copy(username = settings.getString(Constants.USERNAME_PREF_KEY, ""))
    }
}