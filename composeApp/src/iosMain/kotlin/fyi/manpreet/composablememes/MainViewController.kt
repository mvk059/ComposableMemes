package fyi.manpreet.composablememes

import androidx.compose.ui.window.ComposeUIViewController
import fyi.manpreet.composablememes.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
    content = {
        App()
    }
)