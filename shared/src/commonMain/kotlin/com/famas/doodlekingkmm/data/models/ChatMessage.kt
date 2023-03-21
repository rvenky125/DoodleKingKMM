package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.TYPE_CHAT_MESSAGE
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

