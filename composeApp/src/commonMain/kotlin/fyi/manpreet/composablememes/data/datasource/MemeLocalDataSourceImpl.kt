package fyi.manpreet.composablememes.data.datasource

import fyi.manpreet.composablememes.data.database.MemeDatabase
import fyi.manpreet.composablememes.data.database.MemeTable

class MemeLocalDataSourceImpl(
    private val db: MemeDatabase,
) : MemeLocalDataSource {

    override suspend fun insertMeme(meme: MemeTable) {
        db.memeDao().insertMeme(meme = meme)
    }

    override suspend fun getMemeById(id: Long): MemeTable? = db.memeDao().getMemeById(id = id)

    override suspend fun getMemesSortedByFavorites(): List<MemeTable> =
        db.memeDao().getMemesSortedByFavorites()

    override suspend fun getMemesSortedByDate(): List<MemeTable> = db.memeDao().getMemesSortedByDate()

    override suspend fun updateMeme(meme: MemeTable) {
        db.memeDao().updateMeme(meme = meme)
    }

    override suspend fun deleteMemes(memes: List<MemeTable>) {
        db.memeDao().deleteMemes(memes = memes)
    }

}
