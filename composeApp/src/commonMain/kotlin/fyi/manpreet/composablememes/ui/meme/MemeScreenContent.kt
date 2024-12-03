package fyi.manpreet.composablememes.ui.meme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fyi.manpreet.composablememes.ui.meme.components.dialog.BackConfirmationDialog
import fyi.manpreet.composablememes.ui.meme.components.topbar.MemeTopBar
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent

@Composable
fun MemeScreenContent(
    modifier: Modifier = Modifier,
    isBackDialogVisible: Boolean,
    onBackConfirmClickTopBar: (MemeEvent.TopBarEvent) -> Unit,
    onCancelClickDialog: (MemeEvent.TopBarEvent) -> Unit,
    onBackClickDialog: () -> Unit
) {

    Scaffold(
        topBar = {
            MemeTopBar(
                onBackClick = onBackConfirmClickTopBar,
            )
        },
        bottomBar = {

        }
    ) {

        Text("Hello")
    }


    if (isBackDialogVisible) {
        BackConfirmationDialog(
            onBack = onBackClickDialog,
            onCancel = onCancelClickDialog
        )
    }
}
