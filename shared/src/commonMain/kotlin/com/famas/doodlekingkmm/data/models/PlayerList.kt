package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.PlayerData
import com.famas.doodlekingkmm.data.models.TYPE_PLAYER_LIST
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(TYPE_PLAYER_LIST)
data class PlayerList(
    val players: List<PlayerData>
): BaseModel()
