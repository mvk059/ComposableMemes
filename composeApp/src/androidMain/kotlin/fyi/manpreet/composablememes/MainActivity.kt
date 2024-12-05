package fyi.manpreet.composablememes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import fyi.manpreet.composablememes.usecase.MainActivityUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainActivity : ComponentActivity(), KoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        get<MainActivityUseCase>().setActivity(this)

        installSplashScreen()
        setContent {
            App()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        get<MainActivityUseCase>().setActivity(null)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}