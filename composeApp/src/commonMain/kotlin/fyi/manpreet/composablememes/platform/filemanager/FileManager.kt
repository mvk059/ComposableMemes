package fyi.manpreet.composablememes.platform.filemanager

import androidx.compose.ui.graphics.ImageBitmap
import arrow.core.raise.Raise

expect class FileManager {
    suspend fun Raise<String>.saveImage(bitmap: ImageBitmap, fileName: String): String
//    suspend fun loadImage(fileName: String): ImageBitmap?
//    suspend fun loadAllImages(): List<ImageBitmap>
//    suspend fun platformGetImageReferences(): List<ImageReference>
}