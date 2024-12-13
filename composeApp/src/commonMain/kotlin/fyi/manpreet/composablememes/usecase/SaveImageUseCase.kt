package fyi.manpreet.composablememes.usecase

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import arrow.core.Either
import arrow.core.raise.either
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.repository.MemeRepository
import fyi.manpreet.composablememes.platform.filemanager.FileManager
import fyi.manpreet.composablememes.ui.meme.state.ShareOption
import fyi.manpreet.composablememes.util.MemeConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

sealed interface SaveError {
    data object Initial : SaveError
    data class Failure(val message: String) : SaveError
}

class SaveImageUseCase(
    private val repository: MemeRepository,
    private val fileManager: FileManager,
) {

    private val _saveState =
        MutableStateFlow<Either<SaveError, ShareOption.ShareType>>(Either.Left(SaveError.Initial))
    val saveState = _saveState.asStateFlow()

    suspend fun saveImages(imageNames: List<String>) {
        shareImage(imageNames)
    }

    suspend fun saveImage(
        imageBitmap: ImageBitmap,
        imageSize: Size,
        imageOffset: Offset,
        type: ShareOption.ShareType,
        meme: Meme,
    ) {
        cropImage(
            imageBitmap = imageBitmap,
            imageSize = imageSize,
            imageOffset = imageOffset,
            type = type,
            meme = meme,
        )
    }

    private suspend fun cropImage(
        imageBitmap: ImageBitmap,
        imageSize: Size,
        imageOffset: Offset,
        type: ShareOption.ShareType,
        meme: Meme,
    ) {
        return either {
            with(fileManager) {
                this@either.cropImage(imageBitmap, imageOffset, imageSize)
            }
        }.fold(
            ifLeft = { _saveState.update { Either.Left(SaveError.Failure("Failed to crop image: $it")) } },
            ifRight = { bitmap ->
                requireNotNull(bitmap) { "Failed to crop image" }
                saveImage(bitmap, type, meme)
            }
        )
    }

    private suspend fun saveImage(
        imageBitmap: ImageBitmap,
        type: ShareOption.ShareType,
        meme: Meme,
    ) {

        val imageName = when (type) {
            ShareOption.ShareType.SAVE -> "${meme.imageName}_${Clock.System.now().epochSeconds}.jpg"
            ShareOption.ShareType.SHARE -> "${MemeConstants.TEMP_FILE_NAME}${meme.imageName}_${Clock.System.now().epochSeconds}.jpg"
        }

        either {
            with(fileManager) {
                this@either.saveImage(imageBitmap, imageName)
            }
        }.fold(
            ifLeft = { Either.Left(SaveError.Failure("Error saving image: $it")) },
            ifRight = { path ->
                println("Image saved successfully: $path")
                when (type) {
                    ShareOption.ShareType.SAVE -> {
                        repository.insertMeme(meme.copy(imageName = imageName, path = path))
                        _saveState.update { Either.Right(ShareOption.ShareType.SAVE) }
                    }

                    ShareOption.ShareType.SHARE -> shareImage(imageName)
                }
            }
        )
    }

    private suspend fun shareImage(imageName: String) {
        either {
            with(fileManager) {
                this@either.shareImage(listOf(imageName))
            }
        }.fold(
            ifLeft = { Either.Left(SaveError.Failure("Error sharing image: $it")) },
            ifRight = { _saveState.update { Either.Right(ShareOption.ShareType.SHARE) } }
        )
    }

    private suspend fun shareImage(imageName: List<String>) {
        either {
            with(fileManager) {
                this@either.shareImage(imageName)
            }
        }.fold(
            ifLeft = { Either.Left(SaveError.Failure("Error sharing image: $it")) },
            ifRight = { _saveState.update { Either.Right(ShareOption.ShareType.SHARE) } }
        )
    }
}