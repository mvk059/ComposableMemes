package fyi.manpreet.composablememes.usecase

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.SimCardDownload
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.ic_font
import composablememes.composeapp.generated.resources.ic_font_color
import composablememes.composeapp.generated.resources.ic_font_size
import composablememes.composeapp.generated.resources.meme_editor_share_save_to_device_message
import composablememes.composeapp.generated.resources.meme_editor_share_save_to_device_title
import composablememes.composeapp.generated.resources.meme_editor_share_share_image_message
import composablememes.composeapp.generated.resources.meme_editor_share_share_image_title
import fyi.manpreet.composablememes.platform.platform.Platform
import fyi.manpreet.composablememes.platform.platform.Platforms
import fyi.manpreet.composablememes.ui.meme.state.FontFamilyType
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.ShareOption

class MemeEditorConfigUseCase {

    fun provideMemeEditorOptions() = MemeEditorOptions(
        editorSize = IntSize.Zero,
        imageContentSize = Size.Zero,
        imageContentOffset = Offset.Zero,
        options = listOf(
            MemeEditorOptions.Options(
                type = MemeEvent.EditorOptionsBottomBarEvent.Font,
                icon = Res.drawable.ic_font,
            ),
            MemeEditorOptions.Options(
                type = MemeEvent.EditorOptionsBottomBarEvent.FontSize,
                icon = Res.drawable.ic_font_size,
            ),
            MemeEditorOptions.Options(
                type = MemeEvent.EditorOptionsBottomBarEvent.FontColor,
                icon = Res.drawable.ic_font_color,
            )
        ),
        isUndoEnabled = false,
        isRedoEnabled = false,
        selectedOption = MemeEvent.EditorOptionsBottomBarEvent.Font
    )

    fun provideMemeEditorSelectionOptions() =
        MemeEditorSelectionOptions(
            font = provideFonts(),
            fontSize = MemeEditorSelectionOptions.DEFAULT_FONT_SIZE,
            fontColors = provideFontColors(),
        )

    private fun provideFonts() =
        MemeEditorSelectionOptions.Fonts(
            example = "Good",
            fonts = provideFontList(),
        )

    private fun provideFontColors() =
        listOf(
            MemeEditorSelectionOptions.FontColor(
                id = 1,
                color = Color.White,
            ),
            MemeEditorSelectionOptions.FontColor(
                id = 2,
                color = Color.Yellow,
            ),
            MemeEditorSelectionOptions.FontColor(
                id = 3,
                color = Color.Red,
            ),
            MemeEditorSelectionOptions.FontColor(
                id = 4,
                color = Color.Magenta,
            ),
            MemeEditorSelectionOptions.FontColor(
                id = 5,
                color = Color.Cyan,
            ),
            MemeEditorSelectionOptions.FontColor(
                id = 6,
                color = Color.Green,
            ),
            MemeEditorSelectionOptions.FontColor(
                id = 7,
                color = Color.Black,
            ),
        )

    private fun provideFontList(): List<MemeEditorSelectionOptions.Fonts.Font> =
        listOf(
            MemeEditorSelectionOptions.Fonts.Font(
                id = FontFamilyType.AntonSC,
                name = "Anton SC",
            ),
            MemeEditorSelectionOptions.Fonts.Font(
                id = FontFamilyType.Jaro,
                name = "Jaro",
            ),
            MemeEditorSelectionOptions.Fonts.Font(
                id = FontFamilyType.RubrikDoodleShadow,
                name = "Rubrik Doodle Shadow",
            ),
            MemeEditorSelectionOptions.Fonts.Font(
                id = FontFamilyType.Lobster,
                name = "Lobster",
            ),
            MemeEditorSelectionOptions.Fonts.Font(
                id = FontFamilyType.DancingScript,
                name = "Dancing Script",
            ),
            MemeEditorSelectionOptions.Fonts.Font(
                id = FontFamilyType.Manrope,
                name = "Manrope",
            ),
            MemeEditorSelectionOptions.Fonts.Font(
                id = FontFamilyType.OpenSans,
                name = "Open Sans",
            ),
            MemeEditorSelectionOptions.Fonts.Font(
                id = FontFamilyType.Roboto,
                name = "Roboto",
            ),
        )

    fun provideShareOptions(platforms: Platforms): List<ShareOption> =
        buildList {
            add(
                ShareOption(
                    title = Res.string.meme_editor_share_save_to_device_title,
                    subtitle = Res.string.meme_editor_share_save_to_device_message,
                    icon = Icons.Outlined.SimCardDownload,
                    type = ShareOption.ShareType.SAVE,
                )
            )

            if (platforms.getPlatform() != Platform.WasmJs) {
                add(
                    ShareOption(
                        title = Res.string.meme_editor_share_share_image_title,
                        subtitle = Res.string.meme_editor_share_share_image_message,
                        icon = Icons.Filled.Share,
                        type = ShareOption.ShareType.SHARE,
                    )
                )
            }
        }

    fun provideShouldShowEditOptions() = false
}