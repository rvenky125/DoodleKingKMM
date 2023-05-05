package com.famas.doodlekingkmm.presentation.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.dp


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
        Row(
            modifier = Modifier.align(Alignment.TopCenter).background(Color.White).padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            canvasController.colors.forEach {
                ColorCircle(
                    color = it,
                    isActive = canvasController.color == it,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    if (drawingEnabled) {
                        canvasController.changeColor(it)
                    }
                }
            }
            IconButton(onClick = {
                canvasController.unDo(true)
            }) {
                Icon(Icons.Default.Undo, null)
            }
        }
    }
}

@Composable
fun ColorCircle(
    color: Color,
    modifier: Modifier = Modifier,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(if (isActive) 28.dp else 23.dp)
            .clip(RoundedCornerShape(50))
            .background(if (isActive) Color.Black else color)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.85f)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}