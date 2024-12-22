package fyi.manpreet.composablememes.ui.meme.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.core.*
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.common_cancel
import composablememes.composeapp.generated.resources.common_leave
import composablememes.composeapp.generated.resources.meme_dialog_leave_subtitle
import composablememes.composeapp.generated.resources.meme_dialog_leave_title
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackConfirmationDialog(
    modifier: Modifier = Modifier,
    onBack: (MemeEvent.TopBarEvent) -> Unit,
    onCancel: (MemeEvent.TopBarEvent) -> Unit
) {

    val dialogState = rememberDialogState(initiallyVisible = true)

    Box {

        Dialog(
            state = dialogState,
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            )
        ) {
            Scrim()
            DialogPanel(
                modifier = modifier
                    .displayCutoutPadding()
                    .systemBarsPadding()
                    .widthIn(max = MaterialTheme.spacing.memeBackDialogSize)    // Large size so that it fills the width in phones and shows smaller size in web
                    .wrapContentHeight()
                    .padding(MaterialTheme.spacing.large)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(MaterialTheme.spacing.small)
                    ),
            ) {
                Column {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.large)
                            .padding(top = MaterialTheme.spacing.large)
                    ) {
                        Text(
                            text = stringResource(Res.string.meme_dialog_leave_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = stringResource(Res.string.meme_dialog_leave_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiary,
                        )
                    }

                    Spacer(Modifier.height(MaterialTheme.spacing.small))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {

                        TextButton(
                            onClick = { onCancel(MemeEvent.TopBarEvent.Cancel) },
                            content = {
                                Text(
                                    text = stringResource(Res.string.common_cancel),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.fixedAccentColors.secondaryFixedDim,
                                )
                            }
                        )

                        Spacer(Modifier.width(MaterialTheme.spacing.small))

                        TextButton(
                            onClick = {
                                dialogState.visible = false
                                onBack(MemeEvent.TopBarEvent.GoBack)
                            },
                            content = {
                                Text(
                                    text = stringResource(Res.string.common_leave),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.fixedAccentColors.secondaryFixedDim,
                                )
                            }
                        )

                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                    }
                }
            }
        }
    }
}
