package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.TYPE_GAME_STATE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_GAME_STATE)
data class GameState(
    @SerialName("drawing_player")
    val drawingPlayer: String,
    val word: String
) : BaseModel()