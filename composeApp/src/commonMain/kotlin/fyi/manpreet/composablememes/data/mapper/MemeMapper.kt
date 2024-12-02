package fyi.manpreet.composablememes.data.mapper

import fyi.manpreet.composablememes.data.database.MemeTable
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.home.state.MemeListBottomSheet
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource

fun Meme.toMemeTable(): MemeTable {
    return MemeTable(
        id = id,
        imageUrl = imageName,
        createdDateInMillis = createdDate.toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds(),
        isFavorite = isFavorite,
    )
}

fun List<Meme>.toMemeTable() =
    map { meme -> meme.toMemeTable() }

fun List<MemeTable>.toMeme(): List<Meme> {
    return this.map { memeTable ->
        Meme(
            id = memeTable.id,
            imageName = memeTable.imageUrl,
            isFavorite = memeTable.isFavorite,
            createdDate = Instant.fromEpochMilliseconds(memeTable.createdDateInMillis)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }
}

fun Map<String, DrawableResource>.toMemeListBottomSheet(): MemeListBottomSheet {
    val memeList = this.map { (key, _) ->
        Meme(
            imageName = key,
            createdDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            isFavorite = false
        )
    }
    return MemeListBottomSheet(memes = memeList)
}
