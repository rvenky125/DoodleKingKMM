package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.TYPE_CUR_ROUND_DRAW_INFO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_CUR_ROUND_DRAW_INFO)
data class RoundDrawInfo(
    val data: List<String>
): BaseModel()
