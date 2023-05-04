package com.famas.doodlekingkmm.presentation.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration


@Composable
fun CanvasBox(
    canvasController: CanvasController,
    modifier: Modifier = Modifier.fillMaxSize(),
    defaultBackgroundColor: Color = Color.White,
    drawingEnabled: Boolean = true
) {
    Box {
        Canvas(modifier = modifier
            .background(canvasController.bgColor ?: defaultBackgroundColor)
            .pointerInput(drawingEnabled) {
                if (drawingEnabled) {
                    detectTapGestures(
                        onTap = { offset ->
                            canvasController.insertNewPathFromUserInput(offset)
                            canvasController.updateLatestPathFromUserInput(offset)
                        }
                    )
                }
            }
            .pointerInput(drawingEnabled) {
                if (drawingEnabled) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            canvasController.insertNewPathFromUserInput(offset)
                        }
                    ) { change, _ ->
                        val newPoint = change.position
                        canvasController.updateLatestPathFromUserInput(newPoint)
                    }
                }

            }) {
            clipRect {
                canvasController.pathList.forEach { pw ->
                    drawPath(
                        createPath(pw.points),
                        color = pw.strokeColor,
                        style = Stroke(
                            width = pw.strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }
        IconButton(onClick = {
            canvasController.unDo(true)
        }, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(Icons.Default.Undo, null)
        }
    }
}
