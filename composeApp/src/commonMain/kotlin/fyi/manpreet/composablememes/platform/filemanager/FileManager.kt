package fyi.manpreet.composablememes.platform.filemanager

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import arrow.core.raise.Raise

expect class FileManager {
    suspend fun Raise<String>.saveImage(bitmap: ImageBitmap, fileName: String): String
    suspend fun Raise<String>.deleteImage(fileName: String)
    suspend fun Raise<String>.deleteTemporaryImage()
    suspend fun Raise<String>.cropImage(imageBitmap: ImageBitmap, offset: Offset, size: Size): ImageBitmap?
    suspend fun Raise<String>.shareImage(imageNames: List<String>)
}