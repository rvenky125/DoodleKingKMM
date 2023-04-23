package com.famas.doodlekingkmm.data.remote.api

import com.famas.doodlekingkmm.data.models.BaseModel
import kotlinx.coroutines.flow.Flow

interface GameClient {
    suspend fun sendBaseModel(baseModel: BaseModel)
    suspend fun close()
    fun observeBaseModels(clientId: String): Flow<BaseModel>
}



//interface DrawingApi {
//
//    @Receive
//    fun observeEvents(): Flow<WebSocket.Event>
//
//    @Send
//    fun sendBaseModel(baseModel: BaseModel): Boolean
//
//    @Receive
//    fun observeBaseModels(): Flow<BaseModel>
//}