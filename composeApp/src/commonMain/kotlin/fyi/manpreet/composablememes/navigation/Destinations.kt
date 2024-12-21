package fyi.manpreet.composablememes.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeDestination

@Serializable
data class MemeDestination(
    val memeName: String,
    val memePath: String,
    val width: Int,
    val height: Int,
)

@Serializable
object HomeWasmDestination