package fyi.manpreet.composablememes.data.datasource

import fyi.manpreet.composablememes.data.model.MemeTable

interface MemeLocalDataSource {

    suspend fun insertMeme(meme: MemeTable.MemeData)

    suspend fun getMemeById(id: Long): MemeTable.MemeData?

    suspend fun getMemesSortedByFavorites(): List<MemeTable.MemeData>

    suspend fun getMemesSortedByDate(): List<MemeTable.MemeData>

    suspend fun updateMeme(meme: MemeTable.MemeData)

    suspend fun deleteMemes(memes: List<MemeTable.MemeData>)

}
