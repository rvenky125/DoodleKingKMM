package com.famas.doodlekingkmm.presentation.screen_home

import cafe.adriel.voyager.navigator.Navigator
import com.famas.doodlekingkmm.data.remote.responses.RoomResponse

sealed class HomeScreenEvent {
    data class JoinRoomEvent(val roomResponse: RoomResponse, val navigator: Navigator?): HomeScreenEvent()
    object CreateRoom: HomeScreenEvent()
    object SyncRooms: HomeScreenEvent()
    object Refresh : HomeScreenEvent()

    data class OnChangeRoomName(val value: String): HomeScreenEvent()
    data class OnChangeRoomCount(val count: Int) : HomeScreenEvent()
}
