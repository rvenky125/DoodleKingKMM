package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.TYPE_DRAW_DATA
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_DRAW_DATA)
data class DrawData(
    @SerialName("room_id")
    val roomId: String,
    val color: Int,
    val thickness: Float,
    @SerialName("from_x")
    val fromX: Float,
    @SerialName("from_y")
    val fromY: Float,
    @SerialName("to_x")
    val toX: Float,
    @SerialName("to_y")
    val toY: Float,
    @SerialName("motion_event")
    val motionEvent: Int
): BaseModel()
