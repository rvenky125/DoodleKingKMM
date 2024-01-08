package com.famas.doodlekingkmm.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_GAME_STATE)
data class GameState(
    @SerialName("drawing_player")
    val drawingPlayer: String,
    val word: String
) : BaseModel()