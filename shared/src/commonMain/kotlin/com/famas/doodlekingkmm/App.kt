package com.famas.doodlekingkmm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.famas.doodlekingkmm.presentation.navigation.MainNavigation
import com.famas.doodlekingkmm.theme.DoodleKingKmmTheme
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
internal fun App() {
    val navigator = rememberNavigator()

    DoodleKingKmmTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            MainNavigation()
        }
    }
}