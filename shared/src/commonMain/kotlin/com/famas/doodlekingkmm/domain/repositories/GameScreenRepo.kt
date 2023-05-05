package com.famas.doodlekingkmm.domain.repositories

import com.famas.doodlekingkmm.data.models.BaseModel
import kotlinx.coroutines.flow.Flow

interface GameScreenRepo {
    suspend fun sendBaseModel(baseModel: BaseModel): Boolean
    suspend fun close()
    fun observeBaseModels(clientId: String): Flow<BaseModel>
}