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
    val color: Int? = null,
    val thickness: Float? = null,
    val x: Float?,
    val y: Float?,
    @SerialName("device_width")
    val deviceWidth: Int?,
    @SerialName("device_height")
    val deviceHeight: Int?,
    @SerialName("path_event")
    val pathEvent: Int
) : BaseModel() {
    companion object {
        const val INSERT = 0
        const val UPDATE = 1
        const val UNDO = 2
    }
}