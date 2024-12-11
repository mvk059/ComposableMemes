package fyi.manpreet.composablememes.platform.filemanager

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.ensure
import fyi.manpreet.composablememes.usecase.MainActivityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class FileManager(
    private val mainActivityUseCase: MainActivityUseCase,
) {

    actual suspend fun Raise<String>.saveImage(bitmap: ImageBitmap, fileName: String): String {
        return withContext(Dispatchers.IO) {
            val activity = mainActivityUseCase.requireActivity()

            catch(
                catch = { e -> "Failed to save image: ${e.message}" },
                block = {
                    // Save the file
                    activity.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                        val androidBitmap = bitmap.asAndroidBitmap()
                        androidBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                    }
                    // After the file is properly closed, get its path
                    val file = File(activity.filesDir, fileName)
                    ensure(file.exists()) { "Could not find saved file: $fileName" }
                    file.path
                }
            )
        }
    }

    actual suspend fun Raise<String>.deleteImage(fileName: String) {
        withContext(Dispatchers.IO) {
            val activity = mainActivityUseCase.requireActivity()

            catch(
                catch = { e -> throw Exception("Failed to delete image: ${e.message}") },
                block = {
                    val file = File(activity.filesDir, fileName)
                    ensure(file.exists()) { "Failed to get file path" }
                    val deleted = file.delete()
                    ensure(deleted) { "Failed to delete file: $fileName" }
                }
            )
        }
    }

    actual suspend fun Raise<String>.cropImage(imageBitmap: ImageBitmap, offset: Offset, size: Size): ImageBitmap {
        return withContext(Dispatchers.IO) {

            catch(
                catch = { e -> throw Exception("Failed to crop image: ${e.message}") },
                block = {
                    val androidBitmap = imageBitmap.asAndroidBitmap()
                    val croppedBitmap = Bitmap.createBitmap(
                        androidBitmap,
                        offset.x.toInt(),
                        offset.y.toInt(),
                        size.width.toInt(),
                        size.height.toInt(),
                    )
                    croppedBitmap.asImageBitmap()
                }
            )
        }
    }

    /*
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

        actual suspend fun loadAllImages(): List<ImageBitmap> {
            return withContext(Dispatchers.IO) {
                val files = mainActivityUseCase.requireActivity().filesDir.listFiles()
                files?.mapNotNull { file ->
                    try {
                        mainActivityUseCase.requireActivity().openFileInput(file.name).use { inputStream ->
                            val androidBitmap = BitmapFactory.decodeStream(inputStream)
                            androidBitmap.asImageBitmap()
                        }
                    } catch (e: Exception) {
                        println("Failed to read image: ${e.message}")
                        null
                    }
                } ?: emptyList()
            }
        }

        actual suspend fun platformGetImageReferences(): List<ImageReference> {
             return withContext(Dispatchers.IO) {
                val files = mainActivityUseCase.requireActivity().filesDir.listFiles()
                 files?.mapNotNull { file ->
                     if (file.name.endsWith(".jpg").not()) return@mapNotNull null

                     try {
                         ImageReference(
                             id = file.name,
                             path = file.path,
                         )
                     } catch (e: Exception) {
                         println("Failed to read image: ${e.message}")
                         null
                     }
                 } ?: emptyList()
            }
        }

     */
}