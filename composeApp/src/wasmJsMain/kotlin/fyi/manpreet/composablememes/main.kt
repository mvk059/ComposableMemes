package fyi.manpreet.composablememes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import fyi.manpreet.composablememes.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()

    CanvasBasedWindow(canvasElementId = "ComposeTarget") {

        Box(modifier = Modifier.fillMaxSize()) {
            App()
        }
    }
}
