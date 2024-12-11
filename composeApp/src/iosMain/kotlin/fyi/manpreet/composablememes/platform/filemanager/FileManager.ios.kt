package fyi.manpreet.composablememes.platform.filemanager

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.ensure
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.refTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.skia.*
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

actual class FileManager {

    actual suspend fun Raise<String>.saveImage(bitmap: ImageBitmap, fileName: String): String {
        return withContext(Dispatchers.IO) {
            catch(
                catch = { e -> "Failed to save image: ${e.message}" },
                block = {
                    val fileManager = NSFileManager.defaultManager
                    val documentDirectory = (fileManager.URLsForDirectory(
                        NSDocumentDirectory,
                        NSUserDomainMask
                    ).firstOrNull() as? NSURL) ?: raise("Failed to get document directory")

                    val fileURL = documentDirectory.URLByAppendingPathComponent(fileName)?.also {
                        if (!fileManager.fileExistsAtPath(it.path ?: "")) {
                            fileManager.createFileAtPath(it.path ?: "", contents = null, attributes = null)
                        }
                    } ?: raise("Failed to create file URL")

                    // Convert ImageBitmap to CGImage first, then to UIImage
                    val uiImage: UIImage = bitmap.toUIImage() ?: raise("Failed to convert to UIImage")
                    val jpeg = UIImageJPEGRepresentation(uiImage, 0.85) ?: raise("Failed to create JPEG data")
                    val path = fileURL.path ?: raise("Failed to get file path")
                    val imageData = NSData.dataWithData(jpeg)

                    ensure(imageData.writeToFile(path, atomically = true)) { "Failed to write image data to file" }
                    path
                }
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun Raise<String>.deleteImage(fileName: String) {
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
                        documentDirectory.URLByAppendingPathComponent(fileName) ?: raise("Failed to get file URL")
                    ensure(fileManager.removeItemAtURL(fileURL, null)) { "Failed to delete file" }
                }
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun Raise<String>.cropImage(imageBitmap: ImageBitmap, offset: Offset, size: Size): ImageBitmap {
        return withContext(Dispatchers.IO) {

            catch(
                catch = { e -> throw Exception("Failed to crop image: ${e.message}") },
                block = {
                    val uiImage = imageBitmap.toUIImage()

                    val scale = uiImage?.scale ?: 1.0
                    val cropRect = CGRectMake(
                        offset.x * scale,
                        offset.y * scale,
                        size.width * scale,
                        size.height * scale
                    )

                    val cgImage = uiImage?.CGImage
                    val croppedCGImage = CGImageCreateWithImageInRect(cgImage, cropRect)
                    val croppedUIImage = UIImage.imageWithCGImage(croppedCGImage)

                    croppedUIImage.toImageBitmap()
                }
            )
        }
    }

    /*
    actual suspend fun loadImage(fileName: String): ImageBitmap? {
        return try {
            val fileManager = NSFileManager.defaultManager
            val documentDirectory = (fileManager.URLsForDirectory(
                NSDocumentDirectory,
                NSUserDomainMask
            ).firstOrNull() as? NSURL) ?: return null

            val fileURL = documentDirectory.URLByAppendingPathComponent(fileName) ?: return null
            val nsData = NSData.dataWithContentsOfURL(fileURL) ?: return null

            // Load UIImage from file
            val path = fileURL.path ?: return null
            val uiImage = UIImage(contentsOfFile = path)

            // Convert UIImage back to ImageBitmap
            return uiImage.toImageBitmap()
        } catch (e: Exception) {
            println("Failed to load file: ${e.message}")
            null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun loadAllImages(): List<ImageBitmap> {
        return try {
            val fileManager = NSFileManager.defaultManager
            val documentDirectory = (fileManager.URLsForDirectory(
                NSDocumentDirectory,
                NSUserDomainMask
            ).firstOrNull() as? NSURL) ?: return emptyList()

            val files = fileManager.contentsOfDirectoryAtURL(
                url = documentDirectory,
                includingPropertiesForKeys = null,
                options = NSDirectoryEnumerationSkipsHiddenFiles,
                error = null
            ) ?: return emptyList()

            files.mapNotNull { fileURL ->
                val path = (fileURL as? NSURL)?.path ?: return@mapNotNull null
                val uiImage = UIImage(contentsOfFile = path) ?: return@mapNotNull null
                uiImage.toImageBitmap()

//                val nsData = NSData.dataWithContentsOfURL(fileURL) ?: return@mapNotNull null

                // Load UIImage from file
//                val path = fileURL.path ?: return@mapNotNull null
//                val uiImage = UIImage(contentsOfFile = path)

                // Convert UIImage back to ImageBitmap
//                uiImage?.toImageBitmap()
            } ?: emptyList()
        } catch (e: Exception) {
            println("Failed to load all files: ${e.message}")
            emptyList()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun platformGetImageReferences(): List<ImageReference> {
        return try {
            val fileManager = NSFileManager.defaultManager
            val documentDirectory = (fileManager.URLsForDirectory(
                NSDocumentDirectory,
                NSUserDomainMask
            ).firstOrNull() as? NSURL) ?: return emptyList()

            val files = fileManager.contentsOfDirectoryAtURL(
                url = documentDirectory,
                includingPropertiesForKeys = null,
                options = NSDirectoryEnumerationSkipsHiddenFiles,
                error = null
            ) ?: return emptyList()

            files.mapNotNull { fileURL ->
                val nsurl = fileURL as? NSURL ?: return@mapNotNull null
                val fileName = nsurl.lastPathComponent ?: return@mapNotNull null
                if (fileName.endsWith(".jpg", ignoreCase = true).not()) return@mapNotNull null
                val path = (fileURL as? NSURL)?.path ?: return@mapNotNull null

                // Get file attributes for size
                val attributes = fileManager.attributesOfItemAtPath(path, null)
                val fileSize = (attributes?.get(NSFileSize) as? NSNumber)?.longValue ?: return@mapNotNull null

                ImageReference(
                    id = path,
                    path = path,
                    size = fileSize,
                )
            } ?: emptyList()
        } catch (e: Exception) {
            println("Failed to get image references: ${e.message}")
            emptyList()
        }
    }

     */
}

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage? {
    val width = this.width
    val height = this.height
    val buffer = IntArray(width * height)

    this.readPixels(buffer)

    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val context = CGBitmapContextCreate(
        data = buffer.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value
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

/*
@OptIn(ExperimentalForeignApi::class)
private fun UIImage.toSkiaImage(): Image? {
    val imageRef = this.CGImage ?: return null

    val width = CGImageGetWidth(imageRef).toInt()
    val height = CGImageGetHeight(imageRef).toInt()

    val bytesPerRow = CGImageGetBytesPerRow(imageRef)
    val data = CGDataProviderCopyData(CGImageGetDataProvider(imageRef))
    val bytePointer = CFDataGetBytePtr(data)
    val length = CFDataGetLength(data)

    val alphaType = when (CGImageGetAlphaInfo(imageRef)) {
        CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst,
        CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> ColorAlphaType.PREMUL

        CGImageAlphaInfo.kCGImageAlphaFirst,
        CGImageAlphaInfo.kCGImageAlphaLast -> ColorAlphaType.UNPREMUL

        CGImageAlphaInfo.kCGImageAlphaNone,
        CGImageAlphaInfo.kCGImageAlphaNoneSkipFirst,
        CGImageAlphaInfo.kCGImageAlphaNoneSkipLast -> ColorAlphaType.OPAQUE

        else -> ColorAlphaType.UNKNOWN
    }

    val byteArray = ByteArray(length.toInt()) { index ->
        bytePointer!![index].toByte()
    }

    CFRelease(data)
    CGImageRelease(imageRef)

    val skiaColorSpace = ColorSpace.sRGB
    val colorType = ColorType.RGBA_8888

    // Convert RGBA to BGRA
    for (i in byteArray.indices step 4) {
        val r = byteArray[i]
        val g = byteArray[i + 1]
        val b = byteArray[i + 2]
        val a = byteArray[i + 3]

        byteArray[i] = b
        byteArray[i + 2] = r
    }

    return Image.makeRaster(
        imageInfo = ImageInfo(
            width = width,
            height = height,
            colorType = colorType,
            alphaType = alphaType,
            colorSpace = skiaColorSpace
        ),
        bytes = byteArray,
        rowBytes = bytesPerRow.toInt(),
    )
}

fun UIImage.toImageBitmap(): ImageBitmap {
    val skiaImage = this.toSkiaImage() ?: return ImageBitmap(1, 1)
    return skiaImage.toComposeImageBitmap()
}

 */