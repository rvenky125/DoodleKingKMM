package com.famas.doodlekingkmm.data.repositories

import com.famas.doodlekingkmm.core.util.Constants
import com.famas.doodlekingkmm.data.remote.api.HomeScreenApi
import com.famas.doodlekingkmm.data.remote.requests.CreateRoomRequest
import com.famas.doodlekingkmm.data.remote.requests.JoinRoomRequest
import com.famas.doodlekingkmm.data.remote.responses.RoomResponse
import com.famas.doodlekingkmm.domain.Response
import com.famas.doodlekingkmm.domain.repositories.HomeScreenRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeScreenRepoImpl(private val homeScreenApi: HomeScreenApi) : HomeScreenRepo {
    override fun createRoom(request: CreateRoomRequest): Flow<Response<Unit>> {
        return flow {
            emit(Response.Loading(true))
            try {
                val response = homeScreenApi.createRoom(request)

                if (response.successful) {
                    emit(Response.Success(response.data))
                } else {
                    emit(Response.Failure(response.message ?: Constants.DEF_MESSAGE))
                }
            } catch (e: Exception) {
                emit(Response.Failure(e.message ?: Constants.DEF_MESSAGE))
            }
            emit(Response.Loading(false))
        }
    }

    override fun getAllRooms(): Flow<Response<List<RoomResponse>>> {
        return flow {
            emit(Response.Loading(true))
            try {
                val response = homeScreenApi.getAllRooms()

                if (response.successful) {
                    emit(Response.Success(response.data))
                } else {
                    emit(Response.Failure(response.message ?: Constants.DEF_MESSAGE))
                }
            } catch (e: Exception) {
                emit(Response.Failure(e.message ?: Constants.DEF_MESSAGE))
            }
            emit(Response.Loading(false))
        }
    }

    override fun joinRoom(request: JoinRoomRequest): Flow<Response<Unit>> {
        return flow {
            emit(Response.Loading(true))
            try {
                val response = homeScreenApi.joinRoom(request)

                if (response.successful) {
                    emit(Response.Success(response.data))
                } else {
                    emit(Response.Failure(response.message ?: Constants.DEF_MESSAGE))
                }
            } catch (e: Exception) {
                emit(Response.Failure(e.message ?: Constants.DEF_MESSAGE))
            }
            emit(Response.Loading(false))
        }
    }

}