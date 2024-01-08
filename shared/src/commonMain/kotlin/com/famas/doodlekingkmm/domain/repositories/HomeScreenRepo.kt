package com.famas.doodlekingkmm.domain.repositories

import com.famas.doodlekingkmm.data.remote.requests.CreateRoomRequest
import com.famas.doodlekingkmm.data.remote.requests.JoinRoomRequest
import com.famas.doodlekingkmm.data.remote.responses.RoomResponse
import com.famas.doodlekingkmm.domain.Response
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepo {
    fun createRoom(request: CreateRoomRequest): Flow<Response<Unit>>

    fun getAllRooms(): Flow<Response<List<RoomResponse>>>

    fun joinRoom(request: JoinRoomRequest): Flow<Response<Unit>>
}