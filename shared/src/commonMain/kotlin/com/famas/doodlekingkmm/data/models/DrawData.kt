package com.famas.doodlekingkmm.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_DRAW_DATA)
data class DrawData(
    @SerialName("room_id")
    val roomId: String,
    val color: Int? = null,
    val thickness: Float? = null,
    val x: Float?,
    val y: Float?,
    @SerialName("path_event")
    val pathEvent: Int
) : BaseModel() {
    companion object {
        const val INSERT = 0
        const val UPDATE = 1
        const val UNDO = 2
    }
}