package fyi.manpreet.composablememes.data.datasource

import fyi.manpreet.composablememes.data.model.MemeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MemeRemoteDataSourceImpl(
    private val client: HttpClient,
) : MemeRemoteDataSource {

    private val baseUrl = "https://api.imgflip.com/"

    override suspend fun getAllMemes(): MemeResponse =
        client.get("$baseUrl/get_memes").body<MemeResponse>()

}