package fyi.manpreet.composablememes.platform.filemanager

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import arrow.core.raise.Raise
import fyi.manpreet.composablememes.data.model.MemeImageName
import fyi.manpreet.composablememes.data.model.MemeImagePath

expect class FileManager {
    suspend fun Raise<String>.saveImage(bitmap: ImageBitmap, fileName: MemeImageName): MemeImagePath
    suspend fun Raise<String>.getFullImagePath(relativePath: String): String
    suspend fun Raise<String>.deleteImage(fileName: MemeImageName)
    suspend fun Raise<String>.deleteTemporaryImage()
    suspend fun Raise<String>.cropImage(imageBitmap: ImageBitmap, offset: Offset, size: Size): ImageBitmap?
    suspend fun Raise<String>.shareImage(imageNames: List<MemeImageName>)
}