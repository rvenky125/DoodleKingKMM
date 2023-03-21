package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.TYPE_NEW_WORDS
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_NEW_WORDS)
data class NewWords(
    @SerialName("new_words")
    val newWords: List<String>
): BaseModel()
