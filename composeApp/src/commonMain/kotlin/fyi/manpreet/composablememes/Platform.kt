package fyi.manpreet.composablememes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform