package com.famas.doodlekingkmm.presentation.navigation

import androidx.compose.runtime.Composable
import com.famas.doodlekingkmm.presentation.screen_game.GameScreen
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
internal fun MainNavigation() {
    val navigator = rememberNavigator()
    NavHost(navigator = navigator, initialRoute = Screens.GameScreen.routeName) {
        scene(route = Screens.GameScreen.routeName) {
            GameScreen()
        }
    }
}