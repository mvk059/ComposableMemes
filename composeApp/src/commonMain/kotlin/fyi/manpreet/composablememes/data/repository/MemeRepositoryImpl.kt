package fyi.manpreet.composablememes.data.repository

import fyi.manpreet.composablememes.data.datasource.MemeLocalDataSource
import fyi.manpreet.composablememes.data.datasource.MemeRemoteDataSource
import fyi.manpreet.composablememes.data.mapper.toMeme
import fyi.manpreet.composablememes.data.mapper.toMemeData
import fyi.manpreet.composablememes.data.model.Meme

class MemeRepositoryImpl(
    private val localDataSource: MemeLocalDataSource,
    private val remoteDataSource: MemeRemoteDataSource,
) : MemeRepository {

    override suspend fun insertMeme(meme: Meme) {
        localDataSource.insertMeme(meme.toMemeData())
    }

    override suspend fun getMemeById(id: Long): Meme? =
        localDataSource.getMemeById(id)?.toMeme()

    override suspend fun getMemesSortedByFavorites(): List<Meme> =
        localDataSource.getMemesSortedByFavorites().toMeme()

    override suspend fun getMemesSortedByDate(): List<Meme> =
        localDataSource.getMemesSortedByDate().toMeme()

    override suspend fun deleteMemes(memes: List<Meme>) {
        localDataSource.deleteMemes(memes.toMemeData())
    }

    override suspend fun updateMeme(meme: Meme) {
        localDataSource.updateMeme(meme.toMemeData())
    }

    override suspend fun getAllMemes(): List<Meme> =
        remoteDataSource.getAllMemes().toMeme()
}
