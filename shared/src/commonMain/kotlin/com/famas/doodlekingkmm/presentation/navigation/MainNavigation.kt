package com.famas.doodlekingkmm.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import com.famas.doodlekingkmm.presentation.screen_game.GameScreen
import com.famas.doodlekingkmm.presentation.screen_home.HomeScreen

@Composable
internal fun MainNavigation() {
    Navigator(HomeScreen())
}