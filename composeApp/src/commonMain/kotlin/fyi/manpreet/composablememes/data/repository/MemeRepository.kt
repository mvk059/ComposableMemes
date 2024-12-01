package fyi.manpreet.composablememes.data.repository

import fyi.manpreet.composablememes.data.model.Meme
import kotlinx.coroutines.flow.Flow

interface MemeRepository {

    suspend fun insertMeme(meme: Meme)

    fun getMemesSortedByFavorites(): Flow<List<Meme>>

    fun getMemesSortedByDate(): Flow<List<Meme>>

    suspend fun updateMeme(meme: Meme)

    suspend fun deleteMemes(memes: List<Meme>)

}