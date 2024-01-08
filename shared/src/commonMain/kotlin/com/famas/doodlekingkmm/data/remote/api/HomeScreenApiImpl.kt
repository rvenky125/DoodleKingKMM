package com.famas.doodlekingkmm.data.remote.api

import com.famas.doodlekingkmm.data.remote.api.HomeScreenApi.Companion.CREATE_ROOM_ROUTE
import com.famas.doodlekingkmm.data.remote.api.HomeScreenApi.Companion.GET_ROOMS_ROUTE
import com.famas.doodlekingkmm.data.remote.api.HomeScreenApi.Companion.JOIN_ROOM_ROUTE
import com.famas.doodlekingkmm.data.remote.requests.CreateRoomRequest
import com.famas.doodlekingkmm.data.remote.requests.JoinRoomRequest
import com.famas.doodlekingkmm.data.remote.responses.BasicApiResponse
import com.famas.doodlekingkmm.data.remote.responses.RoomResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class HomeScreenApiImpl(private val httpClient: HttpClient): HomeScreenApi {
    override suspend fun createRoom(request: CreateRoomRequest): BasicApiResponse<Unit> {
        return httpClient.post(CREATE_ROOM_ROUTE) {
            setBody(request)
        }.body()
    }

    override suspend fun getAllRooms(): BasicApiResponse<List<RoomResponse>> {
        return httpClient.get("${GET_ROOMS_ROUTE}?query=")
            .body()
    }

    override suspend fun joinRoom(joinRoomRequest: JoinRoomRequest): BasicApiResponse<Unit> {
        return httpClient.post(JOIN_ROOM_ROUTE) {
            setBody(joinRoomRequest)
        }.body()
    }
}