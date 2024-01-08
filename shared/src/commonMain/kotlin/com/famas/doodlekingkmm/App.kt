package com.famas.doodlekingkmm

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.famas.doodlekingkmm.di.getAllModules
import com.famas.doodlekingkmm.presentation.navigation.MainNavigation
import com.famas.doodlekingkmm.theme.DoodleKingKmmTheme
import org.koin.compose.KoinApplication

@Composable
internal fun App() {
    KoinApplication(moduleList = {
        getAllModules()
    }) {
        DoodleKingKmmTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                MainNavigation()
            }
        }
    }
}