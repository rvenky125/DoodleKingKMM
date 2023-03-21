package com.famas.doodlekingkmm.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_PHASE_CHANGE)
data class PhaseChange(
    var phase: Phase?,
    var time: Long,
    @SerialName("drawing_player")
    val drawingPlayer: String? = null
): BaseModel()

enum class Phase {
    WAITING_FOR_PLAYERS,
    WAITING_FOR_START,
    NEW_ROUND,
    GAME_RUNNING,
    SHOW_WORD
}

