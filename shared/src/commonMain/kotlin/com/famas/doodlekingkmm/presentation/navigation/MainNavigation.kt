package com.famas.doodlekingkmm.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.famas.doodlekingkmm.presentation.screen_home.HomeScreen

@Composable
internal fun MainNavigation() {
    Navigator(HomeScreen())
}