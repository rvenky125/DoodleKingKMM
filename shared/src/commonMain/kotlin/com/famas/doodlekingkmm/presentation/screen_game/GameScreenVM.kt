package com.famas.doodlekingkmm.presentation.screen_game

import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.ui.graphics.Color
import com.famas.doodlekingkmm.core.canvas.CanvasController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class GameScreenVM(
): ViewModel() {
    val canvasController = CanvasController()

    init {
        canvasController.changeStrokeWidth(30f)
        viewModelScope.launch {
            delay(500)
            canvasController.changeBgColor(Color.Green)
        }
    }
}