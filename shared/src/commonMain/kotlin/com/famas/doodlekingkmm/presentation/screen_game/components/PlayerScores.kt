package com.famas.doodlekingkmm.presentation.screen_game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.famas.doodlekingkmm.data.models.PlayerData

@Composable
fun PlayerScores(playersList: List<PlayerData>) {
    Column(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.8f).background(color = Color.White).padding(horizontal = 16.dp).padding(top = 16.dp)) {
        playersList.forEach {
            Column(
                modifier = Modifier.padding(bottom = 16.dp).border(
                    1.dp,
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(25f)
                ).padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(it.username, style = MaterialTheme.typography.bodyLarge)
                    Text("Score: ${it.score}", style = MaterialTheme.typography.bodyMedium)
                }
                Text("rank: ${it.rank}", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}