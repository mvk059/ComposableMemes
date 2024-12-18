package fyi.manpreet.composablememes.data.datasource

import fyi.manpreet.composablememes.data.model.MemeResponse

interface MemeRemoteDataSource {

    suspend fun getAllMemes(): MemeResponse
}