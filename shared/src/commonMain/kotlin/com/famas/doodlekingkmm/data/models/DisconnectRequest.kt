package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.TYPE_DISCONNECT_REQUEST
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName(TYPE_DISCONNECT_REQUEST)
class DisconnectRequest: BaseModel()