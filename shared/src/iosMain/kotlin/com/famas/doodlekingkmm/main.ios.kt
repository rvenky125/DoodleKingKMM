package com.famas.doodlekingkmm

import androidx.compose.ui.window.Application
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController =
    Application("Example Application") {
        App()
    }