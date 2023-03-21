package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.TYPE_GAME_ERROR
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_GAME_ERROR)
data class GameError(
    @SerialName("error_type")
    val errorType: Int
): BaseModel() {
    companion object {
        const val ERROR_ROOM_NOT_FOUND = 0
    }
}
