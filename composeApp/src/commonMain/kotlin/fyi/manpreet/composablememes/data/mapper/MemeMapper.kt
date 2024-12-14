package fyi.manpreet.composablememes.data.mapper

import fyi.manpreet.composablememes.data.database.MemeTable
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.model.MemeImageName
import fyi.manpreet.composablememes.data.model.MemeImagePath
import fyi.manpreet.composablememes.ui.home.state.MemeListBottomSheet
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.DrawableResource

fun Meme.toMemeTable(): MemeTable {
    val path = this.path
    requireNotNull(path) { "Path cannot be null" }

    return MemeTable(
        id = id,
        imageUrl = imageName.value,
        createdDateInMillis = Clock.System.now().toEpochMilliseconds(),
        path = path.value,
        isFavorite = isFavorite,
    )
}

fun List<Meme>.toMemeTable() =
    map { meme -> meme.toMemeTable() }

fun MemeTable.toMeme(): Meme {
    return Meme(
        id = id,
        imageName = MemeImageName(imageUrl),
        path = MemeImagePath(path),
        isFavorite = isFavorite,
        isSelected = false,
    )
}

fun List<MemeTable>.toMeme(): List<Meme> {
    return this.map { memeTable ->
        memeTable.toMeme()
    }
}

fun Map<String, DrawableResource>.toMemeListBottomSheet(): MemeListBottomSheet {
    val memeList = this.map { (key, _) ->
        Meme(
            imageName = MemeImageName(key),
            path = MemeImagePath(key),
            isFavorite = false,
            isSelected = false,
        )
    }
    return MemeListBottomSheet(memes = memeList)
}
