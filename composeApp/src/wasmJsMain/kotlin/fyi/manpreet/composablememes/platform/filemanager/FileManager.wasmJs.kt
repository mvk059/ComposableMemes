package fyi.manpreet.composablememes.platform.filemanager

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import arrow.core.raise.Raise
import fyi.manpreet.composablememes.data.model.MemeImageName
import fyi.manpreet.composablememes.data.model.MemeImagePath

actual class FileManager {
    actual suspend fun Raise<String>.saveImage(
        bitmap: ImageBitmap,
        fileName: MemeImageName
    ): MemeImagePath {
//        TODO("Not yet implemented")
        return MemeImagePath("")
    }

    actual suspend fun Raise<String>.getFullImagePath(relativePath: String): String {
//        TODO("Not yet implemented")
        return ""
    }

    actual suspend fun Raise<String>.deleteImage(fileName: MemeImageName) {
    }

    actual suspend fun Raise<String>.deleteTemporaryImage() {
    }

    actual suspend fun Raise<String>.cropImage(
        imageBitmap: ImageBitmap,
        offset: Offset,
        size: Size
    ): ImageBitmap? {
//        TODO("Not yet implemented")
        return null
    }

    actual suspend fun Raise<String>.shareImage(imageNames: List<MemeImageName>) {
    }
}