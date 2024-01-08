package com.famas.doodlekingkmm.presentation.screen_game

sealed class GameScreenEvent {
    data class OnSelectWord(val word: String): GameScreenEvent()
    data class OnChangeTextInputValue(val textInputValue: String): GameScreenEvent()
    data class OnLayout(val width: Int, val height: Int) : GameScreenEvent()
    object OnSendMessage: GameScreenEvent()
}
