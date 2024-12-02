package fyi.manpreet.composablememes.data.datasource

import fyi.manpreet.composablememes.data.database.MemeTable

interface MemeLocalDataSource {

    suspend fun insertMeme(meme: MemeTable)

    fun getMemesSortedByFavorites(): List<MemeTable>

    fun getMemesSortedByDate(): List<MemeTable>

    suspend fun updateMeme(meme: MemeTable)

    suspend fun deleteMemes(memes: List<MemeTable>)

}
