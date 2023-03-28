package com.famas.doodlekingkmm.presentation.screen_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.famas.doodlekingkmm.data.remote.requests.JoinRoomRequest
import com.famas.doodlekingkmm.data.remote.responses.RoomResponse
import com.famas.doodlekingkmm.domain.Response
import com.famas.doodlekingkmm.domain.repositories.HomeScreenRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenVM(private val homeScreenRepo: HomeScreenRepo) : ScreenModel {
    var state by mutableStateOf(HomeScreenState())
        private set

    private val _message = MutableStateFlow("")
    val message: Flow<String> = _message.asSharedFlow()

    private fun syncRooms() {
        homeScreenRepo.getAllRooms().onEach { response ->
            when(response) {
                is Response.Loading -> state = state.copy(loading = false)
                is Response.Success -> {
                    state = state.copy(rooms = response.data ?: emptyList())
                }
                is Response.Failure -> {
                    _message.emit(response.message)
                }
            }
        }.launchIn(coroutineScope)
    }

    fun onJoinRoom(room: RoomResponse) {
        homeScreenRepo.joinRoom(JoinRoomRequest(state.username, room.roomId)).onEach { response ->
            when(response) {
                is Response.Loading -> state = state.copy(loading = false)
                is Response.Success -> {
                    //TODO: Need to connect to game session
                    _message.emit("Need to connect to game session")
                }
                is Response.Failure -> {
                    _message.emit(response.message)
                }
            }
        }.launchIn(coroutineScope)
    }

    init {
        syncRooms()
    }
}