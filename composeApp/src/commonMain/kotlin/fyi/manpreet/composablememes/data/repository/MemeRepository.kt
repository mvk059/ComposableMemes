package fyi.manpreet.composablememes.data.repository

import fyi.manpreet.composablememes.data.model.Meme

interface MemeRepository {

    suspend fun insertMeme(meme: Meme)

    fun getMemesSortedByFavorites(): List<Meme>

    fun getMemesSortedByDate(): List<Meme>

    suspend fun updateMeme(meme: Meme)

    suspend fun deleteMemes(memes: List<Meme>)

}