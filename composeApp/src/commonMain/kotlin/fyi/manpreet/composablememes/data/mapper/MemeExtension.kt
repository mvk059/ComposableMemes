package fyi.manpreet.composablememes.data.mapper

import com.composables.core.SheetDetent

val Peek = SheetDetent(identifier = "peek") { containerHeight, sheetHeight ->
    containerHeight * 0.5f
}