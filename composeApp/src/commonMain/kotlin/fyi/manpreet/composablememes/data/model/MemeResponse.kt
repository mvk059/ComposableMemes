package fyi.manpreet.composablememes.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemeResponse(
    val success: Boolean,
    val data: Data
) {

    @Serializable
    data class Data(
        val memes: List<Meme>
    ) {

        @Serializable
        data class Meme(
            val id: String,
            val name: String,
            val url: String,
            val width: Int,
            val height: Int,
            @SerialName("box_count") val boxCount: Int,
            val captions: Int
        )
    }
}

