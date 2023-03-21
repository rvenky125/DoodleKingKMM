package com.famas.doodlekingkmm.presentation.screen_game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.famas.doodlekingkmm.presentation.components.canvas.CanvasBox
import moe.tlaster.precompose.ui.viewModel

@Composable
internal fun GameScreen() {
    val viewModel = viewModel {
        GameScreenVM()
    }

    Column {
        CanvasBox(
            canvasController = viewModel.canvasController,
            modifier = Modifier.fillMaxWidth().height(400.dp)
        )
    }
}