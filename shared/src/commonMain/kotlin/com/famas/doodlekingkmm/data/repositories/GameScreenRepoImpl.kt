package com.famas.doodlekingkmm.data.repositories

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.remote.api.GameClient
import com.famas.doodlekingkmm.domain.repositories.GameScreenRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GameScreenRepoImpl(private val gameClient: GameClient) : GameScreenRepo {
    override suspend fun sendBaseModel(baseModel: BaseModel): Boolean {
        return try {
            gameClient.sendBaseModel(baseModel)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun close() {
        try {
            gameClient.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun observeBaseModels(): Flow<BaseModel> {
        return try {
            gameClient.observeBaseModels()
        } catch (e: Exception) {
            e.printStackTrace()
            return flow {  }
        }
    }

}