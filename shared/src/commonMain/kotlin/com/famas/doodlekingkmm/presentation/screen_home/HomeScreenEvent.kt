package com.famas.doodlekingkmm.presentation.screen_home

import com.famas.doodlekingkmm.data.remote.responses.RoomResponse

sealed class HomeScreenEvent {
    data class JoinRoom(val roomResponse: RoomResponse): HomeScreenEvent()
    object CreateRoom: HomeScreenEvent()
    object SyncRooms: HomeScreenEvent()
    data class OnChangeRoomName(val value: String): HomeScreenEvent()
    data class OnChangeRoomCount(val count: Int) : HomeScreenEvent()
}
