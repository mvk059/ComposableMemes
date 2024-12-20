package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

data class ShareOption(
    val title: StringResource,
    val subtitle: StringResource,
    val icon: ImageVector,
    val type: ShareType,
) {
    enum class ShareType {
        SAVE,
        SHARE,
    }
}
