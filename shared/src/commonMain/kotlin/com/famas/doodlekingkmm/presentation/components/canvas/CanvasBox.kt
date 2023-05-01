package com.famas.doodlekingkmm.presentation.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    drawingEnabled: Boolean = true
) {
    Canvas(modifier = modifier
        .background(canvasController.bgColor ?: defaultBackgroundColor)
        .pointerInput(Unit) {
            if (drawingEnabled) {
                detectTapGestures(
                    onTap = { offset ->
                        canvasController.insertNewPathFromUserInput(offset)
                        canvasController.updateLatestPathFromUserInput(offset)
                    }
                )
            }
        }
        .pointerInput(Unit) {
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
}
