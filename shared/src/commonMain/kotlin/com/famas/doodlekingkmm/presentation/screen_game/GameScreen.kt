package com.famas.doodlekingkmm.presentation.screen_game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.famas.doodlekingkmm.presentation.components.canvas.CanvasBox

class GameScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { GameScreenVM() }

        Column {
            CanvasBox(
                canvasController = viewModel.canvasController,
                modifier = Modifier.fillMaxWidth().height(400.dp)
            )
        }
    }
}