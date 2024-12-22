package fyi.manpreet.composablememes.platform.filemanager

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import arrow.core.raise.Raise
import arrow.core.raise.catch
import fyi.manpreet.composablememes.data.model.MemeImageName
import fyi.manpreet.composablememes.data.model.MemeImagePath
import kotlinx.browser.document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.khronos.webgl.Uint8ClampedArray
import org.khronos.webgl.get
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.ImageData
import org.w3c.dom.url.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.js.JsArray
import kotlin.js.JsNumber
import kotlin.js.set
import kotlin.js.toJsNumber

actual class FileManager {
    actual suspend fun Raise<String>.saveImage(
        bitmap: ImageBitmap,
        fileName: MemeImageName
    ): MemeImagePath {
        return withContext(Dispatchers.Main) { // Use Main dispatcher for browser APIs
            catch(
                catch = { raise(it.message ?: "Unknown error saving image") },
                block = {
                    // Convert ImageBitmap to HTML Canvas
                    val canvas = document.createElement("canvas") as HTMLCanvasElement
                    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
                    canvas.width = bitmap.width
                    canvas.height = bitmap.height

                    // Create temporary ImageData from our bitmap
                    val buffer = IntArray(bitmap.width * bitmap.height)
                    bitmap.readPixels(buffer)

                    // Convert RGBA to correct format and create ImageData
                    val uint8Array = Uint8ClampedArray(buffer.size * 4)
                    val jsArray = JsArray<JsNumber>()

                    buffer.forEachIndexed { index, color ->
                        val baseIndex = index * 4
                        // Fill JsArray with the RGBA values
                        jsArray[baseIndex + 0] = ((color shr 16) and 0xff).toJsNumber() // R
                        jsArray[baseIndex + 1] = ((color shr 8) and 0xff).toJsNumber()  // G
                        jsArray[baseIndex + 2] = (color and 0xff).toJsNumber()          // B
                        jsArray[baseIndex + 3] = ((color shr 24) and 0xff).toJsNumber() // A
                    }

                    // Set the entire array at once
                    uint8Array.set(jsArray)

                    val imageData = ImageData(uint8Array, bitmap.width, bitmap.height)
                    ctx.putImageData(imageData, 0.0, 0.0)

                    // Convert canvas to blob
                    val blob = suspendCoroutine { continuation ->
                        canvas.toBlob(
                            _callback = { blob -> continuation.resume(blob) },
                            type = "image/jpeg",
                            // quality = 0.85.toJsNumber()
                        )
                    }

                    // Create download link and trigger download
                    val url = URL.createObjectURL(blob ?: raise("Blob is null"))
                    val a = document.createElement("a") as HTMLAnchorElement
                    a.href = url
                    a.download = fileName.value
                    a.click()

                    // Cleanup
                    URL.revokeObjectURL(url)

                    MemeImagePath(fileName.value)
                }
            )
        }
    }

    actual suspend fun Raise<String>.getFullImagePath(relativePath: String): String {
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
        return withContext(Dispatchers.Main) {
            catch(
                catch = { raise(it.message ?: "Unknown error cropping image") },
                block = {
                    // First convert ImageBitmap to a temporary canvas
                    val sourceCanvas = document.createElement("canvas") as HTMLCanvasElement
                    val sourceCtx = sourceCanvas.getContext("2d") as CanvasRenderingContext2D
                    sourceCanvas.width = imageBitmap.width
                    sourceCanvas.height = imageBitmap.height

                    // Create temporary ImageData from our bitmap
                    val buffer = IntArray(imageBitmap.width * imageBitmap.height)
                    imageBitmap.readPixels(buffer)

                    // Convert RGBA to correct format and create ImageData
                    val uint8Array = Uint8ClampedArray(buffer.size * 4)
                    val jsArray = JsArray<JsNumber>()

                    buffer.forEachIndexed { index, color ->
                        val baseIndex = index * 4
                        jsArray[baseIndex + 0] = ((color shr 16) and 0xff).toJsNumber() // R
                        jsArray[baseIndex + 1] = ((color shr 8) and 0xff).toJsNumber()  // G
                        jsArray[baseIndex + 2] = (color and 0xff).toJsNumber()          // B
                        jsArray[baseIndex + 3] = ((color shr 24) and 0xff).toJsNumber() // A
                    }

                    uint8Array.set(jsArray)
                    val imageData = ImageData(uint8Array, imageBitmap.width, imageBitmap.height)
                    sourceCtx.putImageData(imageData, 0.0, 0.0)

                    // Now create the destination canvas for cropping
                    val destCanvas = document.createElement("canvas") as HTMLCanvasElement
                    val destCtx = destCanvas.getContext("2d") as CanvasRenderingContext2D
                    destCanvas.width = size.width.toInt()
                    destCanvas.height = size.height.toInt()

                    // Draw the cropped portion
                    destCtx.drawImage(
                        image = sourceCanvas,
                        sx = offset.x.toDouble(),
                        sy = offset.y.toDouble(),
                        sw = size.width.toDouble(),
                        sh = size.height.toDouble(),
                        dx = 0.0,
                        dy = 0.0,
                        dw = size.width.toDouble(),
                        dh = size.height.toDouble()
                    )

                    // Create new ImageBitmap from the cropped canvas
                    val newImageData = destCtx.getImageData(
                        sx = 0.0,
                        sy = 0.0,
                        sw = size.width.toDouble(),
                        sh = size.height.toDouble()
                    )
                    val byteArray = ByteArray(size.width.toInt() * size.height.toInt() * 4)

                    // Properly convert RGBA values
                    var byteIndex = 0
                    for (i in 0 until newImageData.data.length step 4) {
                        // Canvas gives us RGBA, but we need BGRA for Skia
                        byteArray[byteIndex + 2] = newImageData.data[i + 0]     // R -> B
                        byteArray[byteIndex + 1] = newImageData.data[i + 1]     // G -> G
                        byteArray[byteIndex + 0] = newImageData.data[i + 2]     // B -> R
                        byteArray[byteIndex + 3] = newImageData.data[i + 3]     // A -> A
                        byteIndex += 4
                    }

                    org.jetbrains.skia.Image.makeRaster(
                        imageInfo = org.jetbrains.skia.ImageInfo.makeN32(
                            width = size.width.toInt(),
                            height = size.height.toInt(),
                            alphaType = org.jetbrains.skia.ColorAlphaType.PREMUL
                        ),
                        bytes = byteArray,
                        rowBytes = size.width.toInt() * 4
                    ).toComposeImageBitmap()
                }
            )
        }
    }

    actual suspend fun Raise<String>.shareImage(imageNames: List<MemeImageName>) {
    }
}
