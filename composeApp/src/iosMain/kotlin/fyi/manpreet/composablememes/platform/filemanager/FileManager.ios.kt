package fyi.manpreet.composablememes.platform.filemanager

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.refTo
import org.jetbrains.skia.*
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

actual class FileManager {

    actual suspend fun saveImage(bitmap: ImageBitmap, fileName: String) {
        try {
            val fileManager = NSFileManager.defaultManager
            val documentDirectory = (fileManager.URLsForDirectory(
                NSDocumentDirectory,
                NSUserDomainMask
            ).firstOrNull() as? NSURL) ?: return

            val fileURL = documentDirectory.URLByAppendingPathComponent(fileName)?.also {
                if (!fileManager.fileExistsAtPath(it.path ?: "")) {
                    fileManager.createFileAtPath(it.path ?: "", contents = null, attributes = null)
                }
            } ?: return

            // Convert ImageBitmap to CGImage first, then to UIImage
            val uiImage: UIImage = bitmap.toUIImage() ?: return
            val jpeg = UIImageJPEGRepresentation(uiImage, 0.85) ?: return
            val path = fileURL.path ?: return
            val imageData = NSData.dataWithData(jpeg)

            imageData.writeToFile(path, atomically = true)
        } catch (e: Exception) {
            println("Failed to save file: ${e.message}")
        }
    }

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