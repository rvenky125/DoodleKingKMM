package com.famas.doodlekingkmm.presentation.screen_game

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.Phase
import com.famas.doodlekingkmm.data.models.PlayerData

data class GameScreenState(
    val loading: Boolean = false,
    val messages: List<BaseModel> = listOf(),
    val playersList: List<PlayerData> = emptyList(),
    val newWords: List<String> = emptyList(),
    val textInputValue: String = "",
    val currentPhase: Phase? = null,
    val time: Long = 0L,
    val totalTime: Long = 0L,
    val drawingPlayer: String? = null,
    val username: String? = null,
    val showChooseWordsView: Boolean = false,
    val statusText: String? = null,
    val playerData: PlayerData? = null,
    val currentWord: String = "",
    val canvasWidth: Int = 1,
    val canvasHeight: Int = 1,
)