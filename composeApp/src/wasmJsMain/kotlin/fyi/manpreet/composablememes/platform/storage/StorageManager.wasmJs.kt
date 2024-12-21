package fyi.manpreet.composablememes.platform.storage

import fyi.manpreet.composablememes.util.MemeConstants
import kotlinx.io.files.Path

actual class StorageManager {
    actual fun getStorageDir(): Path = Path(MemeConstants.STORAGE_FILE_NAME)
}