package com.famas.doodlekingkmm.data.models

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.DRAW_ACTION
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(DRAW_ACTION)
data class DrawAction(
    val action: String
) : BaseModel() {
    companion object {
        const val ACTION_UNDO = "ACTION_UNDO"
    }
}
