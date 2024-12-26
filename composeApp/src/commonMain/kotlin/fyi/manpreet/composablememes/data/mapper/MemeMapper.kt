package fyi.manpreet.composablememes.data.mapper

import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.model.MemeImageName
import fyi.manpreet.composablememes.data.model.MemeImagePath
import fyi.manpreet.composablememes.data.model.MemeResponse
import fyi.manpreet.composablememes.data.model.MemeTable
import kotlinx.datetime.Clock

fun Meme.toMemeData(): MemeTable.MemeData {
    val path = this.path
    requireNotNull(path) { "Path cannot be null" }

    return MemeTable.MemeData(
        id = id,
        imageUrl = imageName.value,
        createdDateInMillis = Clock.System.now().toEpochMilliseconds(),
        path = path.value.substringAfterLast("/"),
        isFavorite = isFavorite,
    )
}

fun List<Meme>.toMemeData() =
    map { meme -> meme.toMemeData() }

fun MemeTable.MemeData.toMeme(): Meme {
    return Meme(
        id = id,
        imageName = MemeImageName(imageUrl),
        path = MemeImagePath(path),
        isFavorite = isFavorite,
        isSelected = false,
    )
}

fun List<MemeTable.MemeData>.toMeme(): List<Meme> {
    return this.map { memeTable ->
        memeTable.toMeme()
    }
}

fun MemeResponse.toMeme(): List<Meme> {
    return this.data.memes.map { meme ->
        Meme(
            id = meme.id.toLong(),
            imageName = MemeImageName(meme.name),
            path = MemeImagePath(meme.url),
            isFavorite = false,
            isSelected = false,
            width = meme.width,
            height = meme.height,
        )
    }
}
