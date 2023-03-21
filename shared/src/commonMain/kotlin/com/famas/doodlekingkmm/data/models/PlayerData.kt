package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.TYPE_PLAYER_DATA
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_PLAYER_DATA)
data class PlayerData(
    val username: String,
    @SerialName("is_drawing")
    val isDrawing: Boolean = false,
    val score: Int = 0,
    var rank: Int = 0
): BaseModel()
