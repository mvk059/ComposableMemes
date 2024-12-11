package fyi.manpreet.composablememes.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fyi.manpreet.composablememes.di.DatabaseConstants

@Entity(tableName = DatabaseConstants.TABLE_NAME)
data class MemeTable(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "createdDateInMillis") val createdDateInMillis: Long,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean,
)
