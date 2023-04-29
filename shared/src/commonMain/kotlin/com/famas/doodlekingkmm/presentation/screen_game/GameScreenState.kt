package com.famas.doodlekingkmm.presentation.screen_game

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.ChatMessage
import com.famas.doodlekingkmm.data.models.NewWords
import com.famas.doodlekingkmm.data.models.Phase
import com.famas.doodlekingkmm.data.models.PlayerData
import com.famas.doodlekingkmm.data.models.PlayerList

data class GameScreenState(
    val loading: Boolean = false,
    val messages: List<BaseModel> = listOf(
        ChatMessage("Venky", "", "Hello world", 0L)
    ),
    val playersList: List<PlayerData> = emptyList(),
    val newWords: List<String> = emptyList(),
    val textInputValue: String = "",
    val currentPhase: Phase? = null,
    val time: Long = 0L,
    val totalTime: Long = 0L,
    val drawingPlayer: String? = null,
    val username: String? = null,
    val showChooseWordsView: Boolean = false,
    val statusText: String? = null
)