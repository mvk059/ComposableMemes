package fyi.manpreet.composablememes.data.repository

import fyi.manpreet.composablememes.data.datasource.MemeLocalDataSource
import fyi.manpreet.composablememes.data.mapper.toMeme
import fyi.manpreet.composablememes.data.mapper.toMemeTable
import fyi.manpreet.composablememes.data.model.Meme
import kotlinx.coroutines.flow.Flow

class MemeRepositoryImpl(
    private val localDataSource: MemeLocalDataSource,
) : MemeRepository {

    override suspend fun insertMeme(meme: Meme) {
        localDataSource.insertMeme(meme.toMemeTable())
    }

    override fun getMemesSortedByFavorites(): Flow<List<Meme>> =
        localDataSource.getMemesSortedByFavorites().toMeme()

    override fun getMemesSortedByDate(): Flow<List<Meme>> =
        localDataSource.getMemesSortedByDate().toMeme()

    override suspend fun deleteMemes(memes: List<Meme>) {
        localDataSource.deleteMemes(memes.toMemeTable())
    }

    override suspend fun updateMeme(meme: Meme) {
        localDataSource.updateMeme(meme.toMemeTable())
    }
}
