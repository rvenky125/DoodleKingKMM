package com.famas.doodlekingkmm

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController =
    ComposeUIViewController { App() }