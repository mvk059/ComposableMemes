package fyi.manpreet.composablememes.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.use
import fyi.manpreet.composablememes.di.DatabaseConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

fun prepopulateDatabase(): RoomDatabase.Callback {
    return object : RoomDatabase.Callback() {
        override fun onCreate(connection: SQLiteConnection) {
            super.onCreate(connection)

            val memes = listOf(
                MemeTable(
                    imageUrl = "url1",
                    createdDateInMillis = Clock.System.now().toEpochMilliseconds(),
                    isFavorite = false
                ),
                MemeTable(
                    imageUrl = "url2",
                    createdDateInMillis = Clock.System.now().toEpochMilliseconds(),
                    isFavorite = false
                )
            )

            CoroutineScope(Dispatchers.IO).launch {
                memes.forEach { meme ->
                    connection.prepare("INSERT INTO ${DatabaseConstants.TABLE_NAME} (imageUrl, createdDateInMillis, isFavorite) VALUES (?, ?, ?)")
                        .use { statement ->
                            statement.bindText(1, meme.imageUrl)
                            statement.bindLong(2, meme.createdDateInMillis)
                            statement.bindLong(3, if (meme.isFavorite) 1 else 0)
                            statement.step()
                            statement.close()
                        }
                }
            }
        }
    }
}
