package com.famas.doodlekingkmm.presentation.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun CanvasBox(
    canvasController: CanvasController,
    modifier: Modifier = Modifier.fillMaxSize(),
    defaultBackgroundColor: Color = Color.White,
) {
    Canvas(modifier = modifier
        .background(canvasController.bgColor ?: defaultBackgroundColor)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { offset ->
                    canvasController.insertNewPath(offset)
                    canvasController.updateLatestPath(offset)
                    canvasController.pathList
                }
            )
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    canvasController.insertNewPath(offset)
                }
            ) { change, _ ->
                val newPoint = change.position
                canvasController.updateLatestPath(newPoint)
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
}
