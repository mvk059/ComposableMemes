package fyi.manpreet.composablememes.platform.filemanager

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.ensure
import fyi.manpreet.composablememes.data.model.MemeImageName
import fyi.manpreet.composablememes.data.model.MemeImagePath
import fyi.manpreet.composablememes.usecase.MainActivityUseCase
import fyi.manpreet.composablememes.util.MemeConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class FileManager(
    private val mainActivityUseCase: MainActivityUseCase,
) {

    actual suspend fun Raise<String>.saveImage(
        bitmap: ImageBitmap,
        fileName: MemeImageName
    ): MemeImagePath {
        return withContext(Dispatchers.IO) {
            val activity = mainActivityUseCase.requireActivity()

            catch(
                catch = {
                    raise(it.message ?: "Unknown error saving image")
                },
                block = {
                    // Save the file
                    activity.openFileOutput(fileName.value, Context.MODE_PRIVATE)
                        .use { outputStream ->
                            val androidBitmap = bitmap.asAndroidBitmap()
                            androidBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                        }
                    // After the file is properly closed, get its path
                    val file = File(activity.filesDir, fileName.value)
                    ensure(file.exists()) { "Could not find saved file: $fileName" }
                    MemeImagePath(file.path)
                }
            )
        }
    }

    actual suspend fun Raise<String>.getFullImagePath(relativePath: String): String = relativePath

    actual suspend fun Raise<String>.deleteImage(fileName: MemeImageName) {
        withContext(Dispatchers.IO) {
            val activity = mainActivityUseCase.requireActivity()

            catch(
                catch = { e -> "Failed to delete image: ${e.message}" },
                block = {
                    val file = File(activity.filesDir, fileName.value)
                    ensure(file.exists()) { "Failed to get file path" }
                    val deleted = file.delete()
                    ensure(deleted) { "Failed to delete file: $fileName" }
                }
            )
        }
    }

    actual suspend fun Raise<String>.deleteTemporaryImage() {
        withContext(Dispatchers.IO) {

            catch(
                catch = { e -> "Failed to delete image: ${e.message}" },
                block = {
                    val activity = mainActivityUseCase.requireActivity()
                    val tempFiles = activity.filesDir
                        .walk()
                        .filter { file ->
                            file.isFile && file.name.startsWith(MemeConstants.TEMP_FILE_NAME)
                        }
                        .toList()

                    if (tempFiles.isEmpty()) {
                        return@catch
                    }

                    val failedDeletions = tempFiles.mapNotNull { file ->
                        if (!file.delete()) file.absolutePath
                        else null
                    }

                    ensure(failedDeletions.isEmpty()) { "Failed to delete some temporary files: ${failedDeletions.joinToString()}" }
                }
            )
        }
    }

    actual suspend fun Raise<String>.cropImage(
        imageBitmap: ImageBitmap,
        offset: Offset,
        size: Size
    ): ImageBitmap? {
        return withContext(Dispatchers.IO) {

            catch(
                catch = { null },
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

    actual suspend fun Raise<String>.shareImage(imageNames: List<MemeImageName>) {
        withContext(Dispatchers.IO) {
            val activity = mainActivityUseCase.requireActivity()

            catch(
                catch = { emptyList<String>() },
                block = {
                    ensure(imageNames.isNotEmpty()) { "No images provided for sharing" }

                    val contentUris = imageNames.map { imageName ->
                        val imageFile = File(activity.filesDir, imageName.value)
                        ensure(imageFile.exists()) { "Image file not found at path: $imageName" }

                        FileProvider.getUriForFile(
                            /* context = */ activity,
                            /* authority = */ "${activity.packageName}.fileprovider",
                            /* file = */ imageFile
                        )
                    }

                    val shareIntent = if (contentUris.size == 1) {
                        Intent(Intent.ACTION_SEND).apply {
                            type = "image/jpeg"
                            putExtra(Intent.EXTRA_STREAM, contentUris.first())
                        }
                    } else {
                        Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                            type = "image/jpeg"
                            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(contentUris))
                        }
                    }

                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    val chooserIntent = Intent.createChooser(
                        /* target = */ shareIntent,
                        /* title = */ if (contentUris.size == 1) "Share Image" else "Share Images"
                    )
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(chooserIntent)
                }
            )
        }
    }
}