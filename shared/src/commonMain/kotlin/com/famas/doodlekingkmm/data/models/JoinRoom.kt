package com.famas.doodlekingkmm.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_JOIN_ROOM)
data class JoinRoom(
    val username: String,
    @SerialName("room_id")
    val roomId: String,
): BaseModel()

