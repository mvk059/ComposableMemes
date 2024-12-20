package fyi.manpreet.composablememes.data.datasource

import arrow.core.raise.either
import fyi.manpreet.composablememes.data.model.MemeTable
import fyi.manpreet.composablememes.platform.filemanager.FileManager
import io.github.xxfast.kstore.KStore

class MemeLocalDataSourceImpl(
    private val storage: KStore<MemeTable>,
    private val fileManager: FileManager,
) : MemeLocalDataSource {

    override suspend fun insertMeme(meme: MemeTable.MemeData) {
        storage.update { state ->
            val newId = state?.memes?.maxOfOrNull { it.id }?.plus(1) ?: 1L
            val newMeme = meme.copy(id = newId)
            state?.copy(memes = state.memes + newMeme)
        }
    }

    override suspend fun getMemeById(id: Long): MemeTable.MemeData? {
        val memes = storage.get()?.memes ?: return null
        val meme = memes.firstOrNull { it.id == id }
        return meme?.copy(path = getMemePath(meme))
    }

    override suspend fun getMemesSortedByFavorites(): List<MemeTable.MemeData> {
        val memes = storage.get()?.memes ?: return emptyList()
        return memes
            .sortedWith(
                compareByDescending<MemeTable.MemeData> { it.isFavorite }
                    .thenByDescending { it.createdDateInMillis }
            )
            .map { it.copy(path = getMemePath(it)) }
    }

    override suspend fun getMemesSortedByDate(): List<MemeTable.MemeData> {
        val memes = storage.get()?.memes ?: return emptyList()
        return memes
            .sortedByDescending { it.createdDateInMillis }
            .map { it.copy(path = getMemePath(it)) }
    }

    override suspend fun updateMeme(meme: MemeTable.MemeData) {
        storage.update { state ->
            state?.copy(
                memes = state.memes.map { if (it.id == meme.id) meme else it }
            )
        }
    }

    override suspend fun deleteMemes(memes: List<MemeTable.MemeData>) {
        storage.update { state ->
            state?.copy(
                memes = state.memes.filterNot { meme -> memes.any { it.id == meme.id } }
            )
        }
    }

    private suspend fun getMemePath(meme: MemeTable.MemeData): String {
        return either {
            with(fileManager) {
                this@either.getFullImagePath(meme.path)
            }
        }.fold(
            ifLeft = { println("Failed to get image path: $it"); meme.path },
            ifRight = { return it }
        )
    }
}
