package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.graphics.vector.ImageVector

data class ShareOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val type: ShareType,
) {
    enum class ShareType {
        SAVE,
        SHARE,
    }
}
