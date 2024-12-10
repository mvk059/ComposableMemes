package fyi.manpreet.composablememes.platform.filemanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import fyi.manpreet.composablememes.usecase.MainActivityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class FileManager(
    private val mainActivityUseCase: MainActivityUseCase,
) {

    actual suspend fun saveImage(bitmap: ImageBitmap, fileName: String) {
        withContext(Dispatchers.IO) {
            try {
                mainActivityUseCase.requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE)
                    .use { outputStream ->
                        val androidBitmap = bitmap.asAndroidBitmap()
                        androidBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                    }
            } catch (e: Exception) {
                println("Failed to save image: ${e.message}")
            }
        }
    }

    actual suspend fun loadImage(fileName: String): ImageBitmap? {
        return withContext(Dispatchers.IO) {
            try {
                mainActivityUseCase.requireActivity().openFileInput(fileName).use { inputStream ->
                    val androidBitmap = BitmapFactory.decodeStream(inputStream)
                    androidBitmap.asImageBitmap()
                }
            } catch (e: Exception) {
                println("Failed to read image: ${e.message}")
                null
            }
        }
    }
}