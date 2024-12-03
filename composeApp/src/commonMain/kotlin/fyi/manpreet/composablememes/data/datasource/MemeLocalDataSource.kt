package fyi.manpreet.composablememes.data.datasource

import fyi.manpreet.composablememes.data.database.MemeTable

interface MemeLocalDataSource {

    suspend fun insertMeme(meme: MemeTable)

    suspend fun getMemeById(id: Long): MemeTable?

    suspend fun getMemesSortedByFavorites(): List<MemeTable>

    suspend fun getMemesSortedByDate(): List<MemeTable>

    suspend fun updateMeme(meme: MemeTable)

    suspend fun deleteMemes(memes: List<MemeTable>)

}
