package com.famas.doodlekingkmm

import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController =
    ComposeUIViewController(configure = {
        onFocusBehavior = OnFocusBehavior.DoNothing
    }) { App() }