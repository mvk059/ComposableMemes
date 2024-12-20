package fyi.manpreet.composablememes.platform.filemanager

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.ensure
import fyi.manpreet.composablememes.data.model.MemeImageName
import fyi.manpreet.composablememes.data.model.MemeImagePath
import fyi.manpreet.composablememes.util.MemeConstants
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.refTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGColorSpaceGetModel
import platform.CoreGraphics.CGColorSpaceRelease
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageCreateWithImageInRect
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.CoreGraphics.CGImageGetBitmapInfo
import platform.CoreGraphics.CGImageGetBytesPerRow
import platform.CoreGraphics.CGImageGetColorSpace
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGImageRelease
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.kCGBitmapByteOrder32Big
import platform.CoreGraphics.kCGBitmapByteOrder32Little
import platform.CoreGraphics.kCGBitmapByteOrderMask
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithData
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

actual class FileManager {

    /**
     * Save image in iOS document directory.
     * Save the image in a relative path instead of a full path as iOS uses a document directory which changes on every launch/reinstall in simulator.
     */
    actual suspend fun Raise<String>.saveImage(
        bitmap: ImageBitmap,
        fileName: MemeImageName
    ): MemeImagePath {
        return withContext(Dispatchers.IO) {
            catch(
                catch = { raise(it.message ?: "Unknown error saving image") },
                block = {
                    val fileManager = NSFileManager.defaultManager
                    val documentDirectory = (fileManager.URLsForDirectory(
                        NSDocumentDirectory,
                        NSUserDomainMask
                    ).firstOrNull() as? NSURL) ?: raise("Failed to get document directory")

                    val relativePath = fileName.value
                    val fileURL =
                        documentDirectory.URLByAppendingPathComponent(relativePath)?.also {
                            if (!fileManager.fileExistsAtPath(it.path ?: "")) {
                                fileManager.createFileAtPath(
                                    path = it.path ?: "",
                                    contents = null,
                                    attributes = null
                                )
                            }
                        } ?: raise("Failed to create file URL")

                    // Convert ImageBitmap to CGImage first, then to UIImage
                    val uiImage: UIImage =
                        bitmap.toUIImage() ?: raise("Failed to convert to UIImage")
                    val jpeg = UIImageJPEGRepresentation(uiImage, 0.85)
                        ?: raise("Failed to create JPEG data")
                    val path = fileURL.path ?: raise("Failed to get file path")
                    val imageData = NSData.dataWithData(jpeg)

                    ensure(
                        imageData.writeToFile(
                            path = path,
                            atomically = true
                        )
                    ) { "Failed to write image data to file" }
                    MemeImagePath(relativePath)
                }
            )
        }
    }

    /**
     * Create the full path from the relative path
     */
    actual suspend fun Raise<String>.getFullImagePath(relativePath: String): String {
        val fileManager = NSFileManager.defaultManager
        val documentDirectory = (fileManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        ).firstOrNull() as? NSURL) ?: raise("Failed to get document directory")
        return documentDirectory.URLByAppendingPathComponent(relativePath)?.path
            ?: raise("Failed to get file path")
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun Raise<String>.deleteImage(fileName: MemeImageName) {
        return withContext(Dispatchers.IO) {
            catch(
                catch = { e -> "Failed to delete image: ${e.message}" },
                block = {
                    val fileManager = NSFileManager.defaultManager
                    val documentDirectory = (fileManager.URLsForDirectory(
                        NSDocumentDirectory,
                        NSUserDomainMask
                    ).firstOrNull() as? NSURL) ?: raise("Failed to get document directory")

                    val fileURL =
                        documentDirectory.URLByAppendingPathComponent(fileName.value)
                            ?: raise("Failed to get file URL")
                    ensure(fileManager.removeItemAtURL(fileURL, null)) { "Failed to delete file" }
                }
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun Raise<String>.deleteTemporaryImage() {
        withContext(Dispatchers.Default) {
            catch(
                catch = { e -> "Failed to delete temporary images: ${e.message}" },
                block = {
                    val fileManager = NSFileManager.defaultManager
                    val documentDirectory = (fileManager.URLsForDirectory(
                        NSDocumentDirectory,
                        NSUserDomainMask
                    ).firstOrNull() as? NSURL)?.path ?: raise("Failed to get document directory")

                    val contents = fileManager.contentsOfDirectoryAtPath(documentDirectory, null)
                        ?.filterIsInstance<String>() ?: raise("Failed to get directory contents")

                    val tempFiles = contents.filter { fileName ->
                        fileName.startsWith(MemeConstants.TEMP_FILE_NAME)
                    }

                    if (tempFiles.isEmpty()) {
                        return@catch
                    }

                    val failedDeletions = tempFiles.mapNotNull { fileName ->
                        val fullPath = "$documentDirectory/$fileName"
                        if (!fileManager.removeItemAtPath(fullPath, null)) fullPath else null
                    }

                    ensure(failedDeletions.isEmpty()) { "Failed to delete temporary files: ${failedDeletions.joinToString()}" }
                }
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun Raise<String>.cropImage(
        imageBitmap: ImageBitmap,
        offset: Offset,
        size: Size
    ): ImageBitmap? {
        return withContext(Dispatchers.IO) {

            catch(
                catch = { e -> throw Exception("Failed to crop image: ${e.message}") },
                block = {
                    val uiImage = imageBitmap.toUIImage()

                    val scale = uiImage?.scale ?: 1.0
                    val cropRect = CGRectMake(
                        x = offset.x * scale,
                        y = offset.y * scale,
                        width = size.width * scale,
                        height = size.height * scale
                    )

                    val cgImage = uiImage?.CGImage
                    val croppedCGImage = CGImageCreateWithImageInRect(cgImage, cropRect)
                    val croppedUIImage = UIImage.imageWithCGImage(croppedCGImage)

                    croppedUIImage.toImageBitmap()
                }
            )
        }
    }

    actual suspend fun Raise<String>.shareImage(imageNames: List<MemeImageName>) {

        catch(
            catch = { e -> emptyList<String>() },
            block = {
                ensure(imageNames.isNotEmpty()) { "No images provided for sharing" }

                val images = withContext(Dispatchers.IO) {
                    val fileManager = NSFileManager.defaultManager
                    val documentDirectory = (fileManager.URLsForDirectory(
                        NSDocumentDirectory,
                        NSUserDomainMask
                    ).firstOrNull() as? NSURL) ?: raise("Failed to get document directory")

                    imageNames.map { imageName ->
                        val fileURL = documentDirectory.URLByAppendingPathComponent(imageName.value)
                            ?: raise("Failed to get file URL for: $imageName")
                        val filePath = fileURL.path
                            ?: raise("Failed to get file path for: $imageName")
                        UIImage.imageWithContentsOfFile(filePath)
                            ?: raise("Failed to load image: $imageName")
                    }
                }

                // Switch to Main dispatcher for UI operations
                withContext(Dispatchers.Main) {

                    val activityItems = images.toMutableList<Any>()
                    val activityViewController = UIActivityViewController(
                        activityItems = activityItems,
                        applicationActivities = null
                    )

                    val rootViewController =
                        UIApplication.sharedApplication.keyWindow?.rootViewController
                    rootViewController?.presentViewController(
                        viewControllerToPresent = activityViewController,
                        animated = true,
                        completion = {}
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage? {
    val width = this.width
    val height = this.height
    val buffer = IntArray(width * height)

    this.readPixels(buffer)

    // Convert RGBA to BGRA for iOS
    for (i in buffer.indices) {
        val color = buffer[i]
        val a = (color shr 24) and 0xff
        val r = (color shr 16) and 0xff
        val g = (color shr 8) and 0xff
        val b = color and 0xff
        // Reconstruct as BGRA
        buffer[i] = (a shl 24) or (b shl 16) or (g shl 8) or r
    }

    val colorSpace = CGColorSpaceCreateDeviceRGB()

    // Explicitly set bitmap info for BGRA format
    val bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst.value or kCGBitmapByteOrder32Little

    val context = CGBitmapContextCreate(
        data = buffer.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = bitmapInfo  // Using our explicit bitmap info
    )

    val cgImage = CGBitmapContextCreateImage(context)
    return cgImage?.let { UIImage.imageWithCGImage(it) }
}

/**
 * Platform-specific image conversion utilities for iOS/macOS to Skia/Compose Multiplatform.
 *
 * Key Platform Details:
 * - iOS/macOS (Core Graphics) typically stores images in BGRA format when using little-endian
 *   architecture (which is common on modern devices)
 * - Skia expects RGBA format for its image data
 * - This conversion is necessary because:
 *   1. Core Graphics (CGImage) uses different byte orders depending on platform and settings
 *   2. Skia expects a consistent RGBA byte order
 *   3. Memory layout differences between platforms need to be handled
 */
@OptIn(ExperimentalForeignApi::class)
private fun UIImage.toSkiaImage(): Image? {
    // Get the underlying CGImage. If null, we can't proceed
    val imageRef = this.CGImage ?: return null

    // Get basic image properties
    val width = CGImageGetWidth(imageRef).toInt()
    val height = CGImageGetHeight(imageRef).toInt()
    val bytesPerRow = CGImageGetBytesPerRow(imageRef)

    // Get the raw image data
    val data = CGDataProviderCopyData(CGImageGetDataProvider(imageRef))
    val bytePointer = CFDataGetBytePtr(data)
    val length = CFDataGetLength(data)

    // Validate data length - must be divisible by 4 for RGBA/BGRA format
    if (length % 4 != 0L) {
        CFRelease(data)
        CGImageRelease(imageRef)
        return null
    }

    // Determine alpha channel handling based on CGImage alpha info
    val alphaType = when (CGImageGetAlphaInfo(imageRef)) {
        // Pre-multiplied alpha: RGB values are multiplied by alpha value
        CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst,
        CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> ColorAlphaType.PREMUL

        // Straight alpha: RGB values are independent of alpha value
        CGImageAlphaInfo.kCGImageAlphaFirst,
        CGImageAlphaInfo.kCGImageAlphaLast -> ColorAlphaType.UNPREMUL

        // No alpha channel present
        CGImageAlphaInfo.kCGImageAlphaNone,
        CGImageAlphaInfo.kCGImageAlphaNoneSkipFirst,
        CGImageAlphaInfo.kCGImageAlphaNoneSkipLast -> ColorAlphaType.OPAQUE

        // Unknown or unsupported alpha format
        else -> ColorAlphaType.UNKNOWN
    }

    // Get color space information from the CGImage
    val cgColorSpace = CGImageGetColorSpace(imageRef)
    val colorSpaceModel = CGColorSpaceGetModel(cgColorSpace)
    val skiaColorSpace = when (colorSpaceModel) {
        1 -> ColorSpace.sRGB  // 1 = kCGColorSpaceModelRGB in Core Graphics
        else -> ColorSpace.sRGB // Default to sRGB for unsupported color spaces
    }
    CGColorSpaceRelease(cgColorSpace)

    // Check the bitmap info to determine byte order
    val bitmapInfo = CGImageGetBitmapInfo(imageRef)
    val byteOrderInfo = bitmapInfo and kCGBitmapByteOrderMask
    val shouldSwapRB = when (byteOrderInfo) {
        kCGBitmapByteOrder32Little -> true  // Little-endian needs swap
        kCGBitmapByteOrder32Big -> false    // Big-endian doesn't need swap
        else -> true // Default to swap (most common case on modern devices)
    }

    // Copy raw bytes to Kotlin ByteArray
    val sourceArray = ByteArray(length.toInt()) { index ->
        bytePointer!![index].toByte()
    }

    // Create a new array for the converted data
    val convertedArray = ByteArray(length.toInt())

    // Convert color format if needed
    for (i in sourceArray.indices step 4) {
        if (shouldSwapRB) {
            // Swap R and B channels for BGRA to RGBA conversion
            // This is needed because:
            // - Core Graphics commonly uses BGRA on little-endian systems
            // - Skia expects RGBA regardless of platform
            convertedArray[i] = sourceArray[i + 2]     // R (was B)
            convertedArray[i + 1] = sourceArray[i + 1] // G (unchanged)
            convertedArray[i + 2] = sourceArray[i]     // B (was R)
            convertedArray[i + 3] = sourceArray[i + 3] // A (unchanged)
        } else {
            // Direct copy if no swap is needed
            convertedArray[i] = sourceArray[i]
            convertedArray[i + 1] = sourceArray[i + 1]
            convertedArray[i + 2] = sourceArray[i + 2]
            convertedArray[i + 3] = sourceArray[i + 3]
        }
    }

    // Clean up Core Foundation/Core Graphics resources
    CFRelease(data)
    CGImageRelease(imageRef)

    // Create and return the Skia Image
    return Image.makeRaster(
        imageInfo = ImageInfo(
            width = width,
            height = height,
            colorType = ColorType.RGBA_8888,  // Skia's expected format
            alphaType = alphaType,
            colorSpace = skiaColorSpace
        ),
        bytes = convertedArray,
        rowBytes = bytesPerRow.toInt(),
    )
}

/**
 * Converts a UIImage to Compose Multiplatform's ImageBitmap format.
 * Falls back to a 1x1 pixel ImageBitmap if conversion fails.
 *
 * @return ImageBitmap representation of the UIImage
 */
fun UIImage.toImageBitmap(): ImageBitmap {
    val skiaImage = this.toSkiaImage() ?: return ImageBitmap(1, 1)
    return skiaImage.toComposeImageBitmap()
}
