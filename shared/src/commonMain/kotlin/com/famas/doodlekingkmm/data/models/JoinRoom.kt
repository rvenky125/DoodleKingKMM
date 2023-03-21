package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.TYPE_JOIN_ROOM
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_JOIN_ROOM)
data class JoinRoom(
    val username: String,
    @SerialName("room_id")
    val roomId: String,
): BaseModel()

