package com.famas.doodlekingkmm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

@Composable
fun Application() {
    LaunchedEffect(Unit) {
        Napier.base(DebugAntilog())
    }

    App()
}