package fyi.manpreet.composablememes.platform.platform

sealed interface Platform {
    data object WasmJs: Platform
    data object Android: Platform
    data object Ios: Platform
}

expect class Platforms {
    fun getPlatform(): Platform
}
