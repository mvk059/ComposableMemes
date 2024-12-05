package fyi.manpreet.composablememes.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fyi.manpreet.composablememes.di.DatabaseConstants

@Dao
interface MemeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeme(meme: MemeTable)

    @Update
    suspend fun updateMeme(meme: MemeTable)

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_NAME} WHERE id = :id")
    suspend fun getMemeById(id: Long): MemeTable?

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_NAME} ORDER BY isFavorite DESC, createdDateInMillis DESC")
    suspend fun getMemesSortedByFavorites(): List<MemeTable>

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_NAME} ORDER BY createdDateInMillis DESC")
    suspend fun getMemesSortedByDate(): List<MemeTable>

    @Delete
    suspend fun deleteMemes(memes: List<MemeTable>)

}
