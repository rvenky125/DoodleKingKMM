package com.famas.doodlekingkmm.presentation.screen_home

import com.famas.doodlekingkmm.data.remote.responses.RoomResponse

data class HomeScreenState(
    val loading: Boolean = false,
    val rooms: List<RoomResponse> = emptyList(),
    val username: String = "",
    val roomName: String = "",
    val maxPlayers: Int = 2,
    val enableEditUsername: Boolean = false
)
