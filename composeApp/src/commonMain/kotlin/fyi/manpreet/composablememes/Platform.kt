package fyi.manpreet.composablememes

sealed interface Platform {
    data object WasmJs: Platform
    data object Android: Platform
    data object Ios: Platform
}

expect fun getPlatform(): Platform