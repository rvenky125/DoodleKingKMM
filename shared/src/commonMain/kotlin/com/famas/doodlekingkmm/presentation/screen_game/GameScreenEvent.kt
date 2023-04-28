package com.famas.doodlekingkmm.presentation.screen_game

sealed class GameScreenEvent {
    data class OnSelectWord(val word: String): GameScreenEvent()
    data class OnChangeTextInputValue(val textInputValue: String): GameScreenEvent()
    object OnSendMessage: GameScreenEvent()
}
