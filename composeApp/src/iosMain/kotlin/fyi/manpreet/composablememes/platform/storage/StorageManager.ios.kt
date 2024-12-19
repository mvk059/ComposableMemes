package fyi.manpreet.composablememes.platform.storage

import fyi.manpreet.composablememes.util.MemeConstants
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class StorageManager {

    @OptIn(ExperimentalForeignApi::class)
    actual fun getStorageDir(): Path {
        val fileManager: NSFileManager = NSFileManager.defaultManager
        val documentsUrl: NSURL = fileManager.URLForDirectory(
            directory = NSDocumentDirectory,
            appropriateForURL = null,
            create = false,
            inDomain = NSUserDomainMask,
            error = null
        ) ?: return Path("")

        return Path("${documentsUrl.path}${MemeConstants.STORAGE_FILE_NAME}")
    }
}
