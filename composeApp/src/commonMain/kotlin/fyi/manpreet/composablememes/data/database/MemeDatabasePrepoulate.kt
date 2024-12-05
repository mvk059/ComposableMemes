package fyi.manpreet.composablememes.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.use
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.allDrawableResources
import fyi.manpreet.composablememes.di.DatabaseConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
fun prepopulateDatabase(): RoomDatabase.Callback {
    return object : RoomDatabase.Callback() {
        override fun onCreate(connection: SQLiteConnection) {
            super.onCreate(connection)

            CoroutineScope(Dispatchers.IO).launch {
                val memes = Res.allDrawableResources.filter { it.key.endsWith("Meme") }
                memes.forEach { (key, _) ->
                    val meme = MemeTable(
                        imageUrl = key,
                        createdDateInMillis = Clock.System.now().toEpochMilliseconds(),
                        isFavorite = false
                    )
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
