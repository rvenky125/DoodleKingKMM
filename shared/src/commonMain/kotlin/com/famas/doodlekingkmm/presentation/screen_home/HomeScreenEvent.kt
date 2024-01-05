package com.famas.doodlekingkmm.presentation.screen_home

import cafe.adriel.voyager.navigator.Navigator
import com.famas.doodlekingkmm.data.remote.responses.RoomResponse

sealed class HomeScreenEvent {
    data class JoinRoomEvent(val room: RoomResponse, val navigator: Navigator?): HomeScreenEvent()
    data object CreateRoom: HomeScreenEvent()
    object SyncRooms: HomeScreenEvent()
    object Refresh : HomeScreenEvent()
    data class OnChangeRoomName(val value: String): HomeScreenEvent()
    data class OnChangeRoomCount(val count: Int) : HomeScreenEvent()
    data class OnChangeUsernameTextInput(val value: String): HomeScreenEvent()
    object OnConfirmUsernameChange: HomeScreenEvent()
    data class SetEnableEditUsername(val enabled: Boolean): HomeScreenEvent()
}
