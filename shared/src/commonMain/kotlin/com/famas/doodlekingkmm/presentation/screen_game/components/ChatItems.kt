package com.famas.doodlekingkmm.presentation.screen_game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.famas.doodlekingkmm.core.util.asFormattedDate

@Composable
fun ChatItem(message: String, name: String, timestamp: Long, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.triangleRenderModifier(
            triangleRenderType = TriangleRenderType.Right(
                color = MaterialTheme.colorScheme.primaryContainer
            )
        ),
        shape = RoundedCornerShape(25)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Normal
            )
            Row(modifier = Modifier.wrapContentSize(),horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = timestamp.asFormattedDate(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}


fun Modifier.triangleRenderModifier(triangleRenderType: TriangleRenderType): Modifier =
    this.drawBehind {
        val cornerRadius = 10.dp.toPx()
        val triangleHeight = 20.dp.toPx()
        val triangleWidth = 25.dp.toPx()
        val trianglePath = Path().apply {
            if (triangleRenderType is TriangleRenderType.Right) {
                moveTo(size.width, size.height - cornerRadius)
                lineTo(size.width, size.height + triangleHeight)
                lineTo(
                    size.width - triangleWidth,
                    size.height - cornerRadius
                )
                close()
            } else {
                moveTo(0f, size.height - cornerRadius)
                lineTo(0f, size.height + triangleHeight)
                lineTo(triangleWidth, size.height - cornerRadius)
                close()
            }
        }
        drawPath(
            path = trianglePath,
            color = triangleRenderType.color
        )
    }

sealed class TriangleRenderType(val color: Color) {
    class Left(color: Color) : TriangleRenderType(color)
    class Right(color: Color) : TriangleRenderType(color)
}