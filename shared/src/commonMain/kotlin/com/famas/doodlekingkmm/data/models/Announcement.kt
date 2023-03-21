package com.famas.doodlekingkmm.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_ANNOUNCEMENT)
data class Announcement(
    val message: String,
    val timestamp: Long,
    @SerialName("announcement_type")
    val announcementType: Int
): BaseModel() {
    companion object {
        const val TYPE_PLAYER_GUESSED_WORD = 0
        const val TYPE_PLAYER_JOINED = 1
        const val TYPE_PLAYER_LEFT = 2
        const val TYPE_EVERYBODY_GUESSED_IT = 3
    }
}