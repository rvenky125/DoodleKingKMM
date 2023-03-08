package com.famas.doodlekingkmm

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.window.Application
import moe.tlaster.precompose.PreComposeApplication
import org.jetbrains.skiko.SystemTheme
import org.jetbrains.skiko.currentSystemTheme
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController =
    PreComposeApplication("Example Application") {
        App()
    }

//fun setSafeArea(start: CGFloat, top: CGFloat, end: CGFloat, bottom: CGFloat) {
//    safeAreaState.value = PaddingValues(start.dp, top.dp, end.dp, bottom.dp)
//}
//
//fun setDarkMode() {
//    darkmodeState.value = currentSystemTheme == SystemTheme.DARK
//}