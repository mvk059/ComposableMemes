package fyi.manpreet.composablememes.platform.filemanager

import androidx.compose.ui.graphics.ImageBitmap

expect class FileManager {
    suspend fun saveImage(bitmap: ImageBitmap, fileName: String)
    suspend fun loadImage(fileName: String): ImageBitmap?
}