package fyi.manpreet.composablememes.ui.meme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.meme.components.bottombar.MemeBottomBar
import fyi.manpreet.composablememes.ui.meme.components.dialog.BackConfirmationDialog
import fyi.manpreet.composablememes.ui.meme.components.meme.MemeImage
import fyi.manpreet.composablememes.ui.meme.components.topbar.MemeTopBar
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent

@Composable
fun MemeScreenContent(
    modifier: Modifier = Modifier,
    meme: Meme?,
    isBackDialogVisible: Boolean,
    onBackConfirmClickTopBar: (MemeEvent.TopBarEvent) -> Unit,
    onCancelClickDialog: (MemeEvent.TopBarEvent) -> Unit,
    onBackClickDialog: () -> Unit
) {

    if (meme == null) return

    Scaffold(
        topBar = {
            MemeTopBar(
                onBackClick = onBackConfirmClickTopBar,
            )
        },
        bottomBar = {
            MemeBottomBar()
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MemeImage(
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                meme = meme,
            )
        }
    }


    if (isBackDialogVisible) {
        BackConfirmationDialog(
            onBack = onBackClickDialog,
            onCancel = onCancelClickDialog
        )
    }
}
