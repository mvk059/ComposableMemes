package fyi.manpreet.composablememes.platform.storage

import fyi.manpreet.composablememes.usecase.MainActivityUseCase
import fyi.manpreet.composablememes.util.MemeConstants
import kotlinx.io.files.Path

actual class StorageManager(
    private val mainActivityUseCase: MainActivityUseCase,
) {
    actual fun getStorageDir() =
        Path("${mainActivityUseCase.requireActivity().filesDir.path}${MemeConstants.STORAGE_FILE_NAME}")
}
