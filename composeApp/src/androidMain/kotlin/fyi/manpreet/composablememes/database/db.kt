package fyi.manpreet.composablememes.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import fyi.manpreet.composablememes.data.database.MemeDatabase
import fyi.manpreet.composablememes.di.DatabaseConstants
import kotlinx.coroutines.Dispatchers

fun getMemeDatabase(context: Context): MemeDatabase {
    val dbFile = context.getDatabasePath(DatabaseConstants.DB_NAME)
    return Room.databaseBuilder<MemeDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
