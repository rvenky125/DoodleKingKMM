package com.famas.doodlekingkmm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.famas.doodlekingkmm.theme.DoodleKingKmmTheme

@Composable
internal fun App() {
    DoodleKingKmmTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                Text("Hello world")
                Button({}) {
                    Text("Click me")
                }
            }
        }
    }
}