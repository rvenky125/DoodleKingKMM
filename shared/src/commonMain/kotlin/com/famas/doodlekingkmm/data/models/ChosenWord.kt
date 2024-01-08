package com.famas.doodlekingkmm.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_CHOSEN_WORD)
data class ChosenWord(
    val chosenWord: String,
    @SerialName("room_id")
    val roomId: String
): BaseModel()
