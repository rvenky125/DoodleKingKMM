package com.famas.doodlekingkmm.presentation.screen_game

import com.famas.doodlekingkmm.data.models.BaseModel
import com.famas.doodlekingkmm.data.models.ChatMessage
import com.famas.doodlekingkmm.data.models.NewWords
import com.famas.doodlekingkmm.data.models.PlayerData
import com.famas.doodlekingkmm.data.models.PlayerList

data class GameScreenState(
    val loading: Boolean = false,
    val messages: List<BaseModel> = listOf(
        ChatMessage("Venky", "", "Hello world", 0L)
    ),
    val playersList: List<PlayerData> = emptyList(),
    val newWords: NewWords? = null
)