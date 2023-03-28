package com.famas.doodlekingkmm.data.remote.api

import com.famas.doodlekingkmm.data.remote.requests.CreateRoomRequest
import com.famas.doodlekingkmm.data.remote.requests.JoinRoomRequest
import com.famas.doodlekingkmm.data.remote.responses.BasicApiResponse
import com.famas.doodlekingkmm.data.remote.responses.RoomResponse
import com.famas.doodlekingkmm.domain.Response
import kotlinx.coroutines.flow.Flow

interface HomeScreenApi {
    suspend fun createRoom(request: CreateRoomRequest): BasicApiResponse<Unit>

    suspend fun getAllRooms(): BasicApiResponse<List<RoomResponse>>

    suspend fun joinRoom(joinRoomRequest: JoinRoomRequest): BasicApiResponse<Unit>

    companion object {
        const val CREATE_ROOM_ROUTE = "api/createRoom"
        const val JOIN_ROOM_ROUTE = "api/joinRoom"
        const val GET_ROOMS_ROUTE = "api/getRooms"
    }
}