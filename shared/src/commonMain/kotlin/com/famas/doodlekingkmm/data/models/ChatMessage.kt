package com.famas.doodlekingkmm.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_CHAT_MESSAGE)
data class ChatMessage(
    val from: String,
    @SerialName("room_id")
    val roomId: String,
    val message: String,
    val timestamp: Long
) : BaseModel()

