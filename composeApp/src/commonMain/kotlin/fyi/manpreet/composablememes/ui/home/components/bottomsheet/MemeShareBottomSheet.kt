package fyi.manpreet.composablememes.ui.home.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composables.core.Icon
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Scrim
import com.composables.core.Sheet
import fyi.manpreet.composablememes.ui.meme.state.ShareOption
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun MemeShareBottomSheet(
    modifier: Modifier = Modifier,
    state: ModalBottomSheetState,
    shareOptions: List<ShareOption>,
    onShareClick: (ShareOption.ShareType) -> Unit,
) {

    ModalBottomSheet(state = state) {
        Scrim()
        Sheet(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                ) {

                    shareOptions.forEach { option ->
                        ShareItem(
                            modifier = Modifier.clickable { onShareClick(option.type) },
                            option = option,
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun ShareItem(
    modifier: Modifier = Modifier,
    option: ShareOption,
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            imageVector = option.icon,
            contentDescription = null,
            tint = MaterialTheme.fixedAccentColors.secondaryFixedDim,
            modifier = Modifier.padding(MaterialTheme.spacing.small),
        )

        Column {

            Text(
                text = stringResource(option.title),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.fixedAccentColors.secondaryFixedDim,
            )

            Text(
                text = stringResource(option.subtitle),
                modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}