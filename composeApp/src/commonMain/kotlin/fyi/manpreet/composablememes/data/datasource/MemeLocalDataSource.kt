package fyi.manpreet.composablememes.data.datasource

import fyi.manpreet.composablememes.data.database.MemeTable
import kotlinx.coroutines.flow.Flow

interface MemeLocalDataSource {

    suspend fun insertMeme(meme: MemeTable)

    fun getMemesSortedByFavorites(): Flow<List<MemeTable>>

    fun getMemesSortedByDate(): Flow<List<MemeTable>>

    suspend fun updateMeme(meme: MemeTable)

    suspend fun deleteMemes(memes: List<MemeTable>)

}
